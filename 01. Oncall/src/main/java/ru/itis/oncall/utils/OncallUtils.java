package ru.itis.oncall.utils;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.experimental.UtilityClass;
import org.graalvm.collections.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@UtilityClass
@RegisterForReflection
public class OncallUtils {

    public static Pair<Long, Long> dateToSeconds(String date, String zone) {
        var dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var day = LocalDate.parse(date, dtf);
        var zoneId = ZoneId.of(zone);

        var zdtStart = day.atStartOfDay(zoneId);
        var zdtStop = day.plusDays(1).atStartOfDay(zoneId);

        return Pair.create(zdtStart.toEpochSecond(), zdtStop.toEpochSecond());
    }
}
