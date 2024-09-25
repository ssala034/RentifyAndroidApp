package com.group20.rentify.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class AdminAccount extends Account {

    public AdminAccount(String username, String email) {
        super(username, email, "admin");
    }

    public AdminAccount(String username, String email, String name) {
        super(username, email, "admin", name);
    }

    /**
     * Create an admin version of account.
     */
    public AdminAccount(@NonNull Account a) {
        super(a.getUsername(), a.getEmail(), "admin", a.getName());
    }

    /**Return the account readable attributes.
     * Instead of keeping all accounts as a master list in memory,
     * this method may be better implemented by getting the account list from the db.
     */
    public ArrayList<Account> getAllAccounts() {
        throw new UnsupportedOperationException("This method has not been implemented");
    }

}
