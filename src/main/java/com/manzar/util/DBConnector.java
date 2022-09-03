package com.manzar.util;

import com.manzar.persistence.exception.DBException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/cinema_web_app_db?user=postgres&password=postgres&useSSL=false&serverTimeZone=UTC";

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException("Cannot create connection to db", e);
        }
    }

}
