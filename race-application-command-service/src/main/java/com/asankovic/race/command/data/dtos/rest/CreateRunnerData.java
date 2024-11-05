package com.asankovic.race.command.data.dtos.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRunnerData {

    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must not exceed 255 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must not exceed 255 characters")
    private String lastName;

    @Size(max = 255, message = "Club name must not exceed 255 characters")
    private String club;

    @NotBlank(message = "Distance name is required")
    @Size(max = 50, message = "Distance name must not exceed 50 characters")
    private String distance;
}
