package com.matheusmr.tibiaauctionhistory.auctionsearch.service;

import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import com.matheusmr.tibiaauctionhistory.common.model.SortableField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuctionSearchCacheServiceRedisImpl implements AuctionSearchCacheService {

    private static final long AUCTIONS_AND_AUCTION_SEARCH_CACHE_EXPIRATION = 5;
    private static final TimeUnit CACHE_EXPIRATION_UNIT = TimeUnit.MINUTES;

    private final RedisTemplate<String, Auction> auctionRedisTemplate;

    @Autowired
    public AuctionSearchCacheServiceRedisImpl(
            RedisTemplate<String, Auction> auctionRedisTemplate
    ) {
        this.auctionRedisTemplate = auctionRedisTemplate;
    }

    @Override
    public List<Auction> getAuctions(UUID searchId, int limit, int offset, SortableField sortBy, Sort.Direction orderBy) {
        final String key = buildKey(searchId, sortBy, orderBy);
        return getAuctions(key, limit, offset);
    }

    @Override
    public void cacheAuctions(UUID searchId, List<Auction> auctions, SortableField sortBy, Sort.Direction orderBy) {
        final String key = buildKey(searchId, sortBy, orderBy);
        cacheAuctions(key, auctions);
    }

    private List<Auction> getAuctions(String key, int limit, int offset){
        log.debug("Finding if key {} is in cache...", key);
        final boolean hasKey = Boolean.TRUE.equals(auctionRedisTemplate.hasKey(key));
        log.debug("{} key {} in cache", hasKey ? "Found" : "Not found", key);

        if (hasKey){
            log.debug("Fetching auctions from cache with key {} offset {} and limit {}...", key, offset, limit);
            final List<Auction> auctions = auctionRedisTemplate.opsForList().range(key, offset, offset + limit - 1);
            log.debug("Returning {} auctions", auctions.size());
            return auctions;
        }
        return null;
    }

    private void cacheAuctions(String key, List<Auction> auctions){
        log.debug("Saving {} auctions in cache with key {}...", auctions.size(), key);
        auctionRedisTemplate.opsForList().rightPushAll(key, auctions);
        log.debug("Saved {} auctions in cache with key {}, setting expiry time", auctions.size(), key);
        auctionRedisTemplate.expire(key, AUCTIONS_AND_AUCTION_SEARCH_CACHE_EXPIRATION, CACHE_EXPIRATION_UNIT);
        log.debug("Expiry time set for {} auctions with key {}", auctions.size(), key);
    }

    private String buildKey(UUID searchId, SortableField sortBy, Sort.Direction orderBy){
        return "filteredAuctions/searchId{%s}/sortBy{%s}/orderBy{%s}".formatted(searchId, sortBy, orderBy);
    }
}
