package ru.itis.oncall.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import ru.itis.oncall.dto.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RegisterRestClient(configKey = "oncall-api")
public interface OncallApi {

    @GET
    @Path("/api/v0/users")
    @Consumes(MediaType.APPLICATION_JSON)
    List<User> getUsers();

    @GET
    @Path("/api/v0/teams")
    @Consumes(MediaType.APPLICATION_JSON)
    List<String> getTeams();
}
