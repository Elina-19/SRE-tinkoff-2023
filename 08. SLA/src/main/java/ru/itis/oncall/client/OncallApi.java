package ru.itis.oncall.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient(configKey = "oncall-api")
public interface OncallApi {

    @GET
    @Path("/api/v0/teams")
    @Consumes(MediaType.APPLICATION_JSON)
    Response getTeams();
}
