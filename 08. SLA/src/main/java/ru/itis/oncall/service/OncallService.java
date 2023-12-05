package ru.itis.oncall.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import ru.itis.oncall.client.OncallApi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
@Slf4j
public class OncallService {

    @Inject
    @RestClient
    OncallApi oncallApi;

    private MeterRegistry meterRegistry;

    private Counter getTeams;
    private Counter getTeamsSuccess;
    private Counter getTeamsFail;

    private AtomicLong duration;

    public OncallService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.getTeams = Counter.builder("prober_get_teams_scenario_total")
                .description("Total count of runs the get teams scenario to oncall API")
                .register(meterRegistry);
        this.getTeamsSuccess = Counter.builder("prober_get_teams_scenario_success_total")
                .description("Total count of success runs the get teams scenario to oncall API")
                .register(meterRegistry);
        this.getTeamsFail = Counter.builder("prober_get_teams_scenario_fail_total")
                .description("Total count of failed runs the get teams scenario to oncall API")
                .register(meterRegistry);
        duration = new AtomicLong();

        meterRegistry.gauge("prober_get_teams_scenario_duration_ms", duration);
    }

    @Scheduled(every = "{oncall.scrape.interval}")
    public void getTeams() {
        getTeams.increment();

        try {
            long startTime = System.currentTimeMillis();
            var response = oncallApi.getTeams();
            long endTime = System.currentTimeMillis();
            var status = response.getStatus();

            if (status == 200 || status == 201) {
                getTeamsSuccess.increment();
            } else {
                getTeamsFail.increment();
            }
            duration.set(endTime - startTime);
        } catch (Exception e) {
            log.error("Failed to get teams: {}", e);
            getTeamsFail.increment();
        }
    }
}
