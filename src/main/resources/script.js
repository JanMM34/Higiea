// Initialize the map
const barcelonaBounds = L.latLngBounds(
    [41.2611, 2.0528],
    [41.4670, 2.2285]
);

const map = L.map('map', {
    center: [41.3851, 2.1734],
    zoom: 14,
    minZoom: 14,
    maxBounds: barcelonaBounds // Restrict map to Barcelona's bounds
});

// Add tile layer (map background)
L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png').addTo(map);

// Variables
let isAddingSensor = false;
const addSensorBtn = document.getElementById('addSensorBtn');
const truckSelect = document.getElementById('truckSelect');
let sensorMarkers = {}; // Keyed by sensor ID

// MQTT Client
let mqttClient;
// MQTT Connection Options
const mqttOptions = {
    connectTimeout: 4000,
    clientId: 'webclient_' + Math.random().toString(16).substr(2, 8),
    // username: 'your_mqtt_username', // Avoid hardcoding credentials
    // password: 'your_mqtt_password',
    keepalive: 60,
    clean: true,
    reconnectPeriod: 4000, // Auto-reconnect after 4 seconds
};

// Connect to the MQTT broker over WebSocket
function connectMqtt() {
    const brokerUrl = 'ws://localhost:9002'; // WebSocket URL of your MQTT broker
    mqttClient = mqtt.connect(brokerUrl, mqttOptions);

    mqttClient.on('connect', () => {
        console.log('MQTT connected');
    });

    mqttClient.on('error', (err) => {
        console.error('MQTT connection error:', err);
        mqttClient.end();
    });
}

connectMqtt();

// Load sensors and trucks
loadSensors();
loadTrucks();

// Event listeners
addSensorBtn.addEventListener('click', () => {
    isAddingSensor = !isAddingSensor;
    addSensorBtn.style.backgroundColor = isAddingSensor ? 'blue' : '';
    if (isAddingSensor) {
        map.on('click', onMapClick);
    } else {
        map.off('click', onMapClick);
    }
});

truckSelect.addEventListener('change', () => {
    const selectedTruckId = truckSelect.value;
    const selectedOption = truckSelect.options[truckSelect.selectedIndex];
    const routeId = selectedOption.dataset.routeId;

    // Clear existing route and truck markers
    clearExistingRoute();
    clearTruckMarker();
    clearSensorMarkers();

    if (routeId && routeId !== 'null') {
        fetchAndDisplayRoute(routeId);
    } else {
        alert('Selected truck does not have an assigned route.');
        loadSensors();
    }
});

// Functions

// Load sensors from backend and add to map
function loadSensors() {
    axios.get('http://localhost:8080/sensors')
        .then(response => {
            const sensors = response.data;
            sensors.forEach(sensor => {
                addSensorMarker(sensor);
            });
        })
        .catch(error => {
            console.error('Error fetching sensors:', error);
        });
}

// Load trucks from backend and populate the dropdown
function loadTrucks() {
    axios.get('http://localhost:8080/trucks')
        .then(response => {
            const trucks = response.data;
            populateTruckDropdown(trucks);
        })
        .catch(error => {
            console.error('Error fetching trucks:', error);
        });
}

// Populate the truck dropdown menu
function populateTruckDropdown(trucks) {
    // Clear existing options
    truckSelect.innerHTML = '<option value="" disabled selected>Select a truck</option>';

    trucks.forEach(truck => {
        const option = document.createElement('option');
        option.value = truck.id;
        option.textContent = `Truck ${truck.id} (Capacity: ${truck.maxLoadCapacity})`;

        // Store routeId in data attribute
        option.dataset.routeId = truck.routeId;

        if (!truck.routeId) {
            // Disable option if routeId is null
            option.disabled = true;
            option.textContent += ' - No Route';
        }

        truckSelect.appendChild(option);
    });
}

// Add a sensor marker to the map
function addSensorMarker(sensor) {
    const { id, location, containerState } = sensor;
    const latitude = location.x; // Latitude
    const longitude = location.y; // Longitude

    // Determine marker color based on container state
    let markerColor;
    switch (containerState) {
        case 'FULL':
            markerColor = 'red';
            break;
        case 'HALF':
            markerColor = 'orange';
            break;
        case 'EMPTY':
            markerColor = 'green';
            break;
        default:
            markerColor = 'gray'; // Unknown state
    }

    // Create a custom icon
    const sensorIcon = L.divIcon({
        className: 'sensor-marker',
        html: `<div style="background-color: ${markerColor}; width: 12px; height: 12px; border-radius: 50%;"></div>`,
        iconSize: [12, 12],
        popupAnchor: [0, -6],
    });

    // Add marker to the map
    return L.marker([latitude, longitude], {icon: sensorIcon})
        .addTo(map)
        .bindPopup(createSensorPopupContent(id, containerState));
}

function updateSensorMarker(sensorId, newState) {
    // Find the marker for the sensor
    const marker = sensorMarkers[sensorId];
    if (marker) {
        // Remove the old marker
        map.removeLayer(marker);

        // Update the containerState based on newState
        let containerState;
        switch (parseInt(newState)) {
            case 0:
                containerState = 'EMPTY';
                break;
            case 50:
                containerState = 'HALF';
                break;
            case 80:
                containerState = 'FULL';
                break;
            default:
                containerState = 'UNKNOWN';
        }

        // Create a new marker with updated state
        const updatedSensor = {
            id: sensorId,
            location: {
                x: marker.getLatLng().lng,
                y: marker.getLatLng().lat,
            },
            containerState: containerState
        };

        const newMarker = addSensorMarker(updatedSensor);

        // Update the sensorMarkers map
        sensorMarkers[sensorId] = newMarker;
    }
}

function clearSensorMarkers() {
    for (const sensorId in sensorMarkers) {
        map.removeLayer(sensorMarkers[sensorId]);
    }
    sensorMarkers = {};
}

// Create popup content with update button
function createSensorPopupContent(sensorId, containerState) {
    return `
        <div class="sensor-popup">
            <strong>Sensor ID:</strong> ${sensorId}<br>
            <strong>Current State:</strong> ${containerState}<br>
            <div class="state-buttons">
                <button onclick="updateSensorState(${sensorId}, 0)">Set to EMPTY</button>
                <button onclick="updateSensorState(${sensorId}, 50)">Set to HALF</button>
                <button onclick="updateSensorState(${sensorId}, 80)">Set to FULL</button>
            </div>
        </div>
    `;
}

// Update sensor state via MQTT message
function updateSensorState(sensorId, newState) {
    // Create the MQTT message payload
    const message = {
        sensorId: sensorId,
        state: parseInt(newState)
    };

    // Publish the message to the MQTT topic
    const topic = 'sensors'; // Ensure this matches the topic your backend is subscribed to
    mqttClient.publish(topic, JSON.stringify(message), (err) => {
        if (err) {
            console.error('MQTT publish error:', err);
            alert('Failed to update sensor state.');
        } else {
            alert('Sensor state updated successfully.');
            // Update the marker on the map
            updateSensorMarker(sensorId, newState);
        }
    });
}

// Handle map click event for adding new sensors
function onMapClick(e) {
    if (!isAddingSensor) return;

    const { lat, lng } = e.latlng;

    // Create a sensor create request
    const newSensor = {
        latitude: lat,
        longitude: lng,
        containerState: 'EMPTY' // Default state
    };

    // Send POST request to create a new sensor
    axios.post('http://localhost:8080/sensors', newSensor)
        .then(response => {
            const createdSensor = response.data;
            addSensorMarker(createdSensor);
            isAddingSensor = false;
            addSensorBtn.style.backgroundColor = '';
            map.off('click', onMapClick);
        })
        .catch(error => {
            console.error('Error adding sensor:', error);
        });
}

// Fetch and display route by ID
function fetchAndDisplayRoute(routeId) {
    axios.get(`http://localhost:8080/routes/${routeId}`)
        .then(response => {
            const geoJson = response.data;
            displayRouteOnMap(geoJson);
        })
        .catch(error => {
            console.error('Error fetching route:', error);
        });
}

// Display route on the map
function displayRouteOnMap(routeData) {
    // Remove existing route layer if any
    clearExistingRoute();
    // Create a layer group to hold all route-related layers
    map.routeLayer = L.layerGroup().addTo(map);

    // Iterate over each feature in the GeoJSON
    routeData.features.forEach(feature => {
        const { type, properties, geometry } = feature;

        if (geometry.type === 'Point') {
            const coordinates = geometry.coordinates;
            const latitude = coordinates[1]; // GeoJSON uses [lon, lat]
            const longitude = coordinates[0];

            if (properties.type === 'depot') {
                // Add depot marker
                addDepotMarker(latitude, longitude);
            } else if (properties.sensorId) {
                // Add sensor marker
                addSensorMarkerOnRoute(latitude, longitude, properties);
            }
        } else if (geometry.type === 'LineString') {
            // Add route line
            const routeLine = L.geoJSON(feature, {
                style: {
                    color: 'blue',
                    weight: 4,
                    opacity: 0.7
                }
            }).addTo(map.routeLayer);
        }
    });
}

function addDepotMarker(latitude, longitude) {
    // Create a custom icon for the depot
    const depotIcon = L.divIcon({
        className: 'depot-marker',
        html: `<div style="background-color: purple; width: 16px; height: 16px; border-radius: 50%;"></div>`,
        iconSize: [16, 16],
        popupAnchor: [0, -8],
    });

    // Add marker to the route layer
    const depotMarker = L.marker([latitude, longitude], { icon: depotIcon })
        .addTo(map.routeLayer)
        .bindPopup(`<div class="depot-popup">
                      <strong>Depot</strong>
                    </div>`);
}

function addSensorMarkerOnRoute(latitude, longitude, properties) {
    const { sensorId, containerState } = properties;

    // Determine marker color based on container state
    let markerColor;
    switch (containerState) {
        case 'FULL':
            markerColor = 'red';
            break;
        case 'HALF':
            markerColor = 'yellow';
            break;
        case 'EMPTY':
            markerColor = 'green';
            break;
        default:
            markerColor = 'gray'; // Unknown state
    }

    // Create a custom icon
    const sensorIcon = L.divIcon({
        className: 'sensor-marker',
        html: `<div style="background-color: ${markerColor}; width: 14px; height: 14px; border-radius: 50%;"></div>`,
        iconSize: [14, 14],
        popupAnchor: [0, -7],
    });

    // Add marker to the route layer
    L.marker([latitude, longitude], { icon: sensorIcon })
        .addTo(map.routeLayer)
        .bindPopup(`<div class="sensor-popup">
                      <strong>Sensor ID:</strong> ${sensorId}<br>
                      <strong>State:</strong> ${containerState}
                    </div>`);
}

// Clear existing route from the map
function clearExistingRoute() {
    if (map.routeLayer) {
        map.removeLayer(map.routeLayer);
        map.routeLayer = null;
    }
}

// Clear truck marker from the map
function clearTruckMarker() {
    if (map.truckMarker) {
        map.removeLayer(map.truckMarker);
        map.truckMarker = null;
    }
}
