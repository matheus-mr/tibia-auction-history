package com.tibiaauctionhistory.module.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, ?> redisTemplate (RedisConnectionFactory cf) {
//        new Jackson2ObjectMapperBuilder().failOnEmptyBeans(false)
//                .failOnUnknownProperties(false)
//                .indentOutput(false)
//                .serializationInclusion(JsonInclude.Include.NON_NULL)
//                .modules(new JavaTimeModule())
//                .featuresToDisable(
//                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
//                        DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS,
//                        SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS
//                )
//                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
//                .build();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.registerModule(new JavaTimeModule());

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        RedisTemplate<String, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        redisTemplate.setKeySerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }
}
