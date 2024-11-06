package com.asankovic.race.query.configuration;

import com.asankovic.race.query.receivers.impl.DefaultRunnerMessageReceiver;
import com.asankovic.race.query.services.RunnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.validation.Validator;


@Configuration
@Profile("!test")
public class RedisConfiguration {

    @Value("${redis.runner.channel.name}")
    private String channelName;

    @Bean
    DefaultRunnerMessageReceiver receiver(final ObjectMapper objectMapper,
                                          final Validator validator,
                                          final RunnerService runnerService) {
        return new DefaultRunnerMessageReceiver(objectMapper, validator, runnerService);
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(final RedisConnectionFactory connectionFactory,
                                                                final MessageListenerAdapter listenerAdapter) {

        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, ChannelTopic.of(channelName));

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(final DefaultRunnerMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "onMessage");
    }
}