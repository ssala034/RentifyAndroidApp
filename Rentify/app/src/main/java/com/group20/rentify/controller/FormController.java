package com.group20.rentify.controller;

import android.util.Patterns;

import com.google.common.hash.Hashing;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.UserRole;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class FormController {

    private static FormController instance;
    private SaveDataController saveDataController;

    private FormController() {
        saveDataController = SaveDataController.getInstance();
    }

    public static FormController getInstance() {
        if (instance == null) {
            instance = new FormController();
        }

        return instance;
    }

    public boolean verifyEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean verifyPassword(String password1, String password2) {
        return password1 == null || password2 == null || password1.equals(password2);
    }

    public boolean verifyUsername(String username) {
        return saveDataController.verifyUniqueUsername(username);
    }

    public boolean verifyLogin(String email, String password) {
        try {
            saveDataController.authenticate(email, password,
                    success -> SaveDataController.getInstance().getAccount(email,
                            entity -> {
                                if (entity instanceof Account) {
                                    Account.setSessionAccount((Account) entity);
                                } else {
                                    throw new NoSuchElementException("Account does not exist");
                                }
                            }));

        } catch (NoSuchElementException e) {
            return false;
        }

        return true;
    }

    public boolean createAccount(String username, String email, UserRole role, String firstName, String lastName)
        throws IllegalStateException {
        Account createdAccount = new Account(username, email, role, firstName, lastName);
        return saveDataController.saveAccount(createdAccount);
    }

    public String hashPassword(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
