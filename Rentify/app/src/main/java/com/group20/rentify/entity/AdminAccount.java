package com.group20.rentify.entity;

import java.util.ArrayList;

public class AdminAccount extends Account {

    /**Return the account readable attributes.
     * Instead of keeping all accounts as a master list in memory,
     * this method may be better implemented by getting the account list from the db.
     */
    public ArrayList<Account> getAllAccounts() {
        throw new UnsupportedOperationException("This method has not been implemented");
    }

}
