package com.manzar.persistence.DAO;

public class SQLQueries {


    static class UserQuery {
        public static final String INSERT_USER_SQL = "INSERT INTO users" +
                "(first_name, last_name, email, phone_number, password) VALUES(?,?,?,?,?)";

        public static final String SELECT_ALL_USERS_SQL = "SELECT * from users";

        public static final String SELECT_USER_BY_ID_SQL = "SELECT * from users WHERE id = ?";

        public static final String UPDATE_USER_SQL = "UPDATE users SET first_name = ?, last_name = ?, " +
                "email = ?, phone_number = ?, password = ? WHERE id = ?";

        public static final String DELETE_USER_SQL = "DELETE from users WHERE id = ?";
    }


    static class MovieQuery {
        public static final String INSERT_MOVIE_SQL = "INSERT INTO movies" +
                "(title, genre, duration, director, release_year) VALUES(?,?,?,?,?)";

        public static final String SELECT_ALL_MOVIES_SQL = "SELECT * from movies";

        public static final String SELECT_MOVIE_BY_ID_SQL = "SELECT * from movies WHERE id = ?";

        public static final String UPDATE_MOVIE_SQL = "UPDATE movies SET title = ?, genre = ?, " +
                "duration = ?, director = ?, release_year = ?  WHERE id = ?";

        public static final String DELETE_MOVIE_SQL = "DELETE from movies WHERE id = ?";
    }
}
