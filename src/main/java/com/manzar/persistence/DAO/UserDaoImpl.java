package com.manzar.persistence.DAO;

import com.manzar.persistence.entity.User;
import com.manzar.persistence.exception.DBException;
import com.manzar.util.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.manzar.persistence.DAO.SQLQueries.*;

public class UserDaoImpl implements UserDao {

    private static UserDaoImpl userDao;

    private UserDaoImpl() {

    }

    public static synchronized UserDaoImpl getInstance() {
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
            PreparedStatement insertStatement = connection.prepareStatement(INSERT_USER_SQL,
                    Statement.RETURN_GENERATED_KEYS);
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
    public void update(User user) {
        Objects.requireNonNull(user);
        try (Connection connection = DBConnector.getConnection()) {
            updateUser(user, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot update user: %s", user), e);
        }
    }

    private void updateUser(User user, Connection connection) throws SQLException {
        checkIdIsNotNull(user);
        PreparedStatement updateUserStatement = prepareUpdateStatement(user, connection);
        updateUserStatement.executeUpdate();
    }

    private PreparedStatement prepareUpdateStatement(User user, Connection connection) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_USER_SQL);
            fillUserStatement(user, updateStatement);
            updateStatement.setLong(5, user.getId());
            return updateStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare update statement for user: %s", user));
        }
    }

    @Override
    public void remove(User user) {
        Objects.requireNonNull(user);
        try (Connection connection = DBConnector.getConnection()) {
            removeUser(user, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot remove user: %s", user), e);
        }
    }

    private void removeUser(User user, Connection connection) throws SQLException {
        checkIdIsNotNull(user);
        PreparedStatement removeStatement = prepareRemoveStatement(user, connection);
        removeStatement.executeUpdate();
    }

    private PreparedStatement prepareRemoveStatement(User user, Connection connection) {
        try {
            PreparedStatement removeStatement = connection.prepareStatement(DELETE_USER_SQL);
            removeStatement.setLong(1, user.getId());
            return removeStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare remove statement for user: %s", user), e);
        }
    }

    private void checkIdIsNotNull(User user) {
        if (user.getId() == null) {
            throw new DBException("User id cannot be null");
        }
    }
}
