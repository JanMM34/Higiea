let config;

try {
    config = {
        API_BASE_URL: process.env.API_BASE_URL,
        MQTT_BROKER_URL: process.env.MQTT_BROKER_URL,
        MQTT_USERNAME: process.env.MQTT_USERNAME,
        MQTT_PASSWORD: process.env.MQTT_PASSWORD,
    };
} catch (error) {
    config = {
        API_BASE_URL: 'http://localhost:8080',
        MQTT_BROKER_URL: 'ws://localhost:9002',
        MQTT_USERNAME: '',
        MQTT_PASSWORD: '',
    };
}

export default config;
