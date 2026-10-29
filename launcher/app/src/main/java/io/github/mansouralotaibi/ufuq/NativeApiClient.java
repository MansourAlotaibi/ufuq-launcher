package io.github.mansouralotaibi.ufuq;

import android.os.Handler;
import android.os.Looper;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/** One bounded HTTPS worker. Uses Android's normal certificate and hostname verification. */
class NativeApiClient {
    private static final String TAG = "MansourApi";
    static final class Result {
        final String body, error;
        final int httpCode;
        Result(String body, int httpCode, String error) {
            this.body = body; this.httpCode = httpCode; this.error = error;
        }
        boolean successful() { return httpCode == 200 && body != null; }
    }
    interface Callback { void complete(Result result); }
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());
    private volatile boolean active;

    void setActive(boolean value) { active = value; }

    void get(final String url, final Callback callback) {
        worker.execute(() -> {
            String body = null, errorName = null;
            int httpCode = -1;
            HttpURLConnection connection = null;
            try {
                URL parsed = new URL(url);
                if (!"https".equalsIgnoreCase(parsed.getProtocol())) throw new SecurityException("HTTPS required");
                connection = (HttpURLConnection) parsed.openConnection();
                if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 21 && connection instanceof HttpsURLConnection) {
                    SSLSocketFactory platform = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    ((HttpsURLConnection) connection).setSSLSocketFactory(new Tls12SocketFactory(platform));
                }
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "Ufuq/1.0.0-rc2 Android-TV");
                httpCode = connection.getResponseCode();
                if (httpCode == 200) {
                    BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = in.read(buffer)) != -1 && out.size() < 262144) out.write(buffer, 0, read);
                    in.close();
                    body = new String(out.toByteArray(), StandardCharsets.UTF_8);
                }
            } catch (Exception error) {
                // The caller deliberately keeps the last successful cache.
                errorName = error.getClass().getSimpleName();
                Log.w(TAG, parsedHost(url) + " error=" + errorName);
            } finally {
                if (connection != null) connection.disconnect();
            }
            final Result result = new Result(body, httpCode, errorName);
            Log.i(TAG, parsedHost(url) + " http=" + httpCode + " bytes="
                    + (body == null ? 0 : body.length()));
            // Always deliver a completed request. A brief Activity recreation while leaving settings must not
            // discard verified data; the controller decides whether it is still safe to repaint its views.
            main.post(() -> callback.complete(result));
        });
    }

    void close() { active = false; worker.shutdownNow(); }

    private static String parsedHost(String value) {
        try { return new URL(value).getHost(); } catch (Exception ignored) { return "invalid-host"; }
    }
}
