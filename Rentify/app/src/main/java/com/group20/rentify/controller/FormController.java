package com.group20.rentify.controller;

import android.util.Patterns;

import com.google.common.hash.Hashing;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.Entity;
import com.group20.rentify.util.DatabaseCallBack;
import com.group20.rentify.util.DatabaseInterface;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class FormController {

    private static FormController instance;

    private FormController() {
        super();
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

    public boolean verifyLogin(String email, String password) {
        if (DatabaseInterface.getInstance().authenticateAccount(email, password)) {
            try {
                DatabaseInterface.getInstance().getAccount(
                        email,
                        new DatabaseCallBack() {
                            @Override
                            public void onEntityRetrieved(Entity entity) {
                                if (entity instanceof  Account) {
                                    Account.setSessionAccount((Account) entity);
                                } else {
                                    throw new NoSuchElementException("Account does not exist");
                                }
                            }
                        });

            } catch (NoSuchElementException e) {
                return false;
            }

            return true;
        }

        return false;
    }

    public String hashPassword(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
