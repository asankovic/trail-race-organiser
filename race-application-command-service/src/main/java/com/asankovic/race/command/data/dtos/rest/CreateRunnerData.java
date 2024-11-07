package com.asankovic.race.command.data.dtos.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.asankovic.race.command.constants.BaseConstants.ALLOWED_TEXT_REGEX;
import static com.asankovic.race.command.constants.BaseConstants.ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRunnerData {

    @Schema(description = "First name of the runner", defaultValue = "John")
    @Pattern(regexp = ALLOWED_TEXT_REGEX, message = ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE)
    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must not exceed 255 characters")
    private String firstName;

    @Schema(description = "Last name of the runner", defaultValue = "Doe")
    @Pattern(regexp = ALLOWED_TEXT_REGEX, message = ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE)
    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must not exceed 255 characters")
    private String lastName;

    @Schema(description = "Club name associated with the runner", defaultValue = "Generic Club")
    @Pattern(regexp = ALLOWED_TEXT_REGEX, message = ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE)
    @Size(max = 255, message = "Club name must not exceed 255 characters")
    private String club;

    @Schema(description = "Distance name associated with the runner", defaultValue = "5k")
    @Pattern(regexp = ALLOWED_TEXT_REGEX, message = ALLOWED_TEXT_REGEX_VALIDATION_MESSAGE)
    @NotBlank(message = "Distance name is required")
    @Size(max = 50, message = "Distance name must not exceed 50 characters")
    private String distance;
}
