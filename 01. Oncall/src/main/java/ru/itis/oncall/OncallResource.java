package ru.itis.oncall;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import ru.itis.oncall.dto.FileRequest;
import ru.itis.oncall.service.OncallService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/oncall")
@RegisterForReflection
public class OncallResource {

    @Inject
    OncallService oncallService;

    @POST
    @Path("/schedule")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createSchedule(@MultipartForm FileRequest fileRequest) {
        oncallService.createSchedule(fileRequest.getFile());
        return Response.noContent().build();
    }
}
