package com.matheusmr.tibiaauctionhistory.auctionfetch.service;

import com.matheusmr.tibiaauctionhistory.common.entity.Auction;
import com.matheusmr.tibiaauctionhistory.common.repository.AuctionRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class NewAuctionsFetcherServiceImpl implements NewAuctionsFetcherService {

    private static final String URL_FINISHED_AUCTIONS_BY_START_DATE_LATEST = "https://www.tibia.com/charactertrade/?subtopic=pastcharactertrades&order_column=103&order_direction=0";

    private final AuctionRepository auctionRepository;
    private final AuctionFetchService auctionFetchService;

    @Autowired
    public NewAuctionsFetcherServiceImpl(AuctionRepository auctionRepository,
                                         AuctionFetchService auctionFetchService) {
        this.auctionRepository = auctionRepository;
        this.auctionFetchService = auctionFetchService;
    }

    @Override
    public void fetchNewAuctions() {
        log.info("Starting to fetch new auctions");
        final List<Integer> auctionsIdsToFetch = getAuctionsIdsToFetch();
        final int auctionsIdsToFetchSize = auctionsIdsToFetch.size();

        for (int i = 0; i < auctionsIdsToFetchSize; i++) {
            final Integer auctionId = auctionsIdsToFetch.get(i);
            try {
                Thread.sleep(5 * 1000);
                log.info("Fetching auction {} of {}. Auction id: {}", i, auctionsIdsToFetchSize, auctionId);
                auctionFetchService.fetchAuction(auctionId);
                log.info("Successfully fetched auction of id {}", auctionId);
            } catch (Exception e) {
                if (e instanceof HttpStatusException && ((HttpStatusException) e).getStatusCode() == HttpStatus.FORBIDDEN.value()){
                    final int secondsToWaitForRateLimiter = 60;
                    log.info("Blocked by rate limiter while fetching the auction {}, waiting {} seconds to continue", auctionId, secondsToWaitForRateLimiter);
                    try {
                        Thread.sleep(secondsToWaitForRateLimiter * 1000);
                    } catch (InterruptedException ex) {
                        log.error("Thread interrupted while waiting for rate limit to expire, proceeding to next auction");
                    }
                } else {
                    log.error("Unexpected error while fetching the auction of id {}, proceeding to next auction", auctionId, e);
                }
            }
        }
        log.info("Finished fetching new auctions");
    }

    private List<Integer> getAuctionsIdsToFetch(){
        try {
            log.info("Finding auctions ids to fetch");
            final Document document = Jsoup.connect(URL_FINISHED_AUCTIONS_BY_START_DATE_LATEST).get();
            final String latestFinishedAuctionUrl = document.select("a[href*=auctionid]").get(0).attr("href");

            final Matcher matcher = Pattern.compile(".*auctionid=([0-9]*).*").matcher(latestFinishedAuctionUrl);
            matcher.find();
            final int latestFinishedAuctionId = Integer.parseInt(matcher.group(1));
            log.info("Found {} as the latest finished auction id", latestFinishedAuctionId);

            final Set<Integer> allAuctionsIds = IntStream.range(1, latestFinishedAuctionId).boxed().collect(Collectors.toSet());
            final Set<Integer> processedAuctionsIds = auctionRepository.findAll().stream().map(Auction::getId).collect(Collectors.toSet());
            allAuctionsIds.removeAll(processedAuctionsIds);
            log.info("{} auction already fetched, {} to fetch", processedAuctionsIds.size(), allAuctionsIds.size());

            return new ArrayList<>(allAuctionsIds);
        } catch (Exception e){
            throw new RuntimeException("Error while getting the auctions ids to fetch", e);
        }
    }
}
