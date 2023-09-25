package ru.itis.oncall.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {

    private String name;

    @JsonProperty("scheduling_timezone")
    private String schedulingTimezone;

    private String email;

    @JsonProperty("slack_channel")
    private String slackChannel;

    private List<User> users;
}
