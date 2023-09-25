package ru.itis.oncall.client.dto.request;

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
public class CreateScheduleRequest {

    private Long start;

    private Long end;

    private String user;

    private String team;

    private String role;
}
