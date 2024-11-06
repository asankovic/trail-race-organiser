package com.asankovic.race.command.exceptions;

public class InvalidPublicRunnerIdException extends RuntimeException {
    public InvalidPublicRunnerIdException(final String message) {
        super(message);
    }
}