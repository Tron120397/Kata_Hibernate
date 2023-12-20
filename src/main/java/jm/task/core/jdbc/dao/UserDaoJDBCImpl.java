package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS users ( 
                id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(32), 
                lastname VARCHAR(32),
                age TINYINT UNSIGNED);                
            """;
    private final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS users;";
    private final String SQL_SAVE_USER = """
            INSERT INTO users (name, lastname, age)
            VALUES (?, ?, ?);
            """;
    private final String SQL_DELETE_USER = "DELETE FROM users WHERE id = ?";
    private final String SQL_GET_ALL_USER = "SELECT id, name, lastname, age FROM users";
    private final String SQL_CLEAR_TABLE = "TRUNCATE TABLE users";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(SQL_DROP_TABLE);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_USER)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER)) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<User> getAllUsers() {
        try (Connection connection = Util.getConnection();
        Statement statement = connection.createStatement()) {
            List<User> listUsers = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_USER);

            while (resultSet.next()) {
                User newUser = new User();

                newUser.setId(resultSet.getLong("id"));
                newUser.setName(resultSet.getString("name"));
                newUser.setLastName(resultSet.getString("lastname"));
                newUser.setAge(resultSet.getByte("age"));

                listUsers.add(newUser);
            }

            return listUsers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(SQL_CLEAR_TABLE);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
