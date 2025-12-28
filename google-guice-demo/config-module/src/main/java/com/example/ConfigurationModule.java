package com.example;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationModule extends AbstractModule {
    private static final String CONFIG_FILE = "application.properties";

    @Override
    protected void configure() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream configFileStream = classLoader.getResourceAsStream(CONFIG_FILE);

        if (configFileStream == null) {
            throw new RuntimeException("Could not find " + CONFIG_FILE + " on the classpath");
        }

        try {
            Properties properties = new Properties();
            properties.load(configFileStream);
            Names.bindProperties(binder(), properties);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from " + CONFIG_FILE, e);
        }
    }
}
