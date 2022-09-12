package com.manzar.persistence.dao;

import com.manzar.persistence.entity.User;

import java.util.List;

public interface UserDao {

    void save(User user);

    List<User> findAll();

    User findUser(Long id);

    User findUserByEmail(String email);

    void update(User user);

    void remove(Long id);

}
