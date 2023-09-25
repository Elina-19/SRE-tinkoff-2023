package ru.itis.oncall.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Data
@RegisterForReflection
public class FileRequest {

    @FormParam("file")
    @Schema(description = "Файл в формате .yml, где содержится информация о командах, " +
                          "участниках команд и их дежурствах")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private InputStream file;
}
