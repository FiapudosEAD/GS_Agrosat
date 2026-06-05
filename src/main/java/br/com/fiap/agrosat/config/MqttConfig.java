package br.com.fiap.agrosat.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

/**
 * Configura o cliente MQTT do backend usando Spring Integration.
 *
 * <p>O adapter de entrada assina o filtro de tópicos (por padrão
 * {@code agrosat/+/+/+}, ou seja, todas as leituras de todos os talhões)
 * e encaminha cada mensagem recebida para o canal {@code mqttInputChannel},
 * onde o {@link br.com.fiap.agrosat.service.MqttListenerService} a consome.</p>
 */
@Configuration
public class MqttConfig {

    @Value("${mqtt.broker.url:tcp://localhost:1883}")
    private String brokerUrl;

    @Value("${mqtt.client.id:agrosat-backend}")
    private String clientId;

    @Value("${mqtt.topic.filter:agrosat/+/+/+}")
    private String topicFilter;

    @Value("${mqtt.qos:1}")
    private int qos;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(60);
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * Canal por onde trafegam as mensagens MQTT recebidas.
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * Adapter de entrada: assina o broker e injeta mensagens no canal.
     * Adiciona o sufixo "-in" ao clientId para não colidir com eventuais
     * publishers que reutilizem o mesmo prefixo de cliente.
     */
    @Bean
    public MessageProducer mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        clientId + "-in", mqttClientFactory(), topicFilter);

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }
}
