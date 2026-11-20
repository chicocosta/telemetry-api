package org.exemple.telemetryapi.repository;

import java.util.List;

import org.exemple.telemetryapi.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryRepository extends JpaRepository<Telemetry, Long>{
    List<Telemetry> findTop100ByOrderByCapturedAtDesc();

}
