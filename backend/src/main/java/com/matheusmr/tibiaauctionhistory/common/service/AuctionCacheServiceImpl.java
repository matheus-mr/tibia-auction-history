package com.matheusmr.tibiaauctionhistory.common.service;

import com.matheusmr.tibiaauctionhistory.auctionsearch.model.SortableField;
import com.matheusmr.tibiaauctionhistory.common.model.Auction;
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
public class AuctionCacheServiceImpl implements AuctionCacheService {

    private static final long CACHE_EXPIRATION = 5;
    private static final TimeUnit CACHE_EXPIRATION_UNIT = TimeUnit.MINUTES;

    private final RedisTemplate<String, Auction> redisTemplate;

    @Autowired
    public AuctionCacheServiceImpl(RedisTemplate<String, Auction> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Auction> getAuctions(int limit, int offset, SortableField sortBy, Sort.Direction orderBy) {
        final String key = buildKey(sortBy, orderBy);
        return getAuctions(key, limit, offset);
    }

    @Override
    public void cacheAuctions(List<Auction> auctions, SortableField sortBy, Sort.Direction orderBy) {
        final String key = buildKey(sortBy, orderBy);
        cacheAuctions(key, auctions);
    }

    @Override
    public Long countAuctions(SortableField sortBy, Sort.Direction orderBy) {
        final String key = buildKey(sortBy, orderBy);
        return countAuctions(key);
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

    @Override
    public Long countAuctions(UUID searchId, SortableField sortBy, Sort.Direction orderBy) {
        final String key = buildKey(searchId, sortBy, orderBy);
        return countAuctions(key);
    }

    private List<Auction> getAuctions(String key, int limit, int offset){
        log.debug("Finding if key {} is in cache...", key);
        final boolean hasKey = Boolean.TRUE.equals(redisTemplate.hasKey(key));
        log.debug("{} key {} in cache", hasKey ? "Found" : "Not found", key);

        if (hasKey){
            log.debug("Fetching auctions from cache with key {} offset {} and limit {}...", key, offset, limit);
            final List<Auction> auctions = redisTemplate.opsForList().range(key, offset, offset + limit - 1);
            log.debug("Returning {} auctions", auctions.size());
            return auctions;
        }
        return null;
    }

    private void cacheAuctions(String key, List<Auction> auctions){
        log.debug("Saving {} auctions in cache with key {}...", auctions.size(), key);
        redisTemplate.opsForList().rightPushAll(key, auctions);
        log.debug("Saved {} auctions in cache with key {}, setting expiry time", auctions.size(), key);
        redisTemplate.expire(key, CACHE_EXPIRATION, CACHE_EXPIRATION_UNIT);
        log.debug("Expiry time set for {} auctions with key {}", auctions.size(), key);
    }

    private Long countAuctions(String key){
        log.debug("Counting auctions in cache for key {}...", key);
        final Long size = redisTemplate.opsForList().size(key);
        log.debug("Found {} auctions in cache for key {}", size, key);
        return size;
    }

    private String buildKey(SortableField sortBy, Sort.Direction orderBy){
        return "auctions/sortBy{%s}/orderBy{%s}".formatted(sortBy, orderBy);
    }

    private String buildKey(UUID searchId, SortableField sortBy, Sort.Direction orderBy){
        return "filteredAuctions/searchId{%s}/sortBy{%s}/orderBy{%s}".formatted(searchId, sortBy, orderBy);
    }
}
