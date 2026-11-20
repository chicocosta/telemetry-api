package org.exemple.telemetryapi.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.exemple.telemetryapi.dto.TelemetryPayload;
import org.exemple.telemetryapi.entity.Telemetry;
import org.exemple.telemetryapi.repository.TelemetryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class TelemetryService {
    private static final Logger log = LoggerFactory.getLogger(TelemetryService.class);
    private final TelemetryRepository telemetryRepository;

    @Transactional 
    public void processTelemetry(TelemetryPayload payload) {
        log.info("Processing telemetry payload: {}", payload.hostname());

        Telemetry telemetry = Telemetry.builder()
            .hostname(payload.hostname())
            .cpuUsagePercentage(payload.cpuUsagePercentage())
            .memoryTotalMb(payload.memoryTotalMb())
            .memoryUsedMb(payload.memoryUsedMb())
            .diskTotalGb(payload.diskTotalGb())
            .diskUsedGb(payload.diskUsedGb())
            .systemUptimeSeconds(payload.systemUptimeSeconds())
            .build();

        telemetryRepository.save(telemetry);
        log.info("Telemetry saved successfully for hostname {}", telemetry.getHostname());
    }

    @Transactional(readOnly = true) 
    public List<Telemetry> getLastTelemetry() {
        return telemetryRepository.findTop100ByOrderByCapturedAtDesc();
    }
}
