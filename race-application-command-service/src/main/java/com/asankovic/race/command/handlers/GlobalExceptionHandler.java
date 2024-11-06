package com.asankovic.race.command.handlers;

import com.asankovic.race.command.data.dtos.rest.ErrorData;
import com.asankovic.race.command.data.dtos.rest.ErrorDataList;
import com.asankovic.race.command.exceptions.InvalidPublicRunnerIdException;
import com.asankovic.race.command.exceptions.MessagePublishException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDataList> handleValidationExceptions(final MethodArgumentNotValidException e) {
        final List<ErrorData> errorList = e.getBindingResult().getFieldErrors().stream().map(error -> {
            final String errorMessage = error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid content";
            return new ErrorData(errorMessage, error.getField());
        }).toList();

        LOG.debug("Data validation failed on request", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDataList(errorList));
    }

    @ExceptionHandler(InvalidPublicRunnerIdException.class)
    public ResponseEntity<ErrorData> handleInvalidPublicRunnerIdException(final InvalidPublicRunnerIdException e) {
        LOG.warn("Request attempted with public runner ID in invalid format", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorData(e.getMessage()));
    }

    @ExceptionHandler(MessagePublishException.class)
    public ResponseEntity<ErrorData> handleUnexpectedExceptions() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorData("Unable to publish a new event for a given request"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorData> handleUnexpectedExceptions(final RuntimeException e) {
        LOG.error("Unexpected application error occurred", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorData("An unexpected application error occurred, please try again later or contact user service"));
    }
}