package com.tibiaauctionhistory.service.auctionfetch.service;

import com.tibiaauctionhistory.module.common.model.Auction;
import com.tibiaauctionhistory.module.common.repository.AuctionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class NewAuctionsFetcherServiceImpl implements NewAuctionsFetcherService, CommandLineRunner {

    private static final String URL_FINISHED_AUCTIONS_BY_START_DATE_LATEST = "https://www.tibia.com/charactertrade/?subtopic=pastcharactertrades&order_column=103&order_direction=0";

    private final AuctionRepository auctionRepository;
    private final AuctionFetchService auctionFetchService;
    private final AuctionFetchValidationService auctionFetchValidationService;
    private final int SECONDS_TO_WAIT_BETWEEN_AUCTION_FETCH;
    private final int SECONDS_TO_WAIT_AFTER_RATE_LIMIT;

    private Integer AUCTION_ID_TO_START;
    private Integer AUCTION_ID_TO_END;

    @Autowired
    public NewAuctionsFetcherServiceImpl(
            AuctionRepository auctionRepository,
            AuctionFetchService auctionFetchService,
            AuctionFetchValidationService auctionFetchValidationService,
            @Value("${tibia-auction-history.seconds-to-wait-between-auction-fetch:0}") int secondsToWaitBetweenAuctionFetch,
            @Value("${tibia-auction-history.seconds-to-wait-after-rate-limit:61}") int secondsToWaitAfterRateLimit
    ) {
        this.auctionRepository = auctionRepository;
        this.auctionFetchService = auctionFetchService;
        this.auctionFetchValidationService = auctionFetchValidationService;
        this.SECONDS_TO_WAIT_BETWEEN_AUCTION_FETCH = secondsToWaitBetweenAuctionFetch;
        this.SECONDS_TO_WAIT_AFTER_RATE_LIMIT = secondsToWaitAfterRateLimit;
    }

    @Override
    public void run(String... args) throws Exception {
        parseArgs(args);
//        auctionFetchValidationService.validateAuctionFetchingCorrectness();
        fetchNewAuctions();
    }

    @Override
    public void fetchNewAuctions() {
        log.info("Starting to fetch new auctions");
        final List<Integer> auctionsIdsToFetch = getAuctionsIdsToFetch();
        final int auctionsIdsToFetchSize = auctionsIdsToFetch.size();

        for (int i = 0; i < auctionsIdsToFetchSize; i++) {
            final Integer auctionId = auctionsIdsToFetch.get(i);
            try {
                Thread.sleep(SECONDS_TO_WAIT_BETWEEN_AUCTION_FETCH * 1000L);
                log.info("Fetching auction {} of {}. Auction id: {}", i, auctionsIdsToFetchSize, auctionId);
                final Auction auction = auctionFetchService.fetchAuction(auctionId);
                final long start = System.currentTimeMillis();
                auctionRepository.save(auction);
                final long end = System.currentTimeMillis();
                log.info("Successfully fetched auction of id {}, saved in {} ms", auctionId, end - start);
            } catch (Exception e) {
                if (e instanceof HttpStatusException statusException && statusException.getStatusCode() == 403){
                    log.info(
                            "Blocked by rate limiter while fetching the auction {}, waiting {} seconds to continue",
                            auctionId,
                            SECONDS_TO_WAIT_AFTER_RATE_LIMIT
                    );
                    try {
                        Thread.sleep(SECONDS_TO_WAIT_AFTER_RATE_LIMIT * 1000L);
                    } catch (InterruptedException ex) {
                        log.error("Thread interrupted while waiting for rate limit to expire, proceeding to next auction");
                    }
                } else {
                    log.error("Unexpected error while fetching the auction of id {}, proceeding to next auction", auctionId, e);
                }
            }
        }
        log.info("Finished fetching new auctions");
        System.exit(0);
    }

    private List<Integer> getAuctionsIdsToFetch(){
        try {
            log.info("Finding auctions ids to fetch");

            log.info("Finding latest finished auction id...");
            final Document document = Jsoup.connect(URL_FINISHED_AUCTIONS_BY_START_DATE_LATEST).get();
            final String latestFinishedAuctionUrl = document.select("a[href*=auctionid]").get(0).attr("href");
            final Matcher matcher = Pattern.compile(".*auctionid=([0-9]*).*").matcher(latestFinishedAuctionUrl);
            matcher.find();
            final int latestFinishedAuctionId = Integer.parseInt(matcher.group(1));
            log.info("Found {} as the latest finished auction id", latestFinishedAuctionId);

            final Set<Integer> allAuctionsIds = IntStream.range(1, latestFinishedAuctionId).boxed().collect(Collectors.toSet());
            final Set<Integer> processedAuctionsIds = auctionRepository.findAllIds();

            allAuctionsIds.removeAll(processedAuctionsIds);

            final List<Integer> filteredAuctionsIds = allAuctionsIds
                    .stream()
                    .filter(id -> AUCTION_ID_TO_START == null || id >= AUCTION_ID_TO_START)
                    .filter(id -> AUCTION_ID_TO_END == null || id <= AUCTION_ID_TO_END)
                    .toList();

            log.info("{} auctions already fetched, {} to fetch in this run", processedAuctionsIds.size(), filteredAuctionsIds.size());

            return filteredAuctionsIds;
        } catch (Exception e){
            throw new RuntimeException("Error while getting the auctions ids to fetch", e);
        }
    }

    private void parseArgs(String... args){
        final Optional<String> auctionIdToStartOpt = getArgValue("AUCTION_ID_TO_START", args);
        final Optional<String> auctionIdToEndOpt = getArgValue("AUCTION_ID_TO_END", args);
        this.AUCTION_ID_TO_START = auctionIdToStartOpt
                .map(Integer::parseInt)
                .orElse(null);
        this.AUCTION_ID_TO_END = auctionIdToEndOpt
                .map(Integer::parseInt)
                .orElse(null);
        auctionIdToStartOpt
                .ifPresentOrElse(
                        id -> log.info("Auctions will be fetched starting from id {}", id),
                        () -> log.info("A starting id was not specified, so auctions will be fetched starting from id 1")
                );
        auctionIdToEndOpt
                .ifPresentOrElse(
                        id -> log.info("Auctions will be fetched until id {}", id),
                        () -> log.info("A end id was not specified, so auctions will be fetched until the latest id found")
                );
    }

    private Optional<String> getArgValue(String argName, String... args){
        return Arrays.stream(args)
                .map(arg -> StringUtils.substringAfter(arg, argName + "="))
                .filter(StringUtils::isNotEmpty)
                .findFirst();
    }
}
