package com.matheusmr.tibiaauctionhistory.auctionsearch.service;

import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuctionDomainCacheServiceRedisImpl implements AuctionDomainCacheService {

    private static final TimeUnit CACHE_EXPIRATION_UNIT = TimeUnit.MINUTES;
    private static final long AUCTION_DOMAIN_CACHE_EXPIRATION = 60;
    private static final String AUCTION_DOMAIN_CACHE_KEY = "AUCTION_DOMAIN";

    private final RedisTemplate<String, AuctionDomainDTO> auctionDomainRedisTemplate;

    @Autowired
    public AuctionDomainCacheServiceRedisImpl(RedisTemplate<String, AuctionDomainDTO> auctionDomainRedisTemplate) {
        this.auctionDomainRedisTemplate = auctionDomainRedisTemplate;
    }

    @Override
    public AuctionDomainDTO getAuctionDomainDTO() {
        log.debug("Finding auction domain on cache with key {}", AUCTION_DOMAIN_CACHE_KEY);
        final AuctionDomainDTO auctionDomainDTO = auctionDomainRedisTemplate
                .opsForValue()
                .get(AUCTION_DOMAIN_CACHE_KEY);
        log.debug("{} auction domain on cache", auctionDomainDTO != null ? "Found" : "Did not find");
        return auctionDomainDTO;
    }

    @Override
    public void cacheAuctionDomainDTO(AuctionDomainDTO auctionDomainDTO) {
        log.debug("Caching auction domain {}...", auctionDomainDTO);
        auctionDomainRedisTemplate.opsForValue().set(
                AUCTION_DOMAIN_CACHE_KEY,
                auctionDomainDTO,
                Duration.of(
                        AUCTION_DOMAIN_CACHE_EXPIRATION,
                        CACHE_EXPIRATION_UNIT.toChronoUnit()
                )
        );
        log.debug("Auction domain cached");
    }
}
