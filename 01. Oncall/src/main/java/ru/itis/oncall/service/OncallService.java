package ru.itis.oncall.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import ru.itis.oncall.client.OncallApi;
import ru.itis.oncall.client.dto.request.*;
import ru.itis.oncall.dto.CreateSchedule;
import ru.itis.oncall.dto.Duty;
import ru.itis.oncall.dto.Team;
import ru.itis.oncall.dto.User;
import ru.itis.oncall.utils.OncallUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
@Slf4j
public class OncallService {

    private static final String COOKIE = "oncall-auth=7e70c3381f9b426b29985fa93962324d2b915688Em4AdwOtaI9m/AVF3M9gbA==Mx3NYa3PZRwv0OpvVfDR9q0HuV+e8+6QQapnC3/iVDWLjuQ1i4/e/vYvzXkdxcofbDqY+4wbSBqGFvlozqDGC6ntIOm0IbQGME8Vbik/LydUH3tNpkbKKHl6+AAo3CMiE5xR5OBiKwFJ6XGQT4wcyxx6BIMPhjExbVoFXHvU9e4Omx+1NHqYQx6TRJNu4b5WcEE8Kcc4a2DP1PN08+NqowF/0/7GyVzyrj+f81bfoO2x0k8LFrLlxPtmYCJQLB3ongI7pRWloC+59A==";
    private static final String CSRF_TOKEN = "9f18d37eabd1bc4b8de5cdf0105c5c18";

    @Inject
    @RestClient
    OncallApi oncallApi;

    public void createSchedule(InputStream is) {
        var token = oncallApi.login("root", "root");
        try {
            var schedule = isToObject(is);
            schedule.getTeams().forEach(t -> createTeam(t, CSRF_TOKEN));
        } catch (Exception e) {
            log.error("Failed to create schedule from .yml file!", e);
        }
    }

    private void createTeam(Team team, String token) {
        log.info("Create team: {}", team.getName());
        oncallApi.createTeam(COOKIE, token, CreateTeamRequest.builder()
                .email(team.getEmail())
                .name(team.getName())
                .slackChannel(team.getSlackChannel())
                .schedulingTimezone(team.getSchedulingTimezone())
                .build());

        team.getUsers().forEach(user -> createUser(user, team, token));
    }

    private void createUser(User user, Team team, String token) {
        log.info("Create user: {}", user.getName());
        oncallApi.signUp(SignUpRequest.builder()
                .name(user.getName())
                .build());
        oncallApi.updateUser(token, user.getName(), UpdateUserReguest.builder()
                .contacts(Contacts.builder()
                        .call(user.getPhoneNumber())
                        .email(user.getEmail())
                        .build())
                .name(user.getName())
                .fullName(user.getFullName())
                .build());
        oncallApi.addUserToTeam(COOKIE, token, team.getName(), AddToTeamRequest.builder()
                .name(user.getName())
                .build());

        user.getDuty().forEach(duty -> createDuty(user.getName(), team, duty, token));
    }

    private void createDuty(String user, Team team, Duty duty, String token) {
        var date = OncallUtils.dateToSeconds(duty.getDate(), team.getSchedulingTimezone());
        oncallApi.createEvent(COOKIE, token, CreateScheduleRequest.builder()
                .start(date.getLeft())
                .end(date.getRight())
                .role(duty.getRole())
                .user(user)
                .team(team.getName())
                .build());
    }

    private CreateSchedule isToObject(InputStream is) {
        log.info("Map file to object");
        var mapper = new ObjectMapper(new YAMLFactory());
        try {
            var result = IOUtils.toString(is, StandardCharsets.UTF_8);
            return mapper.readValue(result, CreateSchedule.class);
        } catch (IOException e) {
            log.error("Fail parse file to object: {}", e);
            return null;
        }
    }
}
