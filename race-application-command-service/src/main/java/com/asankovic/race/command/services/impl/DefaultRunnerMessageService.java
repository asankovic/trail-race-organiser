package com.asankovic.race.command.services.impl;

import com.asankovic.race.command.data.dtos.messaging.BaseRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.DeleteRunnerMessageData;
import com.asankovic.race.command.data.dtos.messaging.UpdateRunnerMessageData;
import com.asankovic.race.command.data.dtos.rest.CreateRunnerData;
import com.asankovic.race.command.data.dtos.rest.UpdateRunnerData;
import com.asankovic.race.command.exceptions.MessagePublishException;
import com.asankovic.race.command.mappers.RunnerMessageMapper;
import com.asankovic.race.command.services.RunnerMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DefaultRunnerMessageService implements RunnerMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRunnerMessageService.class);

    private final RunnerMessageMapper messageMapper;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${redis.runner.channel.name}")
    private String channelName;

    public DefaultRunnerMessageService(final RunnerMessageMapper messageMapper,
                                       final StringRedisTemplate redisTemplate,
                                       final ObjectMapper objectMapper) {
        this.messageMapper = messageMapper;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishCreationEvent(final CreateRunnerData createRunnerData) {
        final CreateRunnerMessageData creationMessage = messageMapper.mapToCreationMessage(createRunnerData);
        publish(creationMessage);
    }

    @Override
    public void publishUpdateEvent(final UpdateRunnerData updateRunnerData, final String publicRunnerID) {
        final UpdateRunnerMessageData updateMessage = messageMapper.mapToUpdateMessage(updateRunnerData, publicRunnerID);
        publish(updateMessage);
    }

    @Override
    public void publishDeletionEvent(final String publicRunnerID) {
        final DeleteRunnerMessageData deleteMessage = messageMapper.mapToDeleteMessage(publicRunnerID);
        publish(deleteMessage);
    }

    private void publish(final BaseRunnerMessageData messageData) {
        try {
            final String convertedMessageData = objectMapper.writeValueAsString(messageData);
            redisTemplate.convertAndSend(channelName, convertedMessageData);
            LOG.debug("Successfully published {} event", messageData.getMessageType());
        } catch (final JsonProcessingException e) {
            LOG.warn("Failed to serialize message, event will not be published", e);
        } catch (final Exception e) {
            final String errorMessage = "Error occurred when trying to publish a new %s event".
                    formatted(messageData.getMessageType());
            LOG.error(errorMessage, e);
            throw new MessagePublishException(errorMessage, e);
        }
    }
}
