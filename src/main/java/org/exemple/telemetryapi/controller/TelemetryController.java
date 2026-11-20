package org.exemple.telemetryapi.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;

import java.util.List;

import org.exemple.telemetryapi.dto.TelemetryPayload;
import org.exemple.telemetryapi.entity.Telemetry;
import org.exemple.telemetryapi.service.TelemetryService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/v1/telemetry")
@RequiredArgsConstructor 
public class TelemetryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryController.class.getName());
    private final TelemetryService telemetryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping() 
    public ResponseEntity<Void> receiveTelemetry(@RequestBody TelemetryPayload telemetryPayload) {
        LOGGER.info("Received telemetry payload: {}", telemetryPayload.hostname());
        telemetryService.processTelemetry(telemetryPayload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping 
    public ResponseEntity<List<Telemetry>> getLastTelemetry() {
        LOGGER.info("Fetching last telemetry records");
        List<Telemetry> telemetryList = telemetryService.getLastTelemetry();
        return ResponseEntity.ok(telemetryList);
    }
}
