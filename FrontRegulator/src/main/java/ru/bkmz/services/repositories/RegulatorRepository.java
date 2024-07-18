package ru.bkmz.services.repositories;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.bkmz.services.api.ApiService;

import java.util.HashMap;
import java.util.Map;

public class RegulatorRepository {
    private ApiService apiService;

    public RegulatorRepository() {
        apiService = new ApiService();
    }

    public Integer getTemperature() {
        JSONObject data = apiService.get("/regulator/last", new HashMap<>());
        return data.getInt("temperature");
    }

    public Integer setTemperature(Float temperature) {
        JSONObject payload = new JSONObject();
        payload.put("temperature", String.valueOf(temperature));
        JSONObject data = apiService.post("/regulator/set", payload.toString());
        return data.getInt("temperature");
    }

    public JSONArray getTemperatureHistory(int limit, int offset) {
        Map<String, String> payload = new HashMap<>();
        payload.put("limit", String.valueOf(limit));
        payload.put("offset", String.valueOf(offset));
        JSONObject data = apiService.get("/regulator/history", payload);
        return data.getJSONArray("history");
    }

    public void clearHistory() {
        apiService.post("/regulator/history/clear", "");
    }
}
