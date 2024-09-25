package com.group20.rentify.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Account implements Entity {

    //class variables
    protected static ArrayList<Account> accounts;
    // may remove this attribute and query the db instead
    // temporarily using an array list until the best data structure can be decided

    // instance variables
    private String name;
    private String username;
    private String email;
    private String role;  // this may be changed to a different type later

    // constructors

    /**
     * Minimum argument constructor.
     * @param username  The unique identifier for the user
     * @param email     The primary email of the user
     * @param role      One of {admin, renter, lessor}
     */
    public Account(String username, String email, String role) {
        this(username, email, role, "Anonymous");
    }

    /**
     * Full argument constructor.
     * @param username  The unique identifier for the user
     * @param email     The primary email of the user
     * @param role      One of {admin, renter, lessor}
     * @param name      The name or nickname of the user
     */
    public Account(String username, String email, String role, String name) {
        this.username = username;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    /**
     * Copy constructor
     */
    public Account(@NonNull Account a) {
        this.username = a.username;
        this.role = a.role;
        this.name = a.name;
        this.email = a.email;
    }
}
