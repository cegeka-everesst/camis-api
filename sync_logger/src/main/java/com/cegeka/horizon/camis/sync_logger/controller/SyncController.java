package com.cegeka.horizon.camis.sync_logger.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;

@Controller
public class SyncController {
    private final URL url = new URL("http://localhost:8080/camis-api/sync-response");

    public SyncController() throws IOException {
    }

    public void sendSyncResult(JSONObject syncResult) throws IOException {
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        byte[] out = syncResult.toString().getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try (OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
    }
}
