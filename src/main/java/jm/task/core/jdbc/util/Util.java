package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    /*
    Мало ли, сделал через свойства.
    */
public final class Util {
    private static final String PROPERTIES_URL = PropertiesUtil.get("db.url");
    private static final String PROPERTIES_HOST = PropertiesUtil.get("db.host");
    private static final String PROPERTIES_PORT = PropertiesUtil.get("db.port");
    private static final String PROPERTIES_NAME_DB = PropertiesUtil.get("db.name_db");
    private static final String PROPERTIES_USER_NAME = PropertiesUtil.get("db.user_name");
    private static final String PROPERTIES_PASSWORD = PropertiesUtil.get("db.password");

    static {
        loadDriver();
    }

    public static Connection getConnection() throws SQLException {
            String url = PROPERTIES_URL + PROPERTIES_HOST + PROPERTIES_PORT + PROPERTIES_NAME_DB;

            return DriverManager.getConnection(url,PROPERTIES_USER_NAME, PROPERTIES_PASSWORD);

    }

    /*
    Как я понял загружать драйвер вручную в память считается хорошим тоном.
    */
    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
