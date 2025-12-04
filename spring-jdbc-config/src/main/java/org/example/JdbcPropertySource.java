package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class JdbcPropertySource extends EnumerablePropertySource<DataSource> {
    private static final String GET_PROPERTY_KEYS_QUERY = "SELECT key FROM Configurations WHERE profile IN (?)";
    private static final String GET_PROPERTY_VALUE_QUERY = "SELECT value FROM Configurations WHERE key=? AND profile IN (?)";
    private final String[] activeProfiles;

    private static final Logger logger = LoggerFactory.getLogger(JdbcPropertySource.class);

    public JdbcPropertySource(String name, DataSource source, Environment environment) {
        super(name, source);
        this.activeProfiles = environment.getActiveProfiles().length == 0 ?
                environment.getDefaultProfiles() :
                environment.getActiveProfiles();
    }

    @Override
    public String[] getPropertyNames() {
        return executeQuery(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(GET_PROPERTY_KEYS_QUERY);

                    preparedStatement.setString(1, String.join(",", activeProfiles));

                    return preparedStatement;
                },
                resultSet -> {
                    ArrayList<String> keys = new ArrayList<>();
                    while (resultSet.next()) {
                        keys.add(resultSet.getString("key"));
                    }
                    return keys.toArray(new String[0]);
                }
        );
    }

    @Override
    public Object getProperty(String name) {
        return executeQuery(
                connection -> {
                    ;
                    var preparedStatement = connection.prepareStatement(GET_PROPERTY_VALUE_QUERY);

                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, String.join(",", activeProfiles));

                    return preparedStatement;
                },
                resultSet -> {
                    if (resultSet.next()) {
                        return resultSet.getString(1);
                    }
                    return null;
                }
        );
    }

    private <T> T executeQuery(
            ThrowingFunction<Connection, PreparedStatement> prepareStatementFunction,
            ThrowingFunction<ResultSet, T> resultSetFunction
    ) {
        try (
                Connection connection = getSource().getConnection();
                PreparedStatement preparedStatement = prepareStatementFunction.apply(connection);
        ) {
            logger.info("Executing query {}", preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetFunction.apply(resultSet);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private interface ThrowingFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
