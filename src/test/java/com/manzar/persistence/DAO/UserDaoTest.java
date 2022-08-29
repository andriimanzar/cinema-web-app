package com.manzar.persistence.DAO;

import com.manzar.persistence.entity.User;
import com.manzar.persistence.exception.DBException;
import com.password4j.Password;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoTest {

    private static UserDao userDao;

    @BeforeAll
    static void globalSetUp() {
        userDao = UserDaoImpl.getInstance();
    }


    @Test
    @Order(1)
    @DisplayName("Save a user")
    void saveUser() {
        User testUser = generateUser();

        int usersCountBeforeInsert = userDao.findAll().size();
        userDao.save(testUser);
        List<User> users = userDao.findAll();

        assertNotNull(testUser.getId());
        assertEquals(usersCountBeforeInsert + 1, users.size());
        assertTrue(users.contains(testUser));
    }

    @Test
    @Order(2)
    @DisplayName("Find all users")
    void findAllUsers() {
        List<User> previousUsers = userDao.findAll();
        List<User> newUsers = generateUsers();
        newUsers.forEach(userDao::save);
        int previousUsersAndNewUsersCount = previousUsers.size() + newUsers.size();
        List<User> allUsers = userDao.findAll();

        assertTrue(allUsers.containsAll(previousUsers));
        assertTrue(allUsers.containsAll(newUsers));
        assertEquals(previousUsersAndNewUsersCount, allUsers.size());

    }

    @Test
    @Order(3)
    @DisplayName("Find user by id")
    void findUserById() {
        User testUser = generateUser();

        userDao.save(testUser);

        User userFromDb = userDao.findUser(testUser.getId());

        assertEquals(testUser, userFromDb);
        assertEquals(testUser.getFirstName(), userFromDb.getFirstName());
        assertEquals(testUser.getLastName(), userFromDb.getLastName());
        assertEquals(testUser.getEmail(), userFromDb.getEmail());
        assertEquals(testUser.getPhoneNumber(), userFromDb.getPhoneNumber());
        assertEquals(testUser.getPassword(), userFromDb.getPassword());
    }

    @Test
    @Order(4)
    @DisplayName("findUser() throws an exception, when user ID is incorrect")
    void findByIncorrectId() {

        long id = -1L;

        assertThrows(DBException.class, () -> userDao.findUser(id));
    }

    @Test
    @Order(5)
    @DisplayName("Find user by email")
    void findUserByEmail() {
        User generatedUser = generateUser();
        String email = generatedUser.getEmail();
        userDao.save(generatedUser);
        User userFromDb = userDao.findUserByEmail(email);

        assertEquals(generatedUser, userFromDb);
    }

    @Test
    @Order(6)
    @DisplayName("Update a user")
    void updateUser() {
        User testUser = generateUser();

        userDao.save(testUser);
        List<User> usersBeforeUpdate = userDao.findAll();

        User generatedUser = generateUser();

        changeTargetUserFields(testUser, generatedUser);

        userDao.update(testUser);
        List<User> allUsers = userDao.findAll();
        User updatedUser = userDao.findUser(testUser.getId());

        assertEquals(usersBeforeUpdate.size(), allUsers.size());
        assertEquals(testUser, updatedUser);

    }

    @Test
    @Order(7)
    @DisplayName("update() throws an exception, when a user id is null")
    void updateNotStoredUser() {
        User notStoredUser = generateUser();

        assertThrows(DBException.class, () -> userDao.update(notStoredUser));
    }

    @Test
    @Order(8)
    @DisplayName("update() throws an exception, when a user id is invalid")
    void updateUserWithInvalidId() {
        User invalidIdUser = generateUser();

        invalidIdUser.setId(-1L);

        assertThrows(DBException.class, () -> userDao.update(invalidIdUser));

    }

    @Test
    @Order(9)
    @DisplayName("Remove a user")
    void removeUser() {
        User testUser = generateUser();

        userDao.save(testUser);

        List<User> usersBeforeRemove = userDao.findAll();
        userDao.remove(testUser.getId());

        List<User> usersAfterRemove = userDao.findAll();

        assertEquals(usersBeforeRemove.size() - 1, usersAfterRemove.size());
        assertFalse(usersAfterRemove.contains(testUser));
    }

    @Test
    @Order(10)
    @DisplayName("remove() throws an exception, when a user id is null")
    void removeNotStoredUser() {
        User notStoredUser = generateUser();

        assertThrows(DBException.class, () -> userDao.remove(notStoredUser.getId()));
    }

    @Test
    @Order(11)
    @DisplayName("remove() throws an exception, when a user id is invalid")
    void removeUserWithInvalidId() {
        User invalidIdUser = generateUser();

        invalidIdUser.setId(-1L);

        assertThrows(DBException.class, () -> userDao.remove(invalidIdUser.getId()));

    }

    private static User generateUser() {
        User generatedUser = new User();
        generatedUser.setFirstName(RandomStringUtils.randomAlphabetic(10));
        generatedUser.setLastName(RandomStringUtils.randomAlphabetic(10));
        generatedUser.setEmail(RandomStringUtils.randomAlphabetic(15) + "@gmail.com");
        generatedUser.setPhoneNumber("+" + RandomStringUtils.randomNumeric(10));
        String generatedPassword = RandomStringUtils.randomAlphabetic(32);
        generatedUser.setPassword(Password.hash(generatedPassword).withBcrypt().getResult());
        return generatedUser;
    }


    private List<User> generateUsers() {
        return Stream.generate(() -> new Random().nextInt(10)).limit(5).
                map(a -> generateUser()).collect(Collectors.toList());
    }

    private void changeTargetUserFields(User target, User source) {
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setEmail(source.getEmail());
        target.setPhoneNumber(source.getPhoneNumber());
    }
}
