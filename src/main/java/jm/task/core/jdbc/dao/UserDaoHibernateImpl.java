package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.TransactionException;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final String SQL_CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS users ( 
                id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(32), 
                lastname VARCHAR(32),
                age TINYINT UNSIGNED);    
            """;
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS users";
    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();
        try (session) {
            session.beginTransaction();

            session.createSQLQuery(SQL_CREATE_TABLE).executeUpdate();

            session.getTransaction().commit();
        } catch (TransactionException exception) {
            System.out.println(exception.getMessage());
            session.getTransaction().rollback();
        }
    }


    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        try (session) {
            session.beginTransaction();

            session.createSQLQuery(SQL_DROP_TABLE).executeUpdate();

            session.getTransaction().commit();
        } catch (TransactionException exception) {
            System.out.println(exception.getMessage());
            session.getTransaction().rollback();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = sessionFactory.openSession();
        try (session) {
            session.beginTransaction();

            session.saveOrUpdate(user);

            session.getTransaction().commit();
            System.out.printf("User с именем – %s добавлен в базу данных.\n", name);
        } catch (TransactionException exception) {
            System.out.println(exception.getMessage());
            System.out.printf("User с именем – %s не добавлен в базу данных.\n", name);
            session.getTransaction().rollback();
        }

    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        try (session) {
            session.beginTransaction();

            User user = session.get(User.class, id);
            session.delete(user);

            session.getTransaction().commit();
        } catch (TransactionException exception) {
            System.out.println(exception.getMessage());
            session.getTransaction().rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        try (session) {

            session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).list();
            session.getTransaction().commit();

            return users;
        } catch (TransactionException exception) {
            System.out.println("Не удалось получить список users");
            System.out.println(exception.getMessage());
            session.getTransaction().rollback();
        }
        return Collections.emptyList();
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        try (session) {
            session.beginTransaction();

            session.createSQLQuery("delete from users").executeUpdate();
            session.createSQLQuery("alter table users auto_increment = 1").executeUpdate();

            session.getTransaction().commit();
        } catch (TransactionException exception) {
            System.out.println("Не удалось отчистить таблицу.");
            System.out.println(exception.getMessage());
            session.getTransaction().rollback();
        }
    }
}
