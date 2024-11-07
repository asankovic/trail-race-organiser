package com.asankovic.race.query.data.dtos.messaging;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DeleteRunnerMessageData extends BaseRunnerMessageData {

    @NotBlank(message = "Public ID is required for runner deletion")
    @UUID(message = "Must be a valid UUID")
    private String runnerPublicID;

    public DeleteRunnerMessageData(final String runnerPublicID) {
        super(RunnerMessageType.DELETE);
        this.runnerPublicID = runnerPublicID;
    }
}
