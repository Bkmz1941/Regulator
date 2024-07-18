package ru.bkmz.services.api;

import org.json.JSONObject;

@FunctionalInterface
public interface CallbackInterface {
    String method(JSONObject jsonObject);
}
