package com.manzar.persistence.DAO;

public class SQLQueries {

    public static final String INSERT_USER_SQL = "INSERT INTO users(first_name, last_name, email, phone_number)" +
            "VALUES(?,?,?,?)";

    public static final String SELECT_ALL_USERS_SQL = "SELECT * from users";

    public static final String SELECT_USER_BY_ID_SQL = "SELECT * from users WHERE id = ?";

    public static final String UPDATE_USER_SQL = "UPDATE users SET first_name = ?, last_name = ?, " +
            "email = ?, phone_number = ? WHERE id = ?";

    public static final String DELETE_USER_SQL = "DELETE from users WHERE id = ?";
}
