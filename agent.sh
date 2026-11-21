#!/bin/bash

export LC_ALL=C

API_URL="https://telemetry-api-63ej.onrender.com/v1/telemetry"
API_KEY="d7ea7736842b3a86bf8407e3a0ba3b686b9d7334798c1d9022379905a3c98790"

echo "Starting continuous telemetry agent. Press [CTRL+C] to stop."

while true; do
    HOSTNAME=$(hostname)
    CPU_IDLE=$(top -bn1 | awk -F '[ ,]+' '/Cpu\(s\)/ {for (i = 2; i <= NF; i++) if ($i == "id") {print $(i - 1); exit}}')
    if [[ ! "$CPU_IDLE" =~ ^[0-9]+([.][0-9]+)?$ ]]; then
        echo "ERROR - Could not read CPU idle percentage from top." >&2
        sleep 5
        continue
    fi
    CPU_USAGE=$(awk -v idle="$CPU_IDLE" 'BEGIN {print 100 - idle}')
    MEM_TOTAL=$(free -m | awk '/^Mem:/{print $2}')
    MEM_USED=$(free -m | awk '/^Mem:/{print $3}')
    DISK_TOTAL=$(df -P -BG / | awk 'NR==2 {gsub(/G/, "", $2); print $2}')
    DISK_USED=$(df -P -BG / | awk 'NR==2 {gsub(/G/, "", $3); print $3}')
    UPTIME=$(cat /proc/uptime | awk '{print $1}' | cut -d. -f1)

    INVALID_METRIC=false
    for METRIC in CPU_USAGE MEM_TOTAL MEM_USED DISK_TOTAL DISK_USED UPTIME; do
        if [[ ! "${!METRIC}" =~ ^[0-9]+([.][0-9]+)?$ ]]; then
            echo "ERROR - Invalid or missing metric: $METRIC. Telemetry was not sent." >&2
            INVALID_METRIC=true
        fi
    done
    if [[ "$INVALID_METRIC" == true ]]; then
        sleep 5
        continue
    fi

    PAYLOAD=$(cat <<EOF
{
  "hostname": "$HOSTNAME",
  "cpuUsagePercentage": $CPU_USAGE,
  "memoryTotalMb": $MEM_TOTAL,
  "memoryUsedMb": $MEM_USED,
  "diskTotalGb": $DISK_TOTAL,
  "diskUsedGb": $DISK_USED,
  "systemUptimeSeconds": $UPTIME
}
EOF
    )

    HTTP_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$API_URL" \
      -H "Content-Type: application/json" \
      -H "X-API-KEY: $API_KEY" \
      -d "$PAYLOAD")

    if [[ "$HTTP_RESPONSE" =~ ^2[0-9]{2}$ ]]; then
        echo "$(date -u +"%Y-%m-%dT%H:%M:%SZ") - INFO - Telemetry data successfully sent. HTTP Status: $HTTP_RESPONSE"
    else
        echo "$(date -u +"%Y-%m-%dT%H:%M:%SZ") - ERROR - Failed to send telemetry data. HTTP Status: $HTTP_RESPONSE"
    fi

done
