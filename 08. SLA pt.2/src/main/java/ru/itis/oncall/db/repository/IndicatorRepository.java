package ru.itis.oncall.db.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import ru.itis.oncall.db.entity.Indicator;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IndicatorRepository implements PanacheRepository<Indicator> {
}
