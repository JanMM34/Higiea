package com.ub.higiea.infrastructure.ports.mqtt;

public class MqttSensorMessage {

    private Long sensorId;
    private int state;

    private MqttSensorMessage(Long sensorId, int state) {
        this.sensorId = sensorId;
        this.state = state;
    }

    public static MqttSensorMessage of(Long sensorId, int state) {
        return new MqttSensorMessage(sensorId, state);
    }

    public Long getSensorId() {
        return sensorId;
    }

    public int getState() {
        return state;
    }

}
