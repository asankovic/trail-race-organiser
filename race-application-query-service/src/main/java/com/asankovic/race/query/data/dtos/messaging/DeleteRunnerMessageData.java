package com.asankovic.race.query.data.dtos.messaging;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteRunnerMessageData extends BaseRunnerMessageData {

    @NotBlank(message = "Public ID is required for runner deletion")
    private String runnerPublicID;

    public DeleteRunnerMessageData(final String runnerPublicID) {
        super(RunnerMessageType.DELETE);
        this.runnerPublicID = runnerPublicID;
    }
}
