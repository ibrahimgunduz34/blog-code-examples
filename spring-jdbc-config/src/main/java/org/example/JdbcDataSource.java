package org.example;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class JdbcDataSource {
    private final DataSource dataSource;

    // Map<profile, Map<key, value>>
    private final Map<String, Map<String, String>> properties;

    public JdbcDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        properties = new LinkedHashMap<>();
    }

    public String getProperty(String[] profiles, String key) {
        String value = null;
        for (String profile : profiles) {
            Map<String, String> profileProperties = properties.get(profile);
            if (profileProperties != null && profileProperties.containsKey(key)) {
                value = profileProperties.get(key);
            }
        }

        return value;
    }

    public String[] getPropertyNames() {
        Set<String> keys = new LinkedHashSet<>();
        for (Map<String, String> profileProperties : properties.values()) {
            keys.addAll(profileProperties.keySet());
        }
        return keys.toArray(new String[0]);
    }

    public void reload() {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT key, value, profile FROM Configurations");
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            properties.clear();
            while (resultSet.next()) {
                String profile = resultSet.getString("profile");
                String key = resultSet.getString("key");
                String value = resultSet.getString("value");

                properties
                        .computeIfAbsent(profile, p -> new HashMap<>())
                        .put(key, value);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
