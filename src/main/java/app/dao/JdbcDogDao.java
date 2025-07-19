package app.dao;

import app.domain.Dog;
import app.domain.ObjectNotFoundException;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;

class JdbcDogDao implements DogDao {
    private static final String CREATE_TABLES_DDL = """
            create table dog (
              id varchar(36) primary key,
              name nvarchar(1000) not null,
              time_of_birth timestamp,
              height double,
              weight double
            );
            """;

    private final JdbcConnectionHolder connections;

    public JdbcDogDao(JdbcConnectionHolder connections) {
        this.connections = connections;
    }

    @Override
    public Collection<Dog> getAllDogs() {
        Collection<Dog> result = new ArrayList<>();
        Connection connection = connections.getCurrentConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from dog");
            while (rs.next()) {
                result.add(extractDogFromRecord(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Dog getDog(String id) {
        Connection connection = connections.getCurrentConnection();
        try (PreparedStatement statement = connection.prepareStatement("select * from dog where id = ?")) {
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return extractDogFromRecord(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new ObjectNotFoundException(Dog.class, id);
    }

    @Override
    public Dog createDog(Dog dog) {
        Connection connection = connections.getCurrentConnection();
        try (PreparedStatement statement = connection.prepareStatement("insert into dog values (?, ?, ?, ?, ?)")) {
            statement.setString(1, dog.getId());
            statement.setString(2, dog.getName());
            statement.setTimestamp(3, toTimestamp(dog.getTimeOfBirth()));
            statement.setDouble(4, dog.getHeight());
            statement.setDouble(5, dog.getWeight());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dog;
    }

    @Override
    public Dog updateDog(Dog dog) {
        Connection connection = connections.getCurrentConnection();
        try (PreparedStatement statement = connection
                .prepareStatement("update dog set name=?, time_of_birth=?, height=?, weight=? where id=?")) {
            statement.setString(1, dog.getName());
            statement.setTimestamp(2, toTimestamp(dog.getTimeOfBirth()));
            statement.setDouble(3, dog.getHeight());
            statement.setDouble(4, dog.getWeight());
            statement.setString(5, dog.getId());
            if (statement.executeUpdate() == 0) {
                throw new ObjectNotFoundException(Dog.class, dog.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dog;
    }

    @Override
    public boolean deleteDog(String id) {
        Connection connection = connections.getCurrentConnection();
        try (PreparedStatement statement = connection.prepareStatement("delete from dog where id=?")) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused"/*invoked by spring as init method*/)
    void createTables() {
        Connection connection = connections.getCurrentConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLES_DDL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Dog extractDogFromRecord(ResultSet rs) throws SQLException {
        return new Dog()
                .setId(rs.getString("id"))
                .setName(rs.getString("name"))
                .setTimeOfBirth(fromTimestamp(rs.getTimestamp("time_of_birth")))
                .setHeight(rs.getDouble("height"))
                .setWeight(rs.getDouble("weight"));
    }

    private static Timestamp toTimestamp(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null ? Timestamp.from(offsetDateTime.toInstant()) : null;
    }

    private static OffsetDateTime fromTimestamp(Timestamp timestamp) {
        return timestamp != null ? OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.UTC) : null;
    }
}
