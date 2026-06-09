package br.com.fiap.agrosat.model.dto.leitura;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa o payload JSON publicado pelos sensores no broker MQTT.
 *
 * <pre>
 * {
 *   "sensor_id": 12,
 *   "valor": 18.7,
 *   "unidade": "%",
 *   "timestamp": "2026-06-05T14:32:00Z",
 *   "bateria": 87
 * }
 * </pre>
 */
public class LeituraMqttDTO {

    @JsonProperty("sensor_id")
    private Long sensorId;

    private Double valor;

    private String unidade;

    private Instant timestamp;

    private Integer bateria;

    public LeituraMqttDTO() {
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getBateria() {
        return bateria;
    }

    public void setBateria(Integer bateria) {
        this.bateria = bateria;
    }

    @Override
    public String toString() {
        return "LeituraMqttDTO{sensorId=%d, valor=%.2f, unidade='%s', bateria=%d}"
                .formatted(sensorId, valor, unidade, bateria);
    }
}
