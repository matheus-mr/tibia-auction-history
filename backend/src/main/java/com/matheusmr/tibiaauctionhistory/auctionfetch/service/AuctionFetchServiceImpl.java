package com.matheusmr.tibiaauctionhistory.auctionfetch.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.net.InetAddresses;
import com.matheusmr.tibiaauctionhistory.auctionfetch.model.PaginatedContentResponse;
import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import com.matheusmr.tibiaauctionhistory.common.model.Item;
import com.matheusmr.tibiaauctionhistory.common.model.Outfit;
import com.matheusmr.tibiaauctionhistory.common.model.Vocation;
import com.matheusmr.tibiaauctionhistory.common.utils.DateUtils;
import com.matheusmr.tibiaauctionhistory.common.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Slf4j
@Profile("AUCTION_FETCHING")
public class AuctionFetchServiceImpl implements AuctionFetchService {

    final static String[] SUPPORTED_CIPHER_SUITES = new String[]{
            "TLS_AES_128_GCM_SHA256",
            "TLS_AES_256_GCM_SHA384",
            "TLS_CHACHA20_POLY1305_SHA256",
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384"
    };

    private static final Locale LOCALE = Locale.US;
    private static final String AUCTION_URL_TEMPLATE = "https://www.tibia.com/charactertrade/?subtopic=pastcharactertrades&page=details&auctionid=%s";
    private static final String PAGINATED_CONTENT_URL_TEMPLATE = "https://www.tibia.com/websiteservices/handle_charactertrades.php?auctionid=%s&type=%s&currentpage=%s";
    private static final DateTimeFormatter AUCTION_START_DATE_END_DATE_TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMM dd yyyy, HH:mm z")
                    .toFormatter()
                    .withLocale(LOCALE);
    private static final DateTimeFormatter CHARACTER_CREATION_DATE_TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMM dd yyyy, HH:mm:ss z")
                    .toFormatter()
                    .withLocale(LOCALE);
    /**
     * The item description may or may not start with the amount, if it doesn't then the amount is one. The amount is an integer with comma as thousands separator.
     * After the amount, comes the item name
     * After the item name may or may not have an item complementary description, which always comes with an end of line character before it
     */
    private static final Pattern ITEM_DESCRIPTION_PATTERN = Pattern.compile(
            "((^[0-9]{1,3}(,[0-9]{3})*)x\s+)?(.*)\\R?.*"
    );
    private static final Pattern AUCTION_HEADER_PATTERN = Pattern.compile(
            ".*Level: ([0-9]*) \\| Vocation: ([a-zA-Z\s]*) \\| (Male|Female) \\| World: ([a-zA-Z]*)\\z"
    );
    private static final int ITEMS_PAGINATED_CONTENT_TYPE_ID = 0;
    private static final int STORE_ITEMS_PAGINATED_CONTENT_TYPE_ID = 1;
    private static final int MOUNTS_PAGINATED_CONTENT_TYPE_ID = 2;
    private static final int STORE_MOUNTS_PAGINATED_CONTENT_TYPE_ID = 3;
    private static final int OUTFITS_PAGINATED_CONTENT_TYPE_ID = 4;
    private static final int STORE_OUTFITS_PAGINATED_CONTENT_TYPE_ID = 5;

    private final int SECONDS_TO_WAIT_BETWEEN_PAGINATED_CONTENT_REQUESTS;

    @Autowired
    public AuctionFetchServiceImpl(
            @Value("${tibia-auction-history.seconds-to-wait-between-paginated-content-requests}") int SECONDS_TO_WAIT_BETWEEN_PAGINATED_CONTENT_REQUESTS
    ) {
        this.SECONDS_TO_WAIT_BETWEEN_PAGINATED_CONTENT_REQUESTS = SECONDS_TO_WAIT_BETWEEN_PAGINATED_CONTENT_REQUESTS;
    }

    @Override
    public Auction fetchAuction(int auctionId) throws IOException {
        final String auctionUrl = AUCTION_URL_TEMPLATE.formatted(auctionId);
        final SSLSocketFactory sslSocketFactory = CustomSSLContext.createCustomSSLSocketFactory();
        final Connection connection = Jsoup.connect(auctionUrl).headers(generateHeaders()).sslSocketFactory(sslSocketFactory);
        final Document document = connection.get();
        final String sessionId = connection.response().cookie("DM_SessionID");

        final Elements internalErrorElements = document.select(":containsOwn(An internal error has occurred)");
        if (!internalErrorElements.isEmpty()){
            return Auction.builder().id(auctionId).inErrorState(true).build();
        }

        final Elements auctionHeaderElements = document.select(".AuctionHeader");
        final Matcher auctionHeaderMatcher = AUCTION_HEADER_PATTERN.matcher(auctionHeaderElements.text());
        Preconditions.checkState(
                auctionHeaderMatcher.matches(),
                "For the auction '%s' the header '%s' does not match the expected pattern.",
                auctionId,
                auctionHeaderElements.text()
        );
        final int level = Integer.parseInt(auctionHeaderMatcher.group(1));
        final Vocation vocation = Vocation.getByName(auctionHeaderMatcher.group(2));
        final String world = auctionHeaderMatcher.group(4);
        final String name = auctionHeaderElements.select(".AuctionCharacterName").text();

        final String auctionStartText = getShortAuctionDataValue(document, "Auction Start:");
        final LocalDateTime auctionStartTime = DateUtils.parseZonedDateTimeToUTC(
                auctionStartText,
                AUCTION_START_DATE_END_DATE_TIME_FORMATTER
        );
        final String auctionEndText = getShortAuctionDataValue(document, "Auction End:");
        final LocalDateTime auctionEndTime = DateUtils.parseZonedDateTimeToUTC(
                auctionEndText,
                AUCTION_START_DATE_END_DATE_TIME_FORMATTER
        );

        final Elements winningBidElements = getShortAuctionDataValueElements(document, "Winning Bid:");
        final Elements minimumBidElements = getShortAuctionDataValueElements(document, "Minimum Bid:");
        final String auctionInfoText = document.select(".AuctionInfo").text();
        boolean sold;
        Integer minimumBid = null;
        Integer winningBid = null;
        if (winningBidElements.size() > 0 && auctionInfoText.equalsIgnoreCase("finished")){
            sold = true;
            winningBid = NumberUtils.parseIntegerCommaSeparatedThousands(winningBidElements.get(0).nextElementSibling().text());
        } else {
            sold = false;
            minimumBid = NumberUtils.parseIntegerCommaSeparatedThousands(
                    (minimumBidElements.isEmpty() ? winningBidElements : minimumBidElements).get(0).nextElementSibling().text()
            );
        }

        final int mountsAmount = getGeneralTableRowValueAsInteger(document, "Mounts:");
        final int outfitsAmount = getGeneralTableRowValueAsInteger(document, "Outfits:");
        final int titlesAmount = getGeneralTableRowValueAsInteger(document, "Titles:");
        final int achievementPointsAmount = getGeneralTableRowValueAsInteger(document, "Achievement Points:");

        final Pair<Integer, BigDecimal> axeFightingLevelAndPercentage =
                getSkillLevelAndPercentage(document, "Axe Fighting");
        final Pair<Integer, BigDecimal> clubFightingLevelAndPercentage =
                getSkillLevelAndPercentage(document, "Club Fighting");
        final Pair<Integer, BigDecimal> distanceFightingLevelAndPercentage =
                getSkillLevelAndPercentage(document, "Distance Fighting");
        final Pair<Integer, BigDecimal> fishingLevelAndPercentage =
                getSkillLevelAndPercentage(document, "Fishing");
        final Pair<Integer, BigDecimal> fistFightingLevelAndPercentage =
                getSkillLevelAndPercentage(document, "Fist Fighting");
        final Pair<Integer, BigDecimal> magicLevelLevelAndPercentage =
                getSkillLevelAndPercentage(document, "Magic Level");
        final Pair<Integer, BigDecimal> shieldingLevelAndPercentage =
                getSkillLevelAndPercentage(document, "Shielding");
        final Pair<Integer, BigDecimal> swordFightingLevelAndPercentage =
                getSkillLevelAndPercentage(document, "Sword Fighting");

        final String characterCreationDateText = getGeneralTableRowValueAsString(document, "Creation Date:");
        final LocalDateTime characterCreationDate = DateUtils.parseZonedDateTimeToUTC(
                characterCreationDateText,
                CHARACTER_CREATION_DATE_TIME_FORMATTER
        );

        final boolean hasCharmExpansion = "yes".equals(getGeneralTableRowValueAsString(document, "Charm Expansion:"));
        final int availableCharmPoints = getGeneralTableRowValueAsInteger(document, "Available Charm Points:");
        final int spentCharmPoints = getGeneralTableRowValueAsInteger(document, "Spent Charm Points:");

        final int dailyRewardStreak = getGeneralTableRowValueAsInteger(document, "Daily Reward Streak:");

        final int huntingTaskPoints = getGeneralTableRowValueAsInteger(document, "Hunting Task Points:");
        final int permanentHuntingTasksSlots = getGeneralTableRowValueAsInteger(document, "Permanent Hunting Task Slots:");
        final int permanentPreySlots = getGeneralTableRowValueAsInteger(document, "Permanent Prey Slots:");
        final int preyWildcards = getGeneralTableRowValueAsInteger(document, "Prey Wildcards:");

        final int hirelings = getGeneralTableRowValueAsInteger(document, "Hirelings:");
        final int hirelingJobs = getGeneralTableRowValueAsInteger(document, "Hireling Jobs:");
        final int hirelingOutfits = getGeneralTableRowValueAsInteger(document, "Hireling Outfits:");

        final Pair<Integer, Integer> exaltedDustCompletedAndMaximum = getExaltedDustCompletedAndMaximum(document);

        final int bossPoints = getGeneralTableRowValueAsInteger(document, "Boss Points:");

        final List<Item> items = getPaginatedContentItems(ITEM_PARSER, document, auctionId, ITEMS_PAGINATED_CONTENT_TYPE_ID, sessionId);
        final List<Item> storeItems = getPaginatedContentItems(ITEM_PARSER, document, auctionId, STORE_ITEMS_PAGINATED_CONTENT_TYPE_ID, sessionId);
        final List<String> mounts = getPaginatedContentItems(MOUNT_PARSER, document, auctionId, MOUNTS_PAGINATED_CONTENT_TYPE_ID, sessionId);
        final List<String> storeMounts = getPaginatedContentItems(MOUNT_PARSER, document, auctionId, STORE_MOUNTS_PAGINATED_CONTENT_TYPE_ID, sessionId);
        final List<Outfit> outfits = getPaginatedContentItems(OUTFIT_PARSER, document, auctionId, OUTFITS_PAGINATED_CONTENT_TYPE_ID, sessionId);
        final List<Outfit> storeOutfits = getPaginatedContentItems(OUTFIT_PARSER, document, auctionId, STORE_OUTFITS_PAGINATED_CONTENT_TYPE_ID, sessionId);

        final List<String> charms = parseCharms(document);

        final List<String> imbuements = getTableLinesOrEmptyIfNoContent(
                document, "Imbuements", "No imbuements."
        );
        final List<String> completedCyclopediaMapAreas = getTableLinesOrEmptyIfNoContent(
                document, "CompletedCyclopediaMapAreas", "No areas explored."
        );
        final List<String> completedQuestLines = getTableLinesOrEmptyIfNoContent(
                document, "CompletedQuestLines", "No quest line finished."
        );
        final List<String> titles = getTableLinesOrEmptyIfNoContent(
                document, "Titles", "No character titles."
        );
        final List<String> achievements = getTableLinesOrEmptyIfNoContent(
                document, "Achievements", "No achievements."
        );

        return Auction
                .builder()
                .id(auctionId)
                .name(name)
                .level(level)
                .vocation(vocation)
                .world(world)
                .creationDate(characterCreationDate)
                .auctionStart(auctionStartTime)
                .auctionEnd(auctionEndTime)
                .sold(sold)
                .minimumBid(minimumBid)
                .winningBid(winningBid)
                .mountsAmount(mountsAmount)
                .outfitsAmount(outfitsAmount)
                .titlesAmount(titlesAmount)
                .achievementPoints(achievementPointsAmount)
                .dailyRewardStreak(dailyRewardStreak)
                .axeFighting(axeFightingLevelAndPercentage.getKey())
                .axeFightingPercentage(axeFightingLevelAndPercentage.getValue())
                .clubFighting(clubFightingLevelAndPercentage.getKey())
                .clubFightingPercentage(clubFightingLevelAndPercentage.getValue())
                .distanceFighting(distanceFightingLevelAndPercentage.getKey())
                .distanceFightingPercentage(distanceFightingLevelAndPercentage.getValue())
                .fishing(fishingLevelAndPercentage.getKey())
                .fishingPercentage(fishingLevelAndPercentage.getValue())
                .fistFighting(fistFightingLevelAndPercentage.getKey())
                .fistFightingPercentage(fistFightingLevelAndPercentage.getValue())
                .magicLevel(magicLevelLevelAndPercentage.getKey())
                .magicLevelPercentage(magicLevelLevelAndPercentage.getValue())
                .shielding(shieldingLevelAndPercentage.getKey())
                .shieldingPercentage(shieldingLevelAndPercentage.getValue())
                .swordFighting(swordFightingLevelAndPercentage.getKey())
                .swordFightingPercentage(swordFightingLevelAndPercentage.getValue())
                .hasCharmExpansion(hasCharmExpansion)
                .availableCharmPoints(availableCharmPoints)
                .spentCharmPoints(spentCharmPoints)
                .charms(charms)
                .huntingTaskPoints(huntingTaskPoints)
                .permanentHuntingTaskSlots(permanentHuntingTasksSlots)
                .permanentPreySlots(permanentPreySlots)
                .preyWildcards(preyWildcards)
                .hirelings(hirelings)
                .hirelingsJobs(hirelingJobs)
                .hirelingsOutfits(hirelingOutfits)
                .maximumExaltedDust(exaltedDustCompletedAndMaximum.getValue())
                .completedExaltedDust(exaltedDustCompletedAndMaximum.getKey())
                .bossPoints(bossPoints)
                .items(items)
                .storeItems(storeItems)
                .mounts(mounts)
                .storeMounts(storeMounts)
                .outfits(outfits)
                .storeOutfits(storeOutfits)
                .imbuements(imbuements)
                .completedCyclopediaMapAreas(completedCyclopediaMapAreas)
                .completedQuestLines(completedQuestLines)
                .titles(titles)
                .achievements(achievements)
                .build();
    }

    private Pair<Integer, BigDecimal> getSkillLevelAndPercentage(Document document, String skillName){
        final Element skillRow = document.select("b:containsOwn(%s)".formatted(skillName)).get(0).parent().parent();
        Preconditions.checkState(skillRow.is("tr"));
        final int skill = Integer.parseInt(skillRow.select(".LevelColumn").text());
        final BigDecimal skillPercentage = new BigDecimal(
                skillRow.select(".PercentageStringContainer").text().replaceAll("%|\s", "")
        );
        return Pair.of(skill, skillPercentage);
    }

    private Pair<Integer, Integer> getExaltedDustCompletedAndMaximum(Document document){
        final String[] exaustedDustCompletedAndMaximum = document.select("span:containsOwn(Exalted Dust:)")
                .get(0).nextElementSibling().text().replaceAll(",", "").split("/");
        final int completedExaustedDust = Integer.parseInt(exaustedDustCompletedAndMaximum[0]);
        final int maximumExaustedDust = Integer.parseInt(exaustedDustCompletedAndMaximum[1]);
        return Pair.of(completedExaustedDust, maximumExaustedDust);
    }

    private List<String> getTableLinesOrEmptyIfNoContent(Document document, String tableId, String noContentText){
        final List<String> tableLines = getTableLines(document, tableId);
        return tableLines.contains(noContentText) ?
                Collections.emptyList() :
                tableLines;
    }

    private List<String> parseCharms(Document document){
        return document
                .select("#Charms .TableContent")
                .select("tr .Odd , tr .Even, tr:not(.IndicateMoreEntries):not(.LabelH)")
                .select("td:nth-of-type(2)")
                .stream()
                .map(Element::text)
                .toList();
    }

    private List<String> getTableLines(Document document, String tableId){
        return document
                .select("#" + tableId)
                .select(".TableContent")
                .select("tr:not(.IndicateMoreEntries):not(.LabelH).Odd , tr:not(.IndicateMoreEntries):not(.LabelH).Even")
                .select("td")
                .stream()
                .map(Element::text)
                .toList();
    }

    private String getGeneralTableRowValueAsString(Document document, String rowLabel){
        return document.select("span:containsOwn(%s)".formatted(rowLabel)).get(0).nextElementSibling().text();
    }

    private Integer getGeneralTableRowValueAsInteger(Document document, String rowLabel){
        return NumberUtils.parseIntegerCommaSeparatedThousands(getGeneralTableRowValueAsString(document, rowLabel));
    }

    private Elements getShortAuctionDataValueElements(Document document, String label){
        return document
                .select(".AuctionBody")
                .select(".AuctionBodyBlock.ShortAuctionData")
                .select(".ShortAuctionDataLabel:contains(%s)".formatted(label));
    }

    private String getShortAuctionDataValue(Document document, String label){
        return getShortAuctionDataValueElements(document, label)
                .get(0)
                .nextElementSibling()
                .text();
    }

    private <T> List<T> getPaginatedContentItems(Function<Element, T> elementMappingFunction,
                                                 Document document,
                                                 int auctionId,
                                                 int type,
                                                 String sessionId){
        final Elements firstPageElements = document.select("#ajax-target-type-" + type);
        if (firstPageElements.isEmpty()){
            return Collections.emptyList();
        }

        final List<T> firstPageContent = firstPageElements
                .select(".CVIcon")
                .stream()
                .map(elementMappingFunction)
                .toList();

        final List<Document> pagesDocuments = getPaginatedContentDocuments(document, auctionId, type, sessionId);
        final List<T> otherPagesContent = pagesDocuments
                .stream()
                .map(pageDocument -> pageDocument
                        .select(".CVIcon")
                        .stream()
                        .map(elementMappingFunction)
                        .toList()
                )
                .flatMap(List::stream)
                .toList();

        return Stream.of(firstPageContent, otherPagesContent).flatMap(List::stream).toList();
    }

    final Function<Element, Item> ITEM_PARSER = element -> {
        final String title = element.attr("title");
        final Matcher matcher = ITEM_DESCRIPTION_PATTERN.matcher(title);

        if (matcher.find()){
            return new Item(
                    matcher.group(2) != null ?
                            NumberUtils.parseIntegerCommaSeparatedThousands(matcher.group(2)) :
                            1,
                    matcher.group(4)
            );
        } else {
            log.warn(
                    "Item title '{}' didn't matched the pattern {} assuming whole title as item name and 1 as amount.",
                    title,
                    ITEM_DESCRIPTION_PATTERN
            );
            return new Item(1, title);
        }
    };

    final Function<Element, String> MOUNT_PARSER = element -> element.attr("title");

    final Function<Element, Outfit> OUTFIT_PARSER = element -> {
        final String title = element.attr("title");
        return new Outfit(
                StringUtils.substringBeforeLast(title, " ("),
                title.contains("addon 1"),
                title.contains("addon 2")
        );
    };

    private int findAmountOfPagesOfPaginatedContent(Document document, int auctionId, int type){
        return document.select("a[href~=auctionid=%s&type=%s&currentpage=[0-9]+]".formatted(auctionId, type))
                .stream()
                .map(element -> element.attr("href"))
                .map(link -> {
                    final Pattern pattern = Pattern.compile(".*currentpage=([0-9]+).*");
                    final Matcher matcher = pattern.matcher(link);
                    matcher.find();
                    return Integer.parseInt(matcher.group(1));
                }).max(Integer::compare).orElse(1);
    }

    private Document getPaginatedContentDocument(int auctionId, int type, int page, String sessionId) throws IOException, InterruptedException, URISyntaxException {
        final SSLSocketFactory sslSocketFactory = CustomSSLContext.createCustomSSLSocketFactory();
        final String paginatedContentJson = Jsoup.connect(PAGINATED_CONTENT_URL_TEMPLATE.formatted(auctionId, type, page))
                .headers(generateHeaders())
                .cookie("DM_SessionID", sessionId)
                .sslSocketFactory(sslSocketFactory)
                .execute()
                .body();
        final ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final PaginatedContentResponse paginatedContentResponse = objectMapper.readValue(
                paginatedContentJson,
                PaginatedContentResponse.class
        );

        return Jsoup.parse(paginatedContentResponse.ajaxObjects().get(0).data());
    }

    private List<Document> getPaginatedContentDocuments(Document document, int auctionId, int type, String sessionId){
        final int amountOfPages = findAmountOfPagesOfPaginatedContent(
                document,
                auctionId,
                type
        );

        return IntStream
                .range(2, amountOfPages + 1)
                .mapToObj(page -> {
                    try {
                        Thread.sleep(SECONDS_TO_WAIT_BETWEEN_PAGINATED_CONTENT_REQUESTS * 1000L);
                        return getPaginatedContentDocument(auctionId, type, page, sessionId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    private Map<String, String> generateHeaders(){
        final String ipAddress = InetAddresses.fromInteger(new Random().nextInt()).getHostAddress();
        final HashMap<String, String> headers = new HashMap<>();

        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Originating-IP", ipAddress);
        headers.put("X-Forwarded-For", ipAddress);
        headers.put("X-Remote-IP", ipAddress);
        headers.put("X-Remote-Addr", ipAddress);
        headers.put("X-Client-IP", ipAddress);
        headers.put("X-Host", ipAddress);
        headers.put("X-Forwared-Host", ipAddress);
        headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8");
        headers.put("accept-language", "en-US,en;q=0.7");
        headers.put("Accept-Encoding", "identity");
        headers.put("priority", "u=0, i");
        headers.put("referer", "https://www.tibia.com/charactertrade/?subtopic=pastcharactertrades");
        headers.put("sec-ch-ua", "\"Brave\";v=\"125\", \"Chromium\";v=\"125\", \"Not.A/Brand\";v=\"24\"");
        headers.put("sec-ch-ua-mobile", "?0");
        headers.put("sec-ch-ua-platform", "\"Linux\"");
        headers.put("sec-fetch-dest", "document");
        headers.put("sec-fetch-mode", "navigate");
        headers.put("sec-fetch-site", "same-origin");
        headers.put("sec-fetch-user", "?1");
        headers.put("sec-gpc", "1");
        headers.put("upgrade-insecure-requests", "1");
        headers.put("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");

        return headers;
    }

    private static class CustomSSLContext {
        public static SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
            // Create SSLContext instance
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // Initialize SSLContext with default key manager and trust manager
            sslContext.init(null, null, new java.security.SecureRandom());

            return sslContext;
        }

        public static SSLSocketFactory createCustomSSLSocketFactory() {
            try {
                SSLContext sslContext = createSSLContext();
                return new CustomSSLSocketFactory(sslContext.getSocketFactory());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static class CustomSSLSocketFactory extends SSLSocketFactory {
            private final SSLSocketFactory delegate;

            public CustomSSLSocketFactory(SSLSocketFactory delegate) {
                this.delegate = delegate;
            }

            @Override
            public String[] getDefaultCipherSuites() {
                return new String[] {
                        SUPPORTED_CIPHER_SUITES[new Random().nextInt(SUPPORTED_CIPHER_SUITES.length)]
                };
            }

            @Override
            public String[] getSupportedCipherSuites() {
                return delegate.getSupportedCipherSuites();
            }

            @Override
            public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
                SSLSocket socket = (SSLSocket) delegate.createSocket(s, host, port, autoClose);
                socket.setEnabledCipherSuites(getDefaultCipherSuites());
                return socket;
            }

            @Override
            public Socket createSocket(String host, int port) throws IOException {
                SSLSocket socket = (SSLSocket) delegate.createSocket(host, port);
                socket.setEnabledCipherSuites(getDefaultCipherSuites());
                return socket;
            }

            @Override
            public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
                SSLSocket socket = (SSLSocket) delegate.createSocket(host, port, localHost, localPort);
                socket.setEnabledCipherSuites(getDefaultCipherSuites());
                return socket;
            }

            @Override
            public Socket createSocket(InetAddress host, int port) throws IOException {
                SSLSocket socket = (SSLSocket) delegate.createSocket(host, port);
                socket.setEnabledCipherSuites(getDefaultCipherSuites());
                return socket;
            }

            @Override
            public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
                SSLSocket socket = (SSLSocket) delegate.createSocket(address, port, localAddress, localPort);
                socket.setEnabledCipherSuites(getDefaultCipherSuites());
                return socket;
            }
        }
    }
}
