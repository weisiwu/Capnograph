package io.github.weisiwu.slstracker;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/** Writes the shared event envelope using Alibaba Cloud's PutWebtracking JSON API. */
public final class SlsLogTracker {
    public static final String DEFAULT_WEBTRACKING_URL =
            "https://app-tracking.cn-hangzhou.log.aliyuncs.com/logstores/app-events/track";

    private static final int MAX_BODY_BYTES = 64 * 1024;
    private static final int TIMEOUT_MS = 5_000;
    private static final Gson GSON = new Gson();

    public enum Level { DEBUG, INFO, WARN, ERROR }

    private final URL endpoint;
    private final String channel;
    private final String environment;
    private final String appVersion;

    public SlsLogTracker(URL endpoint, String channel, String environment, String appVersion) {
        this.endpoint = Objects.requireNonNull(endpoint);
        if (!"https".equalsIgnoreCase(endpoint.getProtocol()) || endpoint.getHost().isEmpty()
                || endpoint.getUserInfo() != null || endpoint.getRef() != null) {
            throw new IllegalArgumentException("An HTTPS endpoint without user info or a fragment is required");
        }
        this.channel = requiredName(channel, "channel");
        this.environment = requiredName(environment, "environment");
        this.appVersion = requiredName(appVersion, "appVersion");
    }

    /**
     * Send on a background thread on Android. A successful reply contains the SLS request ID
     * when the destination exposes it. A relay can forward the same JSON body after authentication.
     */
    public String send(Level level, String eventName, long clientTimeMs, Map<String, ?> fields)
            throws IOException {
        byte[] body = encodeEvent(level, eventName, clientTimeMs, fields);
        if (body.length > MAX_BODY_BYTES) {
            throw new IOException("SLS event exceeds the SDK size limit");
        }

        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        try {
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(body.length);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("x-log-apiversion", "0.6.0");
            connection.setRequestProperty("x-log-bodyrawsize", Integer.toString(body.length));

            try (OutputStream output = connection.getOutputStream()) {
                output.write(body);
            }

            int status = connection.getResponseCode();
            if (status < 200 || status >= 300) {
                throw new IOException("SLS write returned HTTP " + status);
            }
            return connection.getHeaderField("x-log-requestid");
        } finally {
            connection.disconnect();
        }
    }

    byte[] encodeEvent(Level level, String eventName, long clientTimeMs, Map<String, ?> fields) {
        String type = requiredName(eventName, "eventName");
        String severity = Objects.requireNonNull(level).name();

        JsonObject common = new JsonObject();
        common.addProperty("type", type);
        common.addProperty("level", severity);
        common.addProperty("client_time_ms", clientTimeMs);
        common.addProperty("environment", environment);

        JsonObject payload = new JsonObject();
        payload.add("businessParam", GSON.toJsonTree(Objects.requireNonNull(fields)));
        payload.add("commonParams", common);

        JsonObject log = new JsonObject();
        log.addProperty("ctl", "");
        log.addProperty("pid", "");
        log.addProperty("ua", "");
        log.addProperty("sr", "");
        log.addProperty("rurl", "");
        log.addProperty("os", "Android");
        log.addProperty("channel", channel);
        log.addProperty("ver", appVersion);
        log.addProperty("pld", GSON.toJson(payload));
        // Keep these columns for the existing Logstore indexes while clients migrate to pld.
        log.addProperty("event_name", type);
        log.addProperty("app_version", appVersion);
        log.addProperty("environment", environment);
        log.addProperty("client_time_ms", Long.toString(clientTimeMs));
        log.addProperty("level", severity);

        JsonArray logs = new JsonArray();
        logs.add(log);
        JsonObject body = new JsonObject();
        body.add("__logs__", logs);
        return GSON.toJson(body).getBytes(StandardCharsets.UTF_8);
    }

    private static String requiredName(String value, String field) {
        String name = Objects.requireNonNull(value, field).trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(field + " must not be empty");
        }
        return name;
    }
}
