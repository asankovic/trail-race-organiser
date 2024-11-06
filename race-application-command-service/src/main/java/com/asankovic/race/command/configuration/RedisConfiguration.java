package com.asankovic.race.command.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfiguration {

    @Bean
    StringRedisTemplate stringRedisTemplate(final RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}