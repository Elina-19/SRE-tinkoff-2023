package ru.itis.oncall.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import ru.itis.oncall.client.dto.request.*;
import ru.itis.oncall.client.dto.response.LoginResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "oncall-api")
public interface OncallApi {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    LoginResponse login(@FormParam("username") String username,
                        @FormParam("password") String password);

    @POST
    @Path("/api/v0/users")
    @Consumes(MediaType.APPLICATION_JSON)
    void signUp(SignUpRequest request);

    @PUT
    @Path("/api/v0/users/{user-name}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateUser(@HeaderParam("X-CSRF-TOKEN") String token,
                    @PathParam("user-name") String userName,
                    UpdateUserReguest request);

    @POST
    @Path("/api/v0/teams")
    @Consumes(MediaType.APPLICATION_JSON)
    void createTeam(@HeaderParam("Cookie") String cookie,
                    @HeaderParam("X-CSRF-TOKEN") String token,
                    CreateTeamRequest request);

    @POST
    @Path("/api/v0/events")
    @Consumes(MediaType.APPLICATION_JSON)
    void createEvent(@HeaderParam("Cookie") String cookie,
                     @HeaderParam("X-CSRF-TOKEN") String token,
                     CreateScheduleRequest request);

    @POST
    @Path("/api/v0/teams/{team}/users")
    @Consumes(MediaType.APPLICATION_JSON)
    void addUserToTeam(@HeaderParam("Cookie") String cookie,
                       @HeaderParam("X-CSRF-TOKEN") String token,
                       @PathParam("team") String team,
                       AddToTeamRequest request);
}
