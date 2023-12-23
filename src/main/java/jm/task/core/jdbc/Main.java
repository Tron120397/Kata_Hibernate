package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        User[] arraUser = new User[]{
                new User("Egor", "Ugrinov", (byte) 32),
                new User("Sveta", "Sokolova", (byte) 18),
                new User("Ivan", "Ivanov", (byte) 2),
                new User("Petr", "Petrov", (byte) 45),
        };

        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        for (User user : arraUser) {
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());


        }

        userService.getAllUsers().forEach(System.out::println);

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }


}

