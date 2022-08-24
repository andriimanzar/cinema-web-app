package com.manzar.util;

import com.manzar.persistence.exception.DBException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {

    public static final String SETTINGS_FILE = "application.properties";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl());
        } catch (SQLException | IOException e) {
            throw new DBException("Cannot create connection to db", e);
        }
    }

    private static String dbUrl() throws IOException {
        try (FileInputStream fileReader = new FileInputStream(SETTINGS_FILE)) {
            Properties urlProperty = new Properties();
            urlProperty.load(fileReader);
            return urlProperty.getProperty("db.url");
        }
    }
}
