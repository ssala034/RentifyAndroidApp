package com.group20.rentify.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Account implements Entity {

    //class variables
    protected static ArrayList<Account> accounts;
    // may remove this attribute and query the db instead
    // temporarily using an array list until the best data structure can be decided

    // instance variables
    private String name;
    private String username;
    private final String email;
    private final String role;  // this may be changed to a different type later

    // constructors

    /**
     * Constructor for creating entity from database representation
     */
    public Account(Map<String, Object> dbObj) {
        Object value = dbObj.get("username");
        if (value instanceof String) {
            username = (String) value;
        } else {
            throw new IllegalArgumentException();
        }

        value = dbObj.get("name");
        if (value instanceof String) {
            name = (String) value;
        } else {
            throw new IllegalArgumentException();
        }

        value = dbObj.get("role");
        if (value instanceof String) {
            role = (String) value;
        } else {
            throw new IllegalArgumentException();
        }

        value = dbObj.get("email");
        if (value instanceof String) {
            email = (String) value;
        } else {
            throw new IllegalArgumentException();
        }
    }

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

    // getters
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    // setters
    public boolean setUsername(String username) {
        // check if the username is already in use
        boolean validUsr = true;  // change this to an appropriate method call

        if (validUsr) {
            // modify the existing account in the db
            this.username = username;
        }

        return validUsr;
    }

    public void setName(String name) {
        // modify the existing account in the db
        this.name = name;
    }

    public Map<String, Object> getDatabaseRepresentation() {
        Map<String, Object> dbRep = new HashMap<>();

        dbRep.put("username", username);
        dbRep.put("name", name);
        dbRep.put("email", email);
        dbRep.put("role", role);

        return dbRep;
    }
}
