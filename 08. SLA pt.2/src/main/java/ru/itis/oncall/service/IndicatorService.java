package ru.itis.oncall.service;

import ru.itis.oncall.db.entity.Indicator;
import ru.itis.oncall.db.repository.IndicatorRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;

@ApplicationScoped
public class IndicatorService {

    @Inject
    IndicatorRepository indicatorRepository;

    @Transactional
    public void save(String name, Float slo, Float value, Boolean isBad, Instant time) {
        indicatorRepository.persist(buildIndicator(name, slo, value, isBad, time));
    }

    private Indicator buildIndicator(String name, Float slo, Float value, Boolean isBad, Instant time) {
        return Indicator.builder()
                .name(name)
                .slo(slo)
                .value(value)
                .isBad(isBad)
                .datetime(time)
                .build();
    }
}
