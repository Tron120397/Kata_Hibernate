package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
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

    public void createUsersTable() {
        try (Connection connection = Util.getConnectionJDBC();
             Statement statement = connection.createStatement()) {

            statement.execute(SQL_CREATE_TABLE);

        } catch (SQLWarning exception) {
            System.out.println("Error in database: " + exception.getMessage());
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnectionJDBC();
             Statement statement = connection.createStatement()) {

            statement.execute(SQL_DROP_TABLE);

        } catch (SQLWarning exception) {
            System.out.println("Error in database: " + exception.getMessage());
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnectionJDBC();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_USER)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();
            System.out.printf("User с именем – %s добавлен в базу данных.\n", name);

        } catch (SQLSyntaxErrorException exception) {
            System.out.println("Error : " + exception.getMessage());
            System.out.printf("Пользователь с именем %s не добавлен в базу данных.", name);
        } catch (SQLDataException exception) {
            System.out.println("Error in database: " + exception.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.printf("Пользователь с именем %s не добавлен в базу данных по причинe: ", name);
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnectionJDBC();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLWarning exception) {
            System.out.println("Error in database: " + exception.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<User> getAllUsers() {
        try (Connection connection = Util.getConnectionJDBC();
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
        } catch (SQLTimeoutException exception) {
            System.out.println("Error while getting manu users a long time: " + exception.getMessage());
        } catch (SQLWarning exception) {
            System.out.println("Error in database: " + exception.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Collections.emptyList();
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnectionJDBC();
             Statement statement = connection.createStatement()) {

            statement.execute(SQL_CLEAR_TABLE);

        } catch (SQLWarning exception) {
            System.out.println("Error in database: " + exception.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
