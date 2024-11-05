package com.asankovic.race.query.data.dtos.messaging;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRunnerMessageData extends BaseRunnerMessageData {

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
    private String distanceName;

    public CreateRunnerMessageData(final String firstName,
                                   final String lastName,
                                   final String club,
                                   final String distanceName) {
        super(RunnerMessageType.CREATE);
        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
        this.distanceName = distanceName;
    }
}
