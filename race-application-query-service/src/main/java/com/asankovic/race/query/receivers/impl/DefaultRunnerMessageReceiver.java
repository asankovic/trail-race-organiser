package com.asankovic.race.query.receivers.impl;

import com.asankovic.race.query.data.dtos.messaging.BaseRunnerMessageData;
import com.asankovic.race.query.data.dtos.messaging.CreateRunnerMessageData;
import com.asankovic.race.query.data.dtos.messaging.DeleteRunnerMessageData;
import com.asankovic.race.query.data.dtos.messaging.UpdateRunnerMessageData;
import com.asankovic.race.query.exceptions.UnknownDistanceException;
import com.asankovic.race.query.exceptions.UnknownRunnerIdException;
import com.asankovic.race.query.services.RunnerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DefaultRunnerMessageReceiver implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRunnerMessageReceiver.class);

    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final RunnerService runnerService;

    public DefaultRunnerMessageReceiver(final ObjectMapper objectMapper,
                                        final Validator validator,
                                        final RunnerService runnerService) {
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.runnerService = runnerService;
    }

    //TODO should errors be ignored or fallback/retry logic created
    //TODO extend messages with unique IDs to improve logging
    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        final var messageBody = new String(message.getBody());
        LOG.debug("Received message data: {}", messageBody);

        try {
            final BaseRunnerMessageData baseRunnerMessageData = objectMapper.readValue(messageBody, BaseRunnerMessageData.class);
            final Errors validationErrors = validator.validateObject(baseRunnerMessageData);

            if (validationErrors.hasErrors()) {
                LOG.warn("Received message does not respect data constraints, skipping. Errors:{}", validationErrors);
                return;
            }

            determineAndProcessOperation(baseRunnerMessageData);
        } catch (final JsonProcessingException e) {
            LOG.warn("Received message can not be parsed, no modification to data will be done and message will be discarded", e);
        } catch (final Exception e) {
            LOG.error("Unable to process message", e);
        }
    }

    private void determineAndProcessOperation(final BaseRunnerMessageData baseRunnerMessageData)
            throws UnknownDistanceException, UnknownRunnerIdException {
        switch (baseRunnerMessageData) {
            case CreateRunnerMessageData createRunnerMessageData -> runnerService.createRunner(createRunnerMessageData);
            case UpdateRunnerMessageData updateRunnerMessageData -> runnerService.updateRunner(updateRunnerMessageData);
            case DeleteRunnerMessageData deleteRunnerMessageData -> runnerService.deleteRunner(deleteRunnerMessageData);
            case null, default -> throw new IllegalArgumentException("Unknown message type!");
        }
    }
}
