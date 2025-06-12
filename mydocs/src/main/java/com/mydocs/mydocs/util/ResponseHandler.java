package com.mydocs.mydocs.util;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {


    public static ResponseEntity<Object> generateResponse(HttpStatus status,boolean isSuccess ,String message, Object response) {

        Map<String, Object> body = new LinkedHashMap<>();

        try {
            body.put("timestamp", Instant.now());
            body.put("status", status.value());
            body.put("isSuccess",isSuccess);
            body.put("message", message != null ? message : "No message provided");
            body.put("response", response);

            return new ResponseEntity<>(body,status);
        } catch (Exception e) {
            body.clear();
            body.put("timestamp", Instant.now());
            body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            body.put("isSuccess", false);
            body.put("message", "Error generating response");
            body.put("response", null);
            return new ResponseEntity<>(body,status);

        }
    }

}