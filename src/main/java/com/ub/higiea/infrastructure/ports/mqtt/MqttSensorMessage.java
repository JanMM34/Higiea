package com.ub.higiea.infrastructure.ports.mqtt;

public class MqttSensorMessage {

    private Long sensorId;
    private int state;

    public MqttSensorMessage(){

    }

    public MqttSensorMessage(Long sensorId, int state) {
        this.sensorId = sensorId;
        this.state = state;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public int getState() {
        return state;
    }

}
