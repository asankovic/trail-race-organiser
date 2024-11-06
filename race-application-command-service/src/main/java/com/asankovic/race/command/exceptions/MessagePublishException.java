package com.asankovic.race.command.exceptions;

public class MessagePublishException extends RuntimeException {

    public MessagePublishException(final String message, final Throwable cause) {
        super(message, cause);
    }
}