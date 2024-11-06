package com.asankovic.race.query.exceptions;

public class InvalidPublicRunnerIdException extends RuntimeException {
    public InvalidPublicRunnerIdException(final String message) {
        super(message);
    }
}