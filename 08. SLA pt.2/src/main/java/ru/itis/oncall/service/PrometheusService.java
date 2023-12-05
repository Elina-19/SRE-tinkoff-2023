package ru.itis.oncall.service;

import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import ru.itis.oncall.client.PrometheusApi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;

@ApplicationScoped
@Slf4j
public class PrometheusService {

    @Inject
    @RestClient
    PrometheusApi prometheusApi;

    @Inject
    IndicatorService indicatorService;

    @Scheduled(every = "{oncall.scrape.interval}")
    public void getIndicators() {
        log.info("Start scrape");

        long currentTimeMillis = System.currentTimeMillis() / 1000;
        var datetime = Instant.ofEpochSecond(currentTimeMillis);

        var successTotal = lastValue("increase(prober_get_teams_scenario_success_total[1m])", currentTimeMillis, 0.0f);
        indicatorService.save("prober_get_teams_scenario_success_total", 1.0f, successTotal, successTotal < 1, datetime);

        var failTotal = lastValue("increase(prober_get_teams_scenario_fail_total[1m])", currentTimeMillis, 100.0f);
        indicatorService.save("prober_get_teams_scenario_fail_total", 0.0f, failTotal, failTotal > 0, datetime);

        var durationMs = lastValue("prober_get_teams_scenario_duration_ms", currentTimeMillis, 2.0f);
        indicatorService.save("prober_get_teams_scenario_duration_ms", 0.1f, durationMs, durationMs > 0.1, datetime);
    }

    private Float lastValue(String query, Long time, Float def) {
        var response = prometheusApi.lastValue(query, time);
        if (response.getData().getResult().size() == 0) return def;

        return response.getData().getResult().get(0).getValue()[1];
    }
}
