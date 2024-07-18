package ru.bkmz.services.api;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiService {
    private String url = "http://localhost:8080/api";

    public JSONObject get(String path, Map<String, String> parameters) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            List<NameValuePair> parameters2 = new ArrayList<>();
            ClassicRequestBuilder httpGetBuilder = ClassicRequestBuilder
                    .get(url + path);

            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                httpGetBuilder.addParameter(entry.getKey(), entry.getValue());
            }

            ClassicHttpRequest httpGet = httpGetBuilder.build();

            return httpclient.execute(httpGet, response -> {
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String retSrc = EntityUtils.toString(entity);
                    JSONObject result = new JSONObject(retSrc);
                    if (result.isNull("data")) return null;
                    return result.getJSONObject("data");
                }
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject post(String path, String jsonString) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            StringEntity requestEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
            ClassicHttpRequest http = ClassicRequestBuilder.post(url + path)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .setEntity(requestEntity)
                    .build();
            return httpclient.execute(http, response -> {
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String retSrc = EntityUtils.toString(entity);
                    JSONObject result = new JSONObject(retSrc);
                    if (result.isNull("data")) return null;
                    return result.getJSONObject("data");
                }
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
