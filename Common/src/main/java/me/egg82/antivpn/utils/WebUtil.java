package me.egg82.antivpn.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.egg82.antivpn.config.ConfigUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtil {
    private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

    private WebUtil() { }

    public static @NonNull String urlEncode(@NonNull String part) {
        try {
            return URLEncoder.encode(part, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ignored) {
            try {
                return URLEncoder.encode(part, StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException ignored2) {
                return part;
            }
        }
    }

    public static @NonNull String getString(@NonNull URL url) throws IOException { return getString(url, null, 5000, null, null, null, 20); }

    public static @NonNull String getString(@NonNull URL url, String method) throws IOException { return getString(url, method, 5000, null, null, null, 20); }

    public static @NonNull String getString(@NonNull URL url, String method, int timeout) throws IOException { return getString(url, method, timeout, null, null, null, 20); }

    public static @NonNull String getString(@NonNull URL url, String method, int timeout, String userAgent) throws IOException { return getString(url, method, timeout, userAgent, null, null, 20); }

    public static @NonNull String getString(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers) throws IOException { return getString(url, method, timeout, userAgent, headers, null, 20); }

    public static @NonNull String getString(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData) throws IOException { return getString(url, method, timeout, userAgent, headers, postData, 20); }

    public static @NonNull String getString(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData, int maxRedirects) throws IOException {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (!hasKey(headers, "Accept-Language")) {
            headers.put("Accept-Language", "en-US,en;q=0.8");
        }

        try (InputStream in = getInputStream(url, method, timeout, userAgent, headers, postData, maxRedirects); InputStreamReader reader = new InputStreamReader(in); BufferedReader buffer = new BufferedReader(reader)) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
    }

    public static @NonNull String getStringWithThrows(@NonNull URL url) throws IOException { return getStringWithThrows(url, null, 5000, null, null, null, 20); }

    public static @NonNull String getStringWithThrows(@NonNull URL url, String method) throws IOException { return getStringWithThrows(url, method, 5000, null, null, null, 20); }

    public static @NonNull String getStringWithThrows(@NonNull URL url, String method, int timeout) throws IOException { return getStringWithThrows(url, method, timeout, null, null, null, 20); }

    public static @NonNull String getStringWithThrows(@NonNull URL url, String method, int timeout, String userAgent) throws IOException { return getStringWithThrows(url, method, timeout, userAgent, null, null, 20); }

    public static @NonNull String getStringWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers) throws IOException { return getStringWithThrows(url, method, timeout, userAgent, headers, null, 20); }

    public static @NonNull String getStringWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData) throws IOException { return getStringWithThrows(url, method, timeout, userAgent, headers, postData, 20); }

    public static @NonNull String getStringWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData, int maxRedirects) throws IOException {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (!hasKey(headers, "Accept-Language")) {
            headers.put("Accept-Language", "en-US,en;q=0.8");
        }

        try (InputStream in = getInputStreamWithThrows(url, method, timeout, userAgent, headers, postData, maxRedirects); InputStreamReader reader = new InputStreamReader(in); BufferedReader buffer = new BufferedReader(reader)) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
    }

    public static @NonNull String getString(@NonNull HttpURLConnection conn) throws IOException {
        try (InputStream in = getInputStream(conn); InputStreamReader reader = new InputStreamReader(in); BufferedReader buffer = new BufferedReader(reader)) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
    }

    public static byte @NonNull [] getBytes(@NonNull URL url) throws IOException { return getBytes(url, null, 5000, null, null, null, 20); }

    public static byte @NonNull [] getBytes(@NonNull URL url, String method) throws IOException { return getBytes(url, method, 5000, null, null, null, 20); }

    public static byte @NonNull [] getBytes(@NonNull URL url, String method, int timeout) throws IOException { return getBytes(url, method, timeout, null, null, null, 20); }

    public static byte @NonNull [] getBytes(@NonNull URL url, String method, int timeout, String userAgent) throws IOException { return getBytes(url, method, timeout, userAgent, null, null, 20); }

    public static byte @NonNull [] getBytes(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers) throws IOException { return getBytes(url, method, timeout, userAgent, headers, null, 20); }

    public static byte @NonNull [] getBytes(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData) throws IOException { return getBytes(url, method, timeout, userAgent, headers, postData, 20); }

    public static byte @NonNull [] getBytes(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData, int maxRedirects) throws IOException {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (!hasKey(headers, "Accept-Language")) {
            headers.put("Accept-Language", "en-US,en;q=0.8");
        }

        try (InputStream in = getInputStream(url, method, timeout, userAgent, headers, postData, maxRedirects); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int read;
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, read);
            }
            out.flush();
            return out.toByteArray();
        }
    }

    public static byte @NonNull [] getBytesWithThrows(@NonNull URL url) throws IOException { return getBytesWithThrows(url, null, 5000, null, null, null, 20); }

    public static byte @NonNull [] getBytesWithThrows(@NonNull URL url, String method) throws IOException { return getBytesWithThrows(url, method, 5000, null, null, null, 20); }

    public static byte @NonNull [] getBytesWithThrows(@NonNull URL url, String method, int timeout) throws IOException { return getBytesWithThrows(url, method, timeout, null, null, null, 20); }

    public static byte @NonNull [] getBytesWithThrows(@NonNull URL url, String method, int timeout, String userAgent) throws IOException { return getBytesWithThrows(url, method, timeout, userAgent, null, null, 20); }

    public static byte @NonNull [] getBytesWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers) throws IOException { return getBytesWithThrows(url, method, timeout, userAgent, headers, null, 20); }

    public static byte @NonNull [] getBytesWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData) throws IOException { return getBytesWithThrows(url, method, timeout, userAgent, headers, postData, 20); }

    public static byte @NonNull [] getBytesWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData, int maxRedirects) throws IOException {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (!hasKey(headers, "Accept-Language")) {
            headers.put("Accept-Language", "en-US,en;q=0.8");
        }

        try (InputStream in = getInputStreamWithThrows(url, method, timeout, userAgent, headers, postData, maxRedirects); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int read;
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, read);
            }
            out.flush();
            return out.toByteArray();
        }
    }

    public static byte @NonNull [] getBytes(HttpURLConnection conn) throws IOException {
        try (InputStream in = getInputStream(conn); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int read;
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, read);
            }
            out.flush();
            return out.toByteArray();
        }
    }

    public static @NonNull HttpURLConnection getConnection(@NonNull URL url) throws IOException { return getConnection(url, null, 5000, null, null, null, 20); }

    public static @NonNull HttpURLConnection getConnection(@NonNull URL url, String method) throws IOException { return getConnection(url, method, 5000, null, null, null, 20); }

    public static @NonNull HttpURLConnection getConnection(@NonNull URL url, String method, int timeout) throws IOException { return getConnection(url, method, timeout, null, null, null, 20); }

    public static @NonNull HttpURLConnection getConnection(@NonNull URL url, String method, int timeout, String userAgent) throws IOException { return getConnection(url, method, timeout, userAgent, null, null, 20); }

    public static @NonNull HttpURLConnection getConnection(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers) throws IOException { return getConnection(url, method, timeout, userAgent, headers, null, 20); }

    public static @NonNull HttpURLConnection getConnection(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData) throws IOException { return getConnection(url, method, timeout, userAgent, headers, postData, 20); }

    public static @NonNull HttpURLConnection getConnection(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData, int maxRedirects) throws IOException {
        if (ConfigUtil.getDebugOrFalse()) {
            logger.info("Fetching URL: " + url);
        }

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        setConnectionProperties(conn, method, timeout, userAgent, headers, postData, null);

        Set<String> previousUrls = new HashSet<>();
        previousUrls.add(url.toExternalForm() + (headers != null && headers.containsKey("Set-Cookie") ? ":" + headers.get("Set-Cookie") : ""));

        int status;
        boolean redirect;
        do {
            status = conn.getResponseCode();
            redirect = status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER;

            if (redirect) {
                String cookies = conn.getHeaderField("Set-Cookie");
                String newUrl = conn.getHeaderField("Location");
                if (newUrl.charAt(0) == '/') {
                    newUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), newUrl, null).toExternalForm();
                }
                if (!previousUrls.add(newUrl + (cookies != null ? ":" + cookies : ""))) {
                    throw new IOException("Recursive redirect detected.");
                }
                if (maxRedirects >= 0 && previousUrls.size() > maxRedirects) {
                    throw new IOException("Too many redirects.");
                }
                if (ConfigUtil.getDebugOrFalse()) {
                    logger.info("Redirected to URL: " + newUrl);
                }
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                setConnectionProperties(conn, method, timeout, userAgent, headers, postData, cookies);
            }
        } while (redirect);

        return conn;
    }

    private static void setConnectionProperties(@NonNull HttpURLConnection conn, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData, String cookies) throws IOException {
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);

        if (method != null && !method.isEmpty()) {
            conn.setRequestMethod(method);
        }
        if (userAgent != null && !userAgent.isEmpty()) {
            conn.setRequestProperty("User-Agent", userAgent);
        }
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> kvp : headers.entrySet()) {
                conn.setRequestProperty(kvp.getKey(), kvp.getValue());
            }
        }
        if (cookies != null && !cookies.isEmpty()) {
            conn.setRequestProperty("Cookie", cookies);
        }
        if (postData != null) {
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            StringBuilder data = new StringBuilder();
            for (Map.Entry<String, String> kvp : postData.entrySet()) {
                data.append(URLEncoder.encode(kvp.getKey(), StandardCharsets.UTF_8.name()));
                data.append('=');
                data.append(URLEncoder.encode(kvp.getValue(), StandardCharsets.UTF_8.name()));
                data.append('&');
            }
            if (data.length() > 0) {
                data.deleteCharAt(data.length() - 1);
            }
            byte[] dataBytes = data.toString().getBytes(StandardCharsets.UTF_8);
            conn.setRequestProperty("Content-Length", String.valueOf(dataBytes.length));
            conn.setDoOutput(true);
            try (OutputStream out = conn.getOutputStream()) {
                out.write(dataBytes);
            }
        }
    }

    public static @NonNull HttpURLConnection getConnectionWithThrows(@NonNull URL url) throws IOException { return getConnectionWithThrows(url, null, 5000, null, null, null, 20); }

    public static @NonNull HttpURLConnection getConnectionWithThrows(@NonNull URL url, String method) throws IOException { return getConnectionWithThrows(url, method, 5000, null, null, null, 20); }

    public static @NonNull HttpURLConnection getConnectionWithThrows(@NonNull URL url, String method, int timeout) throws IOException { return getConnectionWithThrows(url, method, timeout, null, null, null, 20); }

    public static @NonNull HttpURLConnection getConnectionWithThrows(@NonNull URL url, String method, int timeout, String userAgent) throws IOException { return getConnectionWithThrows(url, method, timeout, userAgent, null, null, 20); }

    public static @NonNull HttpURLConnection getConnectionWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers) throws IOException { return getConnectionWithThrows(url, method, timeout, userAgent, headers, null, 20); }

    public static @NonNull HttpURLConnection getConnectionWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData) throws IOException { return getConnectionWithThrows(url, method, timeout, userAgent, headers, postData, 20); }

    public static @NonNull HttpURLConnection getConnectionWithThrows(@NonNull  URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData, int maxRedirects) throws IOException {
        HttpURLConnection conn = WebUtil.getConnection(url, method, timeout, userAgent, headers, postData, maxRedirects);
        int status = conn.getResponseCode();

        if (status >= 200 && status < 300) {
            if (status == 205) { // Reset
                throw new IOException("Could not get connection (HTTP status " + status + " - reset connection)");
            }
            return conn;
        } else if (status >= 400 && status < 500) {
            if (status == 401 || status == 403) { // Unauthorized, forbidden
                throw new IOException("Could not get connection (HTTP status " + status + " - access denied)");
            }
            if (status == 429) { // Too many queries
                throw new IOException("Could not get connection (HTTP status " + status + " - too many queries, temporary issue)");
            }
            throw new IOException("Could not get connection (HTTP status " + status + ")");
        } else if (status >= 500 && status < 600) { // Server errors (usually temporary)
            throw new IOException("Could not get connection (HTTP status " + status + ")");
        }
        throw new IOException("Could not get connection (HTTP status " + status + ")");
    }

    public static @NonNull InputStream getInputStream(@NonNull URL url) throws IOException { return getInputStream(url, null, 5000, null, null, null, 20); }

    public static @NonNull InputStream getInputStream(@NonNull URL url, String method) throws IOException { return getInputStream(url, method, 5000, null, null, null, 20); }

    public static @NonNull InputStream getInputStream(@NonNull URL url, String method, int timeout) throws IOException { return getInputStream(url, method, timeout, null, null, null, 20); }

    public static @NonNull InputStream getInputStream(@NonNull URL url, String method, int timeout, String userAgent) throws IOException { return getInputStream(url, method, timeout, userAgent, null, null, 20); }

    public static @NonNull InputStream getInputStream(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers) throws IOException { return getInputStream(url, method, timeout, userAgent, headers, null, 20); }

    public static @NonNull InputStream getInputStream(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData) throws IOException { return getInputStream(url, method, timeout, userAgent, headers, postData, 20); }

    public static @NonNull InputStream getInputStream(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData, int maxRedirects) throws IOException {
        HttpURLConnection conn = getConnection(url, method, timeout, userAgent, headers, postData, maxRedirects);
        int status = conn.getResponseCode();

        if (status >= 400 && status < 600) {
            // 400-500 errors
            throw new IOException("Server returned status code " + status);
        }

        return conn.getInputStream();
    }

    public static @NonNull InputStream getInputStreamWithThrows(@NonNull URL url) throws IOException { return getInputStreamWithThrows(url, null, 5000, null, null, null, 20); }

    public static @NonNull InputStream getInputStreamWithThrows(@NonNull URL url, String method) throws IOException { return getInputStreamWithThrows(url, method, 5000, null, null, null, 20); }

    public static @NonNull InputStream getInputStreamWithThrows(@NonNull URL url, String method, int timeout) throws IOException { return getInputStreamWithThrows(url, method, timeout, null, null, null, 20); }

    public static @NonNull InputStream getInputStreamWithThrows(@NonNull URL url, String method, int timeout, String userAgent) throws IOException { return getInputStreamWithThrows(url, method, timeout, userAgent, null, null, 20); }

    public static @NonNull InputStream getInputStreamWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers) throws IOException { return getInputStreamWithThrows(url, method, timeout, userAgent, headers, null, 20); }

    public static @NonNull InputStream getInputStreamWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData) throws IOException { return getInputStreamWithThrows(url, method, timeout, userAgent, headers, postData, 20); }

    public static @NonNull InputStream getInputStreamWithThrows(@NonNull URL url, String method, int timeout, String userAgent, Map<String, String> headers, Map<String, String> postData, int maxRedirects) throws IOException {
        HttpURLConnection conn = getConnectionWithThrows(url, method, timeout, userAgent, headers, postData, maxRedirects);
        int status = conn.getResponseCode();

        if (status >= 400 && status < 600) {
            // 400-500 errors
            throw new IOException("Server returned status code " + status);
        }

        return conn.getInputStream();
    }

    public static @NonNull InputStream getInputStream(@NonNull HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();

        if (status >= 400 && status < 600) {
            // 400-500 errors
            throw new IOException("Server returned status code " + status);
        }

        return conn.getInputStream();
    }

    private static boolean hasKey(@NonNull Map<String, String> map, @NonNull String key) {
        for (String k : map.keySet()) {
            if (k.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }
}
