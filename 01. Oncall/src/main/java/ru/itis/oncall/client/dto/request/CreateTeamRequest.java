package ru.itis.oncall.client.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class CreateTeamRequest {

    private String name;

    private String email;

    @JsonProperty("slack_channel")
    private String slackChannel;

    @JsonProperty("scheduling_timezone")
    private String schedulingTimezone;
}
