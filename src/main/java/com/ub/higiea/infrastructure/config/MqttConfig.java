package com.ub.higiea.infrastructure.config;

import com.ub.higiea.infrastructure.ports.mqtt.MqttMessageListener;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    private final MqttMessageListener mqttMessageListener;
    private MqttClient client;

    @Value("${mqtt.broker.clientId}")
    private String clientId;

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    public MqttConfig(MqttMessageListener mqttMessageListener) {
        this.mqttMessageListener = mqttMessageListener;
    }

    @PostConstruct
    public void connect() throws MqttException {
        client = new MqttClient(brokerUrl, clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setUserName("${MQTT_BROKER_USERNAME}");
        options.setPassword("${MQTT_BROKER_PASSWORD}".toCharArray());

        client.setCallback(mqttMessageListener);
        client.connect(options);
    }

}
