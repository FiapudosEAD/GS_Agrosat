package br.com.fiap.agrosat.service;

import br.com.fiap.agrosat.exception.ResourceNotFoundException;
import br.com.fiap.agrosat.model.entity.Alerta;
import br.com.fiap.agrosat.model.entity.DadoSatelite;
import br.com.fiap.agrosat.model.entity.Leitura;
import br.com.fiap.agrosat.model.entity.Sensor;
import br.com.fiap.agrosat.model.entity.Talhao;
import br.com.fiap.agrosat.model.dto.alerta.AlertaResponse;
import br.com.fiap.agrosat.repository.AlertaRepository;
import br.com.fiap.agrosat.repository.DadoSateliteRepository;
import br.com.fiap.agrosat.repository.LeituraRepository;
import br.com.fiap.agrosat.repository.TalhaoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private static final Logger log = LoggerFactory.getLogger(AlertaService.class);

    private final AlertaRepository alertaRepository;
    private final TalhaoRepository talhaoRepository;
    private final LeituraRepository leituraRepository;
    private final DadoSateliteRepository dadoSateliteRepository;

    // ---- Thresholds ----
    private static final double UMIDADE_CRITICA = 20.0;
    private static final int LEITURAS_CONSECUTIVAS = 3;
    private static final double TEMP_GEADA = 2.0;
    private static final double CHUVA_ENXURRADA = 50.0;
    private static final double QUEDA_NDVI_PCT = 0.15;

    public List<AlertaResponse> listar(Long talhaoId, Long usuarioId) {
        talhaoRepository
                .findByIdAndUsuarioId(talhaoId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado"));

        return alertaRepository
                .findByTalhaoIdOrderByGeradoEmDesc(talhaoId)
                .stream()
                .map(alerta -> new AlertaResponse(
                        alerta.getId(),
                        alerta.getTalhao().getId(),
                        alerta.getTipo(),
                        alerta.getSeveridade(),
                        alerta.getMensagem(),
                        alerta.getLido(),
                        alerta.getGeradoEm()))
                .toList();
    }

    public void marcarComoLido(Long alertaId, Long usuarioId, Integer lido) {
        Alerta alerta = alertaRepository
                .findByIdAndUsuarioId(alertaId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado"));

        alerta.setLido(lido);
        alertaRepository.save(alerta);
    }

    public void avaliarEGerarAlertas(Leitura leitura, String topic) {
        Sensor sensor = leitura.getSensor();
        Talhao talhao = sensor.getTalhao();
        double valor = leitura.getValor();

        switch (sensor.getTipo()) {
            case "UMIDADE_SOLO" -> avaliarUmidade(sensor, talhao, valor);
            case "TEMPERATURA" -> avaliarTemperatura(talhao, valor);
            case "PLUVIOMETRO" -> avaliarPluviometro(talhao, valor);
            default -> log.debug("Tipo de sensor sem regra de alerta: {}", sensor.getTipo());
        }
    }

    private void avaliarUmidade(Sensor sensor, Talhao talhao, double valor) {
        if (valor >= UMIDADE_CRITICA) return;

        List<Leitura> ultimas = leituraRepository.findTop3BySensorOrderByRegistradoEmDesc(sensor);
        boolean secaConfirmada = ultimas.size() >= LEITURAS_CONSECUTIVAS
                && ultimas.stream().allMatch(l -> l.getValor() < UMIDADE_CRITICA);

        if (secaConfirmada) {
            criarAlerta(talhao, "SECA", "CRITICA",
                    "Umidade do solo abaixo de %.0f%% por %d leituras consecutivas (atual %.1f%%). Recomenda-se irrigacao imediata."
                            .formatted(UMIDADE_CRITICA, LEITURAS_CONSECUTIVAS, valor));
        }
    }

    private void avaliarTemperatura(Talhao talhao, double valor) {
        if (valor < TEMP_GEADA) {
            criarAlerta(talhao, "GEADA", "ALTA",
                    "Temperatura de %.1f°C indica risco de geada iminente. Proteja a cultura.".formatted(valor));
        }
    }

    private void avaliarPluviometro(Talhao talhao, double valor) {
        if (valor > CHUVA_ENXURRADA) {
            criarAlerta(talhao, "ENXURRADA", "ALTA",
                    "Chuva acumulada de %.1f mm/h acima do limite de %.0f mm/h. Risco de enxurrada e erosao."
                            .formatted(valor, CHUVA_ENXURRADA));
        }
    }

    public void avaliarNdvi(Talhao talhao) {
        List<DadoSatelite> serie = dadoSateliteRepository
                .findByTalhaoAndDataCapturaAfterOrderByDataCapturaAsc(talhao, LocalDate.now().minusDays(7));

        if (serie.size() < 2) return;

        double ndviAntigo = serie.get(0).getNdvi();
        double ndviRecente = serie.get(serie.size() - 1).getNdvi();
        if (ndviAntigo <= 0) return;

        double queda = (ndviAntigo - ndviRecente) / ndviAntigo;
        if (queda > QUEDA_NDVI_PCT) {
            criarAlerta(talhao, "ESTRESSE_CULTURA", "MEDIA",
                    "NDVI caiu %.0f%% em 7 dias (de %.3f para %.3f). Possivel estresse hidrico ou nutricional."
                            .formatted(queda * 100, ndviAntigo, ndviRecente));
        }
    }

    private void criarAlerta(Talhao talhao, String tipo, String severidade, String mensagem) {
        boolean jaExisteNaoLido = alertaRepository.existsByTalhaoAndTipoAndLidoFalse(talhao, tipo);
        if (jaExisteNaoLido) {
            log.debug("Alerta {} para talhao {} ja existe nao lido; ignorando duplicata.", tipo, talhao.getId());
            return;
        }

        Alerta alerta = new Alerta();
        alerta.setTalhao(talhao);
        alerta.setTipo(tipo);
        alerta.setSeveridade(severidade);
        alerta.setMensagem(mensagem);
        alerta.setLido(0);
        alertaRepository.save(alerta);

        log.warn("[ALERTA {}] talhao {} ({}) -> {}", severidade, talhao.getId(), talhao.getNome(), mensagem);
    }
}