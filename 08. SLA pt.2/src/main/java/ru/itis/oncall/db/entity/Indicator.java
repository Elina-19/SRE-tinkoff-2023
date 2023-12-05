package ru.itis.oncall.db.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@Entity
public class Indicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant datetime;

    private String name;

    private Float slo;

    private Float value;

    @Column(name = "is_bad")
    private Boolean isBad;
}
