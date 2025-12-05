package org.example;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;

public class JdbcPropertySource extends EnumerablePropertySource<JdbcDataSource> {
    private final String[] profiles;

    public JdbcPropertySource(String name, JdbcDataSource source, ConfigurableEnvironment environment) {
        super(name, source);

        profiles = environment.getActiveProfiles().length == 0 ?
                environment.getDefaultProfiles() :
                environment.getActiveProfiles();

        source.reload();

        logger.info(String.format("%s properties have been loaded for profiles: %s through JDBC",
                getSource().getPropertyNames().length,
                String.join(",", profiles)));
    }

    @Override
    public Object getProperty(String name) {
        return getSource().getProperty(profiles, name);
    }

    @Override
    public String[] getPropertyNames() {
        return getSource().getPropertyNames();
    }
}
