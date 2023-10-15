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
public class UpdateUserReguest {

    private Contacts contacts;

    private String name;

    @JsonProperty("full_name")
    private String fullName;
}
