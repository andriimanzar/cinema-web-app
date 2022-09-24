package com.manzar.persistence;

import com.manzar.persistence.exception.DBException;
import org.postgresql.ds.PGConnectionPoolDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final PGConnectionPoolDataSource poolDataSource = new PGConnectionPoolDataSource();

    private DBConnection() {

    }

    static {
        Properties properties = new Properties();
        try {
            properties.load(DBConnection.class.getResourceAsStream("/db.properties"));
            poolDataSource.setURL(properties.getProperty("db.url"));
            poolDataSource.setUser(properties.getProperty("db.user"));
            poolDataSource.setPassword(properties.getProperty("db.password"));
        } catch (IOException e) {
            throw new DBException("Cannot connect to database", e);
        }
    }

    public static Connection getConnection() {
        try {
            return poolDataSource.getConnection();
        } catch (SQLException e) {
            throw new DBException("Cannot create connection to db", e);
        }
    }
}
