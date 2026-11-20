package org.exemple.telemetryapi.entity;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;    

@Entity 
@Table(name = "telemetry")
@NoArgsConstructor
@AllArgsConstructor 
@Builder 
@Getter 
@Setter     
public class Telemetry {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hostname", nullable = false, length = 100)
    private String hostname;

    @Column(name = "cpu_usage_percentage", nullable = false)
    private Double cpuUsagePercentage;

    @Column(name = "memory_total_mb", nullable = false)
    private Double memoryTotalMb;

    @Column(name = "memory_used_mb", nullable = false)
    private Double memoryUsedMb;

    @Column(name = "disk_total_gb", nullable = false)
    private Double diskTotalGb;

    @Column(name = "disk_used_gb", nullable = false)
    private Double diskUsedGb;

    @Column(name = "system_uptime_seconds", nullable = false)
    private Long systemUptimeSeconds;

    @Column(name = "captured_at", nullable = false, updatable = false)
    private LocalDateTime capturedAt;

    @PrePersist 
    protected void onCreate() {
        if(this.capturedAt == null) {
            this.capturedAt = LocalDateTime.now();
        }
    }
}
