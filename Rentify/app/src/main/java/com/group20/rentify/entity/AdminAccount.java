package com.group20.rentify.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Map;

public class AdminAccount extends Account {

    /**
     * Constructor for creating entities from database representation.
     * @param dbObj The mapping from firebase
     */
    public AdminAccount(Map<String, Object> dbObj) {
        super(dbObj);

        Object role = dbObj.get("role");
        if (!(role instanceof String) || !role.equals("admin")) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Minimum argument constructor
     * @param username  The unique identifier for the administrator
     * @param email     The primary email of the administrator
     */
    public AdminAccount(String username, String email) {
        super(username, email, UserRole.admin);
    }

    /**
     * Full argument constructor
     * @param username  The unique identifier for the administrator
     * @param email     The primary email of the administrator
     * @param name      The display name of the administrator
     */
    public AdminAccount(String username, String email, String name) {
        super(username, email, UserRole.admin, name);
    }

    /**
     * Create an admin version of account.
     */
    public AdminAccount(@NonNull Account a) {
        super(a.getUsername(), a.getEmail(), UserRole.admin, a.getName());
    }

    /**
     * Return a list of the copies of all account's readable attributes.
     */
    public ArrayList<Account> getAllAccounts() {
        /*
        TODO Instead of keeping all accounts as a master list in memory,
            this method may be better implemented by getting the account list from the db.
        */
        throw new UnsupportedOperationException("This method has not been implemented");
    }

}