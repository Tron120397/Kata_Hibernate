package jm.task.core.jdbc.util;


import jm.task.core.jdbc.model.User;
import org.hibernate.PropertyAccessException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.internal.util.config.ConfigurationException;
import org.hibernate.service.ServiceRegistry;

import java.sql.*;
import java.util.Properties;

public final class Util {
    private static Connection connectionJDBC;
    private static Configuration configurationHibernate;

    private static final String PROPERTIES_URL = PropertiesUtil.get("db.url");
    private static final String PROPERTIES_HOST = PropertiesUtil.get("db.host");
    private static final String PROPERTIES_PORT = PropertiesUtil.get("db.port");
    private static final String PROPERTIES_NAME_DB = PropertiesUtil.get("db.name_db");
    private static final String PROPERTIES_USER_NAME = PropertiesUtil.get("db.user_name");
    private static final String PROPERTIES_PASSWORD = PropertiesUtil.get("db.password");
    private static final String URL = PROPERTIES_URL + PROPERTIES_HOST + PROPERTIES_PORT + PROPERTIES_NAME_DB;


    public static SessionFactory getSessionFactory() throws NullPointerException{


        if (configurationHibernate == null) {
            try {
                configurationHibernate = new Configuration();
                Properties properties = new Properties();
                properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
                properties.put(Environment.URL, URL);
                properties.put(Environment.USER, PROPERTIES_USER_NAME);
                properties.put(Environment.PASS, PROPERTIES_PASSWORD);
                properties.put(Environment.PHYSICAL_NAMING_STRATEGY, CamelCaseToUnderscoresNamingStrategy.class.getName());
                configurationHibernate.setProperties(properties);
                configurationHibernate.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configurationHibernate.getProperties()).build();

                return configurationHibernate.buildSessionFactory(serviceRegistry);
            } catch (PropertyAccessException exception) {
                System.out.println("Incorrect properties for Hibernate configuration!");
                System.exit(1);
            }
        }
        return configurationHibernate.buildSessionFactory();
    }


    public static Connection getConnectionJDBC() {
        try {
            if (connectionJDBC == null || connectionJDBC.isClosed()) {
                connectionJDBC = DriverManager.getConnection(URL, PROPERTIES_USER_NAME, PROPERTIES_PASSWORD);
            }
        } catch (SQLInvalidAuthorizationSpecException exception) {
            System.out.println("Error getting connection!");
        } catch (SQLClientInfoException exception) {
            System.out.println("Not valid parameters for connection!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return connectionJDBC;
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
