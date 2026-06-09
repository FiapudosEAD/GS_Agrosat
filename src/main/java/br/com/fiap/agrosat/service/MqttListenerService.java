package br.com.fiap.agrosat.service;

import br.com.fiap.agrosat.model.dto.leitura.LeituraMqttDTO;
import br.com.fiap.agrosat.model.entity.Leitura;
import br.com.fiap.agrosat.model.entity.Sensor;
import br.com.fiap.agrosat.repository.LeituraRepository;
import br.com.fiap.agrosat.repository.SensorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Consome as mensagens MQTT recebidas no canal {@code mqttInputChannel},
 * persiste cada leitura na tabela {@code leitura} e delega ao
 * {@link AlertaService} a avaliação das regras de geração de alerta.
 *
 * <p>Demonstra a cadeia completa: sensor -&gt; broker -&gt; API -&gt; banco -&gt; app.</p>
 */
@Service
public class MqttListenerService {

    private static final Logger log = LoggerFactory.getLogger(MqttListenerService.class);

    private final LeituraRepository leituraRepository;
    private final SensorRepository sensorRepository;
    private final AlertaService alertaService;
    private final ObjectMapper objectMapper;

    public MqttListenerService(LeituraRepository leituraRepository,
                               SensorRepository sensorRepository,
                               AlertaService alertaService) {
        this.leituraRepository = leituraRepository;
        this.sensorRepository = sensorRepository;
        this.alertaService = alertaService;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        String topic = String.valueOf(message.getHeaders().get("mqtt_receivedTopic"));
        String payload = String.valueOf(message.getPayload());

        try {
            LeituraMqttDTO dto = objectMapper.readValue(payload, LeituraMqttDTO.class);

            Sensor sensor = sensorRepository.findById(dto.getSensorId()).orElse(null);
            if (sensor == null) {
                log.warn("Leitura descartada: sensor {} nao cadastrado (topico {})",
                        dto.getSensorId(), topic);
                return;
            }

            // Persiste a leitura
            Leitura leitura = new Leitura();
            leitura.setSensor(sensor);
            leitura.setValor(dto.getValor());
            leitura.setUnidade(dto.getUnidade());
            leitura.setRegistradoEm(toLocalDateTime(dto));
            leitura = leituraRepository.save(leitura);

            log.info("[MQTT] {} -> {} {} (sensor {})",
                    topic, dto.getValor(), dto.getUnidade(), sensor.getId());

            // Avalia regras de alerta sobre a nova leitura
            alertaService.avaliarEGerarAlertas(leitura, topic);

        } catch (Exception e) {
            log.error("Falha ao processar mensagem MQTT do topico {}: {}",
                    topic, e.getMessage(), e);
        }
    }

    private LocalDateTime toLocalDateTime(LeituraMqttDTO dto) {
        if (dto.getTimestamp() == null) {
            return LocalDateTime.now();
        }
        return LocalDateTime.ofInstant(dto.getTimestamp(), ZoneId.systemDefault());
    }
}
