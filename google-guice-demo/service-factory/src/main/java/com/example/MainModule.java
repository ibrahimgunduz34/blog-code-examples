package com.example;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import java.net.http.HttpClient;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HttpClient.class)
                .toProvider(HttpClientProvider.class)
                .in(Scopes.SINGLETON);
    }
}
