package com.ub.higiea.infrastructure.utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class GeoJsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String combineRoutes(List<String> geoJsonList) {
        ObjectNode featureCollection = mapper.createObjectNode();
        featureCollection.put("type", "FeatureCollection");
        ArrayNode allFeatures = mapper.createArrayNode();

        try {
            for (String geoJson : geoJsonList) {
                JsonNode node = mapper.readTree(geoJson);
                ArrayNode features = (ArrayNode) node.get("features");
                allFeatures.addAll(features);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing GeoJSON", e);
        }

        featureCollection.set("features", allFeatures);

        try {
            return mapper.writeValueAsString(featureCollection);
        } catch (Exception e) {
            throw new RuntimeException("Error generating GeoJSON response", e);
        }
    }
}
