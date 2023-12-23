package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;
import org.hibernate.query.NativeQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS users ( 
                id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(32), 
                lastname VARCHAR(32),
                age TINYINT UNSIGNED);                
            """;
    private final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS users;";
    private final SessionFactory SESSION_FACTORY;

    public UserDaoHibernateImpl() {
        SESSION_FACTORY = Util.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
        try (Connection connection = Util.getConnectionJDBC(); Statement statement = connection.createStatement()) {

            statement.execute(SQL_CREATE_TABLE);

        } catch (SQLWarning exception) {
            System.out.println("Error in database: " + exception.getMessage());
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnectionJDBC(); Statement statement = connection.createStatement()) {

            statement.execute(SQL_DROP_TABLE);

        } catch (SQLWarning exception) {
            System.out.println("Error in database: " + exception.getMessage());
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);

        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            session.saveOrUpdate(user);

            session.getTransaction().commit();
            System.out.printf("User с именем – %s добавлен в базу данных.\n", name);
        } catch (TransactionException exception) {
            System.out.println(exception.getMessage());
            System.out.printf("User с именем – %s не добавлен в базу данных.\n", name);
        }

    }

    @Override
    public void removeUserById(long id) {
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            session.delete(session.get(User.class, id));

            session.getTransaction().commit();
        } catch (TransactionException exception) {
            System.out.println(exception.getMessage());
            System.out.printf("User с id %s не удалось удалить", id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).list();
            session.getTransaction().commit();

            return users;
        } catch (TransactionException exception) {
            System.out.println("Не удалось получить список users");
            System.out.println(exception.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();

            String SQLQuery = "DELETE FROM users";
            session.createSQLQuery(SQLQuery).executeUpdate();

            session.getTransaction().commit();
        } catch (TransactionException exception) {
            System.out.println("Не удалось отчистить таблицу.");
            System.out.println(exception.getMessage());
        }
    }
}
