package com.asankovic.race.command.handlers;

import com.asankovic.race.command.data.dtos.rest.ErrorData;
import com.asankovic.race.command.data.dtos.rest.ErrorDataList;
import com.asankovic.race.command.exceptions.MessagePublishException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

import static java.util.Objects.isNull;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //TODO improve mockMvc tests with detailed field checks
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDataList> handleValidationExceptions(final MethodArgumentNotValidException e) {
        LOG.debug("Data validation failed on request", e);

        final List<ErrorData> errorList = constructFieldErrorDataList(e.getBindingResult().getFieldErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDataList.forErrors(errorList));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorDataList> handleMethodValidationException(final HandlerMethodValidationException e) {
        LOG.debug("Data validation failed on request", e);

        final List<ErrorData> errorList = e.getAllValidationResults()
                .stream()
                .map(parameterValidationResult -> {
                    if (parameterValidationResult instanceof ParameterErrors parameterErrors) {
                        return constructFieldErrorDataList(parameterErrors.getFieldErrors());
                    }
                    return List.of(constructParameterErrorData(parameterValidationResult));
                })
                .flatMap(List::stream)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDataList.forErrors(errorList));
    }

    private List<ErrorData> constructFieldErrorDataList(final List<FieldError> fieldErrors) {
        return fieldErrors.stream().map(error -> {
            if (isNull(error)) {
                return ErrorData.empty();
            }
            final String errorMessage = error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid content";
            return new ErrorData(errorMessage, error.getField());
        }).toList();
    }

    private ErrorData constructParameterErrorData(final ParameterValidationResult parameterValidationResult) {
        final String field = parameterValidationResult.getMethodParameter().getParameterName();
        final List<String> errorMessages = parameterValidationResult.getResolvableErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();

        return new ErrorData(StringUtils.join(errorMessages, ","), field);
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