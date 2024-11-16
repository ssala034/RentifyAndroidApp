package com.group20.rentify.controller;

import android.util.Patterns;

import com.google.common.hash.Hashing;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.UserRole;
import com.group20.rentify.util.callback.AuthenticationCallback;

import java.nio.charset.StandardCharsets;

public class FormController {

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final boolean REQUIRE_NUMERIC = true;
    public static final boolean REQUIRE_UPPERCASE = true;
    public static final boolean REQUIRE_LOWERCASE = true;
    public static final boolean REQUIRE_SYMBOL = false;  // it would be best if this field was enabled but the admin pwd does not contain symbols
    public static final String SYMBOLS = "^$*.[]{}()?\"!@#%&/\\,><':;|_~";

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

    public boolean verifyUniqueEmail(String email) {
        return saveDataController.verifyUniqueEmail(email);
    }

    public boolean verifyPassword(String password1, String password2) {
        return password1 == null || password2 == null || password1.equals(password2);
    }

    public String verifyPasswordComplexity(String password) {
        boolean hasLower, hasUpper, hasNumber, hasSymbol;
        hasLower = hasUpper = hasNumber = hasSymbol = false;

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password must be at least 8 characters long";
        }

        for (char c : password.toCharArray()) {
            hasLower = hasLower || Character.isLowerCase(c);
            hasUpper = hasUpper || Character.isUpperCase(c);
            hasNumber = hasNumber || Character.isDigit(c);
            hasSymbol = hasSymbol || SYMBOLS.contains(String.valueOf(c));
        }

        if (REQUIRE_LOWERCASE && !hasLower) {
            return "Password must contain at least one lowercase letter";
        } else if (REQUIRE_UPPERCASE && !hasUpper) {
            return "Password must contain at least one uppercase letter";
        } else if (REQUIRE_NUMERIC && !hasNumber) {
            return "Password must contain at least one number [0-9]";
        } else if (REQUIRE_SYMBOL && !hasSymbol) {
            return "Password must contain one of " + SYMBOLS;
        }

        return "";
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
                                        Account.setSessionAccount(entity);
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

    public boolean createAccount(String username, String email, UserRole.Role role, String firstName, String lastName, String password)
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
