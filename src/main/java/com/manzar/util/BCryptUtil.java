package com.manzar.util;

import com.password4j.Hash;
import com.password4j.Password;

public class BCryptUtil {

    public static String encrypt(String input) {
        Hash hash = Password.hash(input).withBcrypt();
        return hash.getResult();
    }

    public static boolean comparePasswords(String userProvidedPassword, String hashFromDb) {
        return Password.check(userProvidedPassword, hashFromDb).withBcrypt();
    }
}

