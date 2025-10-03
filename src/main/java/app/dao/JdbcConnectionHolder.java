package app.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcConnectionHolder {
    private final DataSource dataSource;
    private final ThreadLocal<Connection> connections = new ThreadLocal<>();

    public JdbcConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getCurrentConnection() {
        Connection connection = connections.get();
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connections.set(connection);
        }
        return connection;
    }

    public void closeCurrentConnection() {
        Connection connection = connections.get();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connections.remove();
    }

    public Connection beginTransaction() {
        Connection connection = getCurrentConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            closeCurrentConnection();
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void commit() {
        try {
            getCurrentConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        try {
            getCurrentConnection().rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
