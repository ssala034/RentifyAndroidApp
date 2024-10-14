package com.group20.rentify.controller;

import android.util.Patterns;

import com.google.common.hash.Hashing;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.UserRole;
import com.group20.rentify.util.callback.AuthenticationCallback;

import java.nio.charset.StandardCharsets;

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

    public void verifyLogin(String email, String password, AuthenticationCallback callback) {
        saveDataController.authenticate(email, password,
                success -> {
                    if (success) {
                        saveDataController.getAccount(email,
                                entity -> {
                                    if (entity instanceof Account) {
                                        Account.setSessionAccount((Account) entity);
                                        callback.onAuthenticationCompleted(true);
                                    } else {
                                        callback.onAuthenticationCompleted(false);
                                    }
                                });
                    } else {
                        callback.onAuthenticationCompleted(false);
                    }
                });
    }

    public boolean createAccount(String username, String email, UserRole role, String firstName, String lastName, String password)
        throws IllegalStateException {
        Account createdAccount = new Account(username, email, role, firstName, lastName);
        boolean success = saveDataController.saveAccount(createdAccount);
        if (success) {
            saveDataController.addToAuth(email, password);
            Account.setSessionAccount(createdAccount);
        }
        return success;
    }

    public String hashPassword(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
