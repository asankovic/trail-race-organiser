package com.asankovic.race.query.data.dtos.messaging;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

import static com.asankovic.race.query.constants.BaseConstants.ALLOWED_TEXT_REGEX;
import static com.asankovic.race.query.constants.BaseConstants.ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRunnerMessageData extends BaseRunnerMessageData {

    @NotBlank(message = "Public ID is required for updating the runner data")
    @UUID(message = "Must be a valid UUID")
    private String runnerPublicID;

    @Pattern(regexp = ALLOWED_TEXT_REGEX, message = ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE)
    @Size(max = 255, message = "First name must not exceed 255 characters")
    private String firstName;

    @Pattern(regexp = ALLOWED_TEXT_REGEX, message = ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE)
    @Size(max = 255, message = "Last name must not exceed 255 characters")
    private String lastName;

    @Pattern(regexp = ALLOWED_TEXT_REGEX, message = ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE)
    @Size(max = 255, message = "Club name must not exceed 255 characters")
    private String club;

    @Pattern(regexp = ALLOWED_TEXT_REGEX, message = ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE)
    @Size(max = 50, message = "Club code must not exceed 50 characters")
    private String distanceCode;
}
