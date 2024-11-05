package com.asankovic.race.command.data.dtos.messaging;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRunnerMessageData extends BaseRunnerMessageData {

    private String firstName;

    private String lastName;

    private String club;

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
