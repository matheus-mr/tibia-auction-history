package com.tibiaauctionhistory.service.auctionsearch.service;

import com.tibiaauctionhistory.module.common.model.SortableField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuctionSearchCacheServiceRedisImpl implements AuctionSearchCacheService {

    private static final long CACHE_EXPIRATION_TIME = 5;
    private static final TimeUnit CACHE_EXPIRATION_TIME_UNIT = TimeUnit.MINUTES;

    private final RedisTemplate<String, Integer> auctionRedisTemplate;

    @Autowired
    public AuctionSearchCacheServiceRedisImpl(
            RedisTemplate<String, Integer> auctionRedisTemplate
    ) {
        this.auctionRedisTemplate = auctionRedisTemplate;
    }

    @Override
    public List<Integer> getAuctionsIds(String searchId, int limit, int offset, SortableField sortBy, Sort.Direction orderBy) {
        final String key = buildKey(searchId, sortBy, orderBy);
        return getAuctionsIds(key, limit, offset);
    }

    @Override
    public void cacheAuctionsIds(String searchId, List<Integer> auctionsIds, SortableField sortBy, Sort.Direction orderBy) {
        final String key = buildKey(searchId, sortBy, orderBy);
        cacheAuctionsIds(key, auctionsIds);
    }

    private List<Integer> getAuctionsIds(String key, int limit, int offset){
        log.debug("Finding if key {} is in cache...", key);
        final boolean hasKey = Boolean.TRUE.equals(auctionRedisTemplate.hasKey(key));
        log.debug("{} key {} in cache", hasKey ? "Found" : "Not found", key);

        if (hasKey){
            log.debug("Fetching auctions ids from cache with key {} offset {} and limit {}...", key, offset, limit);
            final List<Integer> auctionsIds = auctionRedisTemplate.opsForList().range(key, offset, offset + limit - 1);
            log.debug("Returning {} auctions ids", auctionsIds.size());
            return auctionsIds;
        }
        return null;
    }

    private void cacheAuctionsIds(String key, List<Integer> auctions){
        log.debug("Saving {} auctions ids in cache with key {}...", auctions.size(), key);
        auctionRedisTemplate.opsForList().rightPushAll(key, auctions);
        log.debug("Saved {} auctions ids in cache with key {}, setting expiry time", auctions.size(), key);
        auctionRedisTemplate.expire(key, CACHE_EXPIRATION_TIME, CACHE_EXPIRATION_TIME_UNIT);
        log.debug("Expiry time set for {} auctions ids with key {}", auctions.size(), key);
    }

    private String buildKey(String searchId, SortableField sortBy, Sort.Direction orderBy){
        return "filteredAuctions/searchId{%s}/sortBy{%s}/orderBy{%s}".formatted(searchId, sortBy, orderBy);
    }
}
