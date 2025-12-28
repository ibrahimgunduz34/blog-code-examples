package com.example;

import com.google.inject.Provider;

import java.net.http.HttpClient;
import java.time.Duration;

public class HttpClientProvider implements Provider<HttpClient> {
    private final static int CONNECTION_TIMEOUT_IN_MS = 10_000;
    @Override
    public HttpClient get() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(CONNECTION_TIMEOUT_IN_MS))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }
}
