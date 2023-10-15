package ru.itis.oncall.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import ru.itis.oncall.client.OncallApi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
@Slf4j
public class OncallService {

    @Inject
    @RestClient
    OncallApi oncallApi;

    private MeterRegistry meterRegistry;

    private AtomicInteger teamsQuantity;
    private AtomicInteger usersQuantity;

    public OncallService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        teamsQuantity = new AtomicInteger();
        usersQuantity = new AtomicInteger();

        meterRegistry.gauge("oncall_teams_quantity", teamsQuantity);
        meterRegistry.gauge("oncall_users_quantity", usersQuantity);
    }


//    @Scheduled(cron = "{cron.metrics}")
    @Scheduled(every = "10s") // for test
    public void getUsers() {
        var users = oncallApi.getUsers();
        var teams = oncallApi.getTeams();

        teamsQuantity.set(teams.size());
        usersQuantity.set(users.size());
    }
}
