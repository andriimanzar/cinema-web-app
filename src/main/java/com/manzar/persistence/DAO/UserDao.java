package com.manzar.persistence.DAO;

import com.manzar.persistence.entity.User;

import java.util.List;

public interface UserDao {

    void save(User user);

    List<User> findAll();

    User findUser(Long id);

    void update(User user);

    void remove(Long id);
}
