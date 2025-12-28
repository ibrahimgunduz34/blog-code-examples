package com.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ConfigurationModule());
        String datasourceUrl = injector.getInstance(Key.get(String.class, Names.named("datasource.url")));
        System.out.println(datasourceUrl);

        DatabaseTransactionStorage storage = injector.getInstance(DatabaseTransactionStorage.class);
    }
}