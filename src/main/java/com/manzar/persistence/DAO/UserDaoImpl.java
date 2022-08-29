package com.manzar.persistence.DAO;

import com.manzar.persistence.entity.User;
import com.manzar.persistence.exception.DBException;
import com.manzar.util.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.manzar.persistence.DAO.SQLQuery.UserQuery.*;

public class UserDaoImpl implements UserDao {

    private static UserDao userDao;

    private UserDaoImpl() {

    }

    public static synchronized UserDao getInstance() {
        if (userDao == null) {
            userDao = new UserDaoImpl();
        }
        return userDao;
    }

    @Override
    public void save(User user) {
        Objects.requireNonNull(user);
        try (Connection connection = DBConnector.getConnection()) {
            saveUser(user, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot save user: %s", user), e);
        }
    }

    private void saveUser(User user, Connection connection) throws SQLException {
        PreparedStatement insertStatement = prepareInsertStatement(user, connection);
        insertStatement.executeUpdate();
        Long generatedId = fetchGeneratedId(insertStatement);
        user.setId(generatedId);
    }

    private PreparedStatement prepareInsertStatement(User user, Connection connection) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            fillUserStatement(user, insertStatement);
            return insertStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare statement for user : %s", user), e);
        }

    }

    private Long fetchGeneratedId(PreparedStatement preparedStatement) throws SQLException {
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong("id");
        } else throw new DBException("Cannot fetch generated id");
    }

    private void fillUserStatement(User user, PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, user.getFirstName());
        insertStatement.setString(2, user.getLastName());
        insertStatement.setString(3, user.getEmail());
        insertStatement.setString(4, user.getPhoneNumber());
        insertStatement.setString(5, user.getPassword());
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = DBConnector.getConnection()) {
            return getAllUsers(connection);
        } catch (SQLException e) {
            throw new DBException("Cannot find all users");
        }
    }

    private List<User> getAllUsers(Connection connection) throws SQLException {
        Statement getAllStatement = connection.createStatement();
        ResultSet resultSet = getAllStatement.executeQuery(SELECT_ALL_USERS_SQL);
        return collectToList(resultSet);
    }

    private List<User> collectToList(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = parseRow(resultSet);
            users.add(user);
        }
        return users;
    }

    private User parseRow(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setId(resultSet.getLong("id"));
        user.setPassword(resultSet.getString("password"));
        return user;
    }


    @Override
    public User findUser(Long id) {
        Objects.requireNonNull(id);
        try (Connection connection = DBConnector.getConnection()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot find user with id: %d", id));
        }
    }

    private User findById(Long id, Connection connection) throws SQLException {
        PreparedStatement selectByIdStatement = prepareSelectByIdStatement(id, connection);
        ResultSet resultSet = selectByIdStatement.executeQuery();
        if (resultSet.next()) {
            return parseRow(resultSet);
        } else throw new DBException(String.format("User with id = %d does not exist", id));

    }

    private PreparedStatement prepareSelectByIdStatement(Long id, Connection connection) {
        try {
            PreparedStatement selectByIdStatement = connection.prepareStatement(SELECT_USER_BY_ID_SQL);
            selectByIdStatement.setLong(1, id);
            return selectByIdStatement;
        } catch (SQLException e) {
            throw new DBException("Cannot prepare select by id statement");
        }
    }

    @Override
    public User findUserByEmail(String email) {
        Objects.requireNonNull(email);
        try (Connection connection = DBConnector.getConnection()) {
            return findByEmail(email, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot find user with email: %s", email));
        }
    }

    private User findByEmail(String email, Connection connection) throws SQLException {
        PreparedStatement selectByEmailStatement = prepareSelectByEmailStatement(email, connection);
        ResultSet resultSet = selectByEmailStatement.executeQuery();
        if (resultSet.next()) {
            return parseRow(resultSet);
        } else throw new DBException(String.format("User with email = %s does not exist", email));

    }


    private PreparedStatement prepareSelectByEmailStatement(String email, Connection connection) {
        try {
            PreparedStatement selectByIdStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL_SQL);
            selectByIdStatement.setString(1, email);
            return selectByIdStatement;
        } catch (SQLException e) {
            throw new DBException("Cannot prepare select by email statement");
        }
    }


    @Override
    public void update(User user) {
        Objects.requireNonNull(user);
        try (Connection connection = DBConnector.getConnection()) {
            updateUser(user, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot update user: %s", user), e);
        }
    }

    private void updateUser(User user, Connection connection) throws SQLException {
        checkId(user.getId());
        PreparedStatement updateUserStatement = prepareUpdateStatement(user, connection);
        updateUserStatement.executeUpdate();
    }

    private PreparedStatement prepareUpdateStatement(User user, Connection connection) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_USER_SQL);
            fillUserStatement(user, updateStatement);
            updateStatement.setLong(6, user.getId());
            return updateStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare update statement for user: %s", user));
        }
    }

    @Override
    public void remove(Long id) {
        try (Connection connection = DBConnector.getConnection()) {
            removeUser(id, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot remove user with id: %d", id), e);
        }
    }

    private void removeUser(Long id, Connection connection) throws SQLException {
        checkId(id);
        PreparedStatement removeStatement = prepareRemoveStatement(id, connection);
        removeStatement.executeUpdate();
    }

    private PreparedStatement prepareRemoveStatement(Long id, Connection connection) {
        try {
            PreparedStatement removeStatement = connection.prepareStatement(DELETE_USER_SQL);
            removeStatement.setLong(1, id);
            return removeStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare remove statement for user with id: %d", id), e);
        }
    }

    private void checkId(Long id) {
        if (id == null || id < 1L) {
            throw new DBException("User id cannot be null or less than 1");
        }
    }
}
