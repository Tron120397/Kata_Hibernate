package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    private static final UserDaoHibernateImpl USER_DAO_HIBERNATE;

    static {
        USER_DAO_HIBERNATE = new UserDaoHibernateImpl();
    }


    public void createUsersTable() {
        USER_DAO_HIBERNATE.createUsersTable();
    }

    public void dropUsersTable() {
        USER_DAO_HIBERNATE.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        USER_DAO_HIBERNATE.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        USER_DAO_HIBERNATE.removeUserById(id);
    }

    public List<User> getAllUsers() {
        return USER_DAO_HIBERNATE.getAllUsers();
    }

    public void cleanUsersTable() {
        USER_DAO_HIBERNATE.cleanUsersTable();
    }
}
