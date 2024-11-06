package com.asankovic.race.query.handlers;

import com.asankovic.race.query.data.dtos.rest.ErrorData;
import com.asankovic.race.query.exceptions.InvalidPublicRunnerIdException;
import com.asankovic.race.query.exceptions.UnknownRunnerIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UnknownRunnerIdException.class)
    public ResponseEntity<ErrorData> handleUnknownRunnerIdException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorData("Runner with requested ID not found"));
    }

    @ExceptionHandler(InvalidPublicRunnerIdException.class)
    public ResponseEntity<ErrorData> handleInvalidPublicRunnerIdException(final InvalidPublicRunnerIdException e) {
        LOG.warn("Request attempted with public runner ID in invalid format", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorData(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorData> handleUnexpectedExceptions(final RuntimeException e) {
        LOG.error("Unexpected application error occurred", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorData("An unexpected application error occurred, please try again later or contact user service"));
    }
}
