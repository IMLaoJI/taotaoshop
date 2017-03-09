package com.taotao.portal.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private Map<String, Object> map = new HashMap<String, Object>();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil() {

    }

    public static JsonUtil getInstance() {
        return new JsonUtil();
    }
    
    public static JsonUtil start(String key, Object value) {
        return getInstance().put(key, value);
    }

    public JsonUtil put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public Map<String, Object> get() {
        return this.map;
    }

    public String toJson() {
        try {
            return MAPPER.writeValueAsString(this.map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
