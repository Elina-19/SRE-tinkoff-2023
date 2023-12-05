package ru.itis.oncall.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import ru.itis.oncall.client.dto.IndicatorResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "prometheus")
public interface PrometheusApi {

    @GET
    @Path("/api/v1/query")
    @Produces(MediaType.APPLICATION_JSON)
    IndicatorResponse lastValue(@QueryParam("query") String query, @QueryParam("time") Long time);
}
