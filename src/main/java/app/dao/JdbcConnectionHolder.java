package app.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

class JdbcConnectionHolder {
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
}
