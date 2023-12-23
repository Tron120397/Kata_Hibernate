package jm.task.core.jdbc.util;

import java.sql.*;

/*
Мало ли, сделал через свойства.
*/
public final class Util {
    private static Connection connection;

    private static final String PROPERTIES_URL = PropertiesUtil.get("db.url");
    private static final String PROPERTIES_HOST = PropertiesUtil.get("db.host");
    private static final String PROPERTIES_PORT = PropertiesUtil.get("db.port");
    private static final String PROPERTIES_NAME_DB = PropertiesUtil.get("db.name_db");
    private static final String PROPERTIES_USER_NAME = PropertiesUtil.get("db.user_name");
    private static final String PROPERTIES_PASSWORD = PropertiesUtil.get("db.password");

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = PROPERTIES_URL + PROPERTIES_HOST + PROPERTIES_PORT + PROPERTIES_NAME_DB;
                connection = DriverManager.getConnection(url, PROPERTIES_USER_NAME, PROPERTIES_PASSWORD);
            }
        } catch (SQLInvalidAuthorizationSpecException exception) {
            System.out.println("Error getting connection!");
        } catch (SQLClientInfoException exception) {
            System.out.println("Not valid parameters for connection!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return connection;
    }

    /*
    Как я понял загружать драйвер вручную в память считается хорошим тоном.
    */
    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage() + " <- Not found!");
            System.exit(1);
        }
    }

}
