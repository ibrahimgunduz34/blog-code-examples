package com.example;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Logger.class)
                .annotatedWith(Names.named("appLogger"))
                .toInstance(LoggerFactory.getLogger("application"));

        bind(Logger.class)
                .annotatedWith(Names.named("auditLogger"))
                .toInstance(LoggerFactory.getLogger("audit"));
    }
}
