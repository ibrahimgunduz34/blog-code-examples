package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.sql.DataSource;

public class JdbcPropertySourcePostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Binder binder = Binder.get(environment);

        DataSourceProperties dataSourceProperties = binder.bind("spring.datasource", Bindable.of(DataSourceProperties.class))
                .orElse(new DataSourceProperties());

        DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().build();

        JdbcPropertySource jdbcPropertySource = new JdbcPropertySource("jdbc-property-source", dataSource, environment);
        environment.getPropertySources().addFirst(jdbcPropertySource);
    }
}
