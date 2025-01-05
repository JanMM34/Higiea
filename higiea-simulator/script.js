import config from './config.js';

let selectedZone = 'all'; // Default to all
const zoneSelect = document.getElementById('zoneSelect');
const addSensorBtn = document.getElementById('addSensorBtn');
const truckSelect = document.getElementById('truckSelect');
const addTruckBtn = document.getElementById('addTruckBtn');
const truckPlateInput = document.getElementById('truckPlateInput');
const truckMaxLoadCapacityInput = document.getElementById('truckMaxLoadCapacityInput');

// Initialize the map bounds for Barcelona
const barcelonaBounds = L.latLngBounds([41.2611, 2.0528], [41.4670, 2.2285]);
const map = L.map('map', {
    center: [41.3851, 2.1734],
    zoom: 14,
    minZoom: 14,
    maxBounds: barcelonaBounds // Restrict map to Barcelona's bounds
});

// Add tile layer (map background)
L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png').addTo(map);

// Variables to track adding sensors/trucks
let isAddingSensor = false;
let isAddingTruck = false;

// Marker storage
let sensorMarkers = {}; // Keyed by sensor ID
let truckMarkers = {};  // Keyed by truck ID

// MQTT Client
let mqttClient;
const mqttOptions = {
    connectTimeout: 4000,
    clientId: 'higiea_simulator' + Math.random().toString(16).substring(2, 8),
    username: config.MQTT_USERNAME,
    password: config.MQTT_PASSWORD,
    keepalive: 60,
    clean: true,
    reconnectPeriod: 4000,
};

// Connect to MQTT over WebSocket
function connectMqtt() {
    const brokerUrl = config.MQTT_BROKER_URL;
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

/* ---------------------------------------
   ZONE CHANGE LOGIC
---------------------------------------- */
zoneSelect.addEventListener('change', () => {
    selectedZone = zoneSelect.value;
    const zoneBaseUrl = config.ZONES[selectedZone];

    // Disable "Add Sensor" and "Add Truck" if "All" is selected
    addSensorBtn.disabled = (selectedZone === 'all');
    addTruckBtn.disabled  = (selectedZone === 'all');

    // Clear existing route and markers
    clearExistingRoute();
    Object.values(sensorMarkers).forEach(marker => map.removeLayer(marker));
    Object.values(truckMarkers).forEach(marker => map.removeLayer(marker));
    sensorMarkers = {};

    // Reload data based on selected zone
    if (selectedZone !== 'all') {
        loadSensors(zoneBaseUrl);
        loadTrucks(zoneBaseUrl);
    }
});

// If the page loads with a specific zone, fetch data initially
if (selectedZone !== 'all') {
    const baseUrl = config.ZONES[selectedZone];
    loadSensors(baseUrl);
    loadTrucks(baseUrl);
}

/* ---------------------------------------
   LOAD SENSORS
---------------------------------------- */
function loadSensors(baseUrl) {
    axios.get(`${baseUrl}/sensors`)
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

/* ---------------------------------------
   LOAD TRUCKS
---------------------------------------- */
function loadTrucks(baseUrl) {
    axios.get(`${baseUrl}/trucks`)
        .then(response => {
            const trucks = response.data;
            populateTruckDropdown(trucks);
            // Add a marker for each truck
            trucks.forEach(truck => {
                addTruckMarker(truck);
            });
        })
        .catch(error => {
            console.error('Error fetching trucks:', error);
        });
}

/* ---------------------------------------
   POPULATE TRUCK DROPDOWN
---------------------------------------- */
function populateTruckDropdown(trucks) {
    // Clear existing options
    truckSelect.innerHTML = '<option value="" disabled selected>Select a truck</option>';

    trucks.forEach(truck => {
        const option = document.createElement('option');
        option.value = truck.plate;
        option.textContent = `Truck ${truck.plate} (Capacity: ${truck.maxLoadCapacity})`;

        // Store routeId in data attribute
        option.dataset.routeId = truck.routeId;

        if (!truck.routeId) {
            // Optionally disable if no route
            option.disabled = true;
            option.textContent += ' - No Route';
        }

        truckSelect.appendChild(option);
    });
}

/* ---------------------------------------
   ADD SENSOR MARKER
---------------------------------------- */
function addSensorMarker(sensor) {
    const { id, location, containerState } = sensor;
    const latitude  = location.x; // as per your backend
    const longitude = location.y;

    // Determine marker color
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
            markerColor = 'gray'; // unknown
    }

    // Create custom icon
    const sensorIcon = L.divIcon({
        className: 'sensor-marker',
        html: `<div style="background-color: ${markerColor}; width: 12px; height: 12px; border-radius: 50%;"></div>`,
        iconSize: [12, 12],
        popupAnchor: [0, -6],
    });

    // Add marker
    const marker = L.marker([latitude, longitude], { icon: sensorIcon })
        .addTo(map)
        .bindPopup(createSensorPopupContent(id, containerState));

    // Store in dictionary
    sensorMarkers[id] = marker;

    return marker;
}

/* ---------------------------------------
   ADD TRUCK MARKER
---------------------------------------- */
function addTruckMarker(truck) {
    // Note: if your backend returns depotLocation.x / .y as lat/long:
    const latitude  = truck.depotLocation?.x;
    const longitude = truck.depotLocation?.y;

    if (latitude == null || longitude == null) {
        // Fallback if your backend actually returns truck.latitude/truck.longitude
        // const { latitude, longitude } = truck;
        console.warn('Truck location not provided for:', truck);
        return;
    }

    const truckIcon = L.divIcon({
        className: 'sensor-marker',
        html: `<div style="background-color: blue; width: 12px; height: 12px; border-radius: 50%;"></div>`,
        iconSize: [12, 12],
        popupAnchor: [0, -6],
    });

    const marker = L.marker([latitude, longitude], {
        icon: truckIcon,
        color: 'blue',
        radius: 8,
    }).addTo(map);



    const popupContent = `
        <div>
            <strong>Plate:</strong> ${truck.plate}<br>
            <strong>Max Load Capacity:</strong> ${truck.maxLoadCapacity}
        </div>
    `;
    marker.bindPopup(popupContent);

    // Store marker by truck.id for easy removal later
    truckMarkers[truck.id] = marker;

    return marker;
}

/* ---------------------------------------
   CLEAR ALL TRUCK MARKERS
---------------------------------------- */
function clearAllTruckMarkers() {
    Object.values(truckMarkers).forEach(marker => {
        map.removeLayer(marker);
    });
    truckMarkers = {};
}

/* ---------------------------------------
   SENSOR UPDATES VIA MQTT
---------------------------------------- */
function updateSensorState(sensorId, newState) {
    const topic = `sensors/${selectedZone}`;
    const message = {
        sensorId: sensorId,
        state: parseInt(newState),
    };

    mqttClient.publish(topic, JSON.stringify(message), err => {
        if (err) {
            console.error('MQTT publish error:', err);
            alert('Failed to update sensor state.');
        } else {
            updateSensorMarker(sensorId, newState);
        }
    });
}
window.updateSensorState = updateSensorState; // Make it callable in popup

function updateSensorMarker(sensorId, newState) {
    const marker = sensorMarkers[sensorId];
    if (!marker) {
        console.warn(`Marker for sensor ${sensorId} not found.`);
        return;
    }

    let markerColor;
    switch (parseInt(newState)) {
        case 0:
            markerColor = 'green';
            break;
        case 50:
            markerColor = 'orange';
            break;
        case 80:
            markerColor = 'red';
            break;
        default:
            markerColor = 'gray';
    }

    const updatedIcon = L.divIcon({
        className: 'sensor-marker',
        html: `<div style="background-color: ${markerColor}; width: 14px; height: 14px; border-radius: 50%;"></div>`,
        iconSize: [14, 14],
        popupAnchor: [0, -7],
    });

    marker.setIcon(updatedIcon);
}

function createSensorPopupContent(sensorId, containerState) {
    return `
        <div class="sensor-popup">
            <strong>Sensor ID:</strong> ${sensorId}<br>
            <strong>Current State:</strong> ${containerState}<br>
            <div class="state-buttons">
                <button onclick="updateSensorState('${sensorId}', 0)">Set to EMPTY</button>
                <button onclick="updateSensorState('${sensorId}', 50)">Set to HALF</button>
                <button onclick="updateSensorState('${sensorId}', 80)">Set to FULL</button>
            </div>
        </div>
    `;
}

/* ---------------------------------------
   ADD SENSOR LOGIC
---------------------------------------- */
addSensorBtn.addEventListener('click', () => {
    isAddingSensor = !isAddingSensor;
    addSensorBtn.style.backgroundColor = isAddingSensor ? 'blue' : '';
    if (isAddingSensor) {
        map.on('click', onMapClickForSensor);
    } else {
        map.off('click', onMapClickForSensor);
    }
});

function onMapClickForSensor(e) {
    if (!isAddingSensor) return;

    const { lat, lng } = e.latlng;
    const zoneBaseUrl = config.ZONES[selectedZone];

    const newSensor = {
        latitude: lat,
        longitude: lng,
        containerState: 'EMPTY'
    };

    axios.post(`${zoneBaseUrl}/sensors`, newSensor)
        .then(response => {
            const createdSensor = response.data;
            addSensorMarker(createdSensor);
            // Reset
            isAddingSensor = false;
            addSensorBtn.style.backgroundColor = '';
            map.off('click', onMapClickForSensor);
        })
        .catch(error => {
            console.error('Error adding sensor:', error);
        });
}

/* ---------------------------------------
   ADD TRUCK LOGIC
---------------------------------------- */
addTruckBtn.addEventListener('click', () => {
    isAddingTruck = !isAddingTruck;
    addTruckBtn.style.backgroundColor = isAddingTruck ? 'green' : '';
    if (isAddingTruck) {
        map.on('click', onMapClickForTruck);
    } else {
        map.off('click', onMapClickForTruck);
    }
});

function onMapClickForTruck(e) {
    if (!truckPlateInput.value.trim()) {
        alert('Please enter a plate number before adding a truck.');
        return;
    }
    if (!truckMaxLoadCapacityInput.value.trim()) {
        alert('Please enter the max load capacity before adding a truck.');
        return;
    }

    const { lat, lng } = e.latlng;
    const baseUrl = config.ZONES[selectedZone];
    const plate = truckPlateInput.value.trim();
    const maxLoadCapacity = parseInt(truckMaxLoadCapacityInput.value, 10);

    // POST to create a new truck
    axios.post(`${baseUrl}/trucks`, {
        plate,
        latitude: lat,
        longitude: lng,
        maxLoadCapacity
    })
        .then(response => {
            const createdTruck = response.data;
            console.log('Truck created:', createdTruck);

            addTruckMarker(createdTruck);

            loadTrucks(baseUrl)

            // Reset state/UI
            isAddingTruck = false;
            addTruckBtn.style.backgroundColor = '';
            map.off('click', onMapClickForTruck);

            // Clear input fields
            truckPlateInput.value = '';
            truckMaxLoadCapacityInput.value = '';
        })
        .catch(err => {
            console.error('Error creating truck:', err);
            alert('Failed to create truck. Check console for details.');
        });
}

/* ---------------------------------------
   TRUCK SELECT ROUTE LOADING
---------------------------------------- */
truckSelect.addEventListener('change', () => {
    const selectedTruckId = truckSelect.value;
    const selectedOption = truckSelect.options[truckSelect.selectedIndex];
    const routeId = selectedOption.dataset.routeId;

    // Clear existing route and truck marker(s)
    clearExistingRoute();
    // Optionally clear or re-draw trucks if needed
    clearAllTruckMarkers();

    if (routeId && routeId !== 'null') {
        fetchAndDisplayRoute(routeId);
    } else {
        alert('Selected truck does not have an assigned route.');
        // If you want to reload sensors or trucks after clearing:
        // loadSensors(config.ZONES[selectedZone]);
        // loadTrucks(config.ZONES[selectedZone]);
    }
});

/* ---------------------------------------
   FETCH & DISPLAY ROUTE
---------------------------------------- */
function fetchAndDisplayRoute(routeId) {
    const zoneBaseUrl = config.ZONES[selectedZone];
    axios.get(`${zoneBaseUrl}/routes/${routeId}`)
        .then(response => {
            const geoJson = response.data;
            displayRouteOnMap(geoJson);
        })
        .catch(error => {
            console.error('Error fetching route:', error);
        });
}

function displayRouteOnMap(routeData) {
    // Remove existing route layer if any
    clearExistingRoute();
    // Create a layer group to hold all route-related layers
    map.routeLayer = L.layerGroup().addTo(map);

    // Iterate over each feature in the GeoJSON
    routeData.features.forEach(feature => {
        const { geometry, properties } = feature;

        if (geometry.type === 'Point') {
            const [lon, lat] = geometry.coordinates;
            if (properties.type === 'depot') {
                addDepotMarker(lat, lon);
            } else if (properties.sensorId) {
                addSensorMarkerOnRoute(lat, lon, properties);
            }
        } else if (geometry.type === 'LineString') {
            L.geoJSON(feature, {
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
    const depotIcon = L.divIcon({
        className: 'depot-marker',
        html: `<div style="background-color: blue; width: 16px; height: 16px; border-radius: 50%;"></div>`,
        iconSize: [16, 16],
        popupAnchor: [0, -8],
    });

    L.marker([latitude, longitude], { icon: depotIcon })
        .addTo(map.routeLayer)
        .bindPopup(`<div class="depot-popup"><strong>Depot</strong></div>`);
}

function addSensorMarkerOnRoute(latitude, longitude, properties) {
    const { sensorId, containerState } = properties;

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
            markerColor = 'gray';
    }

    const sensorIcon = L.divIcon({
        className: 'sensor-marker',
        html: `<div style="background-color: ${markerColor}; width: 14px; height: 14px; border-radius: 50%;"></div>`,
        iconSize: [14, 14],
        popupAnchor: [0, -7],
    });

    const popupContent = createSensorPopupContent(sensorId, containerState);

    L.marker([latitude, longitude], { icon: sensorIcon })
        .addTo(map.routeLayer)
        .bindPopup(popupContent);
}

/* ---------------------------------------
   CLEARING ROUTES
---------------------------------------- */
function clearExistingRoute() {
    if (map.routeLayer) {
        map.removeLayer(map.routeLayer);
        map.routeLayer = null;
    }
}


