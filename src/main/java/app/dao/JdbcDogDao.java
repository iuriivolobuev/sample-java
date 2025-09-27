package app.dao;

import app.domain.Dog;
import app.domain.ObjectNotFoundException;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;

class JdbcDogDao implements DogDao {
    private final JdbcConnectionHolder connections;

    public JdbcDogDao(JdbcConnectionHolder connections) {
        this.connections = connections;
    }

    @Override
    public Collection<Dog> getAllDogs() {
        Collection<Dog> result = new ArrayList<>();
        Connection connection = connections.getCurrentConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from DOG");
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
        try (PreparedStatement statement = connection.prepareStatement("select * from DOG where ID = ?")) {
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
        try (PreparedStatement statement = connection.prepareStatement("insert into DOG values (?, ?, ?, ?, ?)")) {
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
                .prepareStatement("update DOG set NAME=?, TIME_OF_BIRTH=?, HEIGHT=?, WEIGHT=? where ID=?")) {
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
        try (PreparedStatement statement = connection.prepareStatement("delete from DOG where ID=?")) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Dog extractDogFromRecord(ResultSet rs) throws SQLException {
        return new Dog()
                .setId(rs.getString("ID"))
                .setName(rs.getString("NAME"))
                .setTimeOfBirth(fromTimestamp(rs.getTimestamp("TIME_OF_BIRTH")))
                .setHeight(rs.getDouble("HEIGHT"))
                .setWeight(rs.getDouble("WEIGHT"));
    }

    private static Timestamp toTimestamp(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null ? Timestamp.from(offsetDateTime.toInstant()) : null;
    }

    private static OffsetDateTime fromTimestamp(Timestamp timestamp) {
        return timestamp != null ? OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.UTC) : null;
    }
}
