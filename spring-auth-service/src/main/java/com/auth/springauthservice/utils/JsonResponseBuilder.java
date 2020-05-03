package com.auth.springauthservice.utils;

import java.time.LocalDateTime;
import java.util.Map;

import org.json.JSONObject;

public class JsonResponseBuilder {


    public static String getJsonFormatString(Map<String, String> responses){

        JSONObject json = new JSONObject();

        json.put("timestamp", LocalDateTime.now());

        for(Map.Entry<String, String> entry: responses.entrySet()){
            json.put(entry.getKey(), entry.getValue());
        }

        return json.toString();
    }
    
}