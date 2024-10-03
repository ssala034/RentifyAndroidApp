package com.group20.rentify.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A model class for user accounts of the Rentify app.
 */
public class Account implements Entity {

    //class variables
    protected static ArrayList<Account> accounts;
    // may remove this attribute and query the db instead
    // temporarily using an array list until the best data structure can be decided

    // instance variables
    /**
     * The user's profile display name.
     * <p>
     *     If not provided, the default behaviour is to
     *     use the user's username for the name.
     * </p>
     */
    private String name;

    /**
     * The user's unique username which can be used for sign in.
     */
    private String username;

    /**
     * The user's associated email address.
     * <p>Currently READ ONLY.</p>
     */
    private final String email;

    /**
     * The user's role in the system.
     * <p>One of {Renter, Lessor, Admin}</p>
     * <p>READ ONLY</p>
     */
    private final UserRole role;

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
            role = UserRole.stringToRole((String) value);
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
    public Account(String username, String email, UserRole role) {
        this(username, email, role, username);
    }

    /**
     * Full argument constructor.
     * @param username  The unique identifier for the user
     * @param email     The primary email of the user
     * @param role      One of {admin, renter, lessor}
     * @param name      The display name of the user
     */
    public Account(String username, String email, UserRole role, String name) {
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

    /**
     * Getter for the username attribute
     * @return  username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the name attribute
     * @return  name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the email attribute
     * @return  email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for the role attribute
     * @return  role
     */
    public UserRole getRole() {
        return role;
    }

    // setters

    /**
     * Setter for the username attribute
     * <p>Checks if the username is already in use before setting value.</p>
     * @param username  The new username
     * @return          Whether the username was changed successfully
     */
    public boolean setUsername(String username) {
        // check if the username is already in use
        boolean validUsr = true;  // change this to an appropriate method call

        if (validUsr) {
            // modify the existing account in the db
            this.username = username;
        }

        return validUsr;
    }

    /**
     * Setter for the name attribute
     * @param name  The new name
     */
    public void setName(String name) {
        // modify the existing account in the db
        this.name = name;
    }

    /**
     * TEMPORARY METHOD until a proper ORM layer is implemented
     * <p>Implements method of Entity interface.</p>
     * @return  the map representation of the object that is compatible with firebase
     */
    public Map<String, Object> getDatabaseRepresentation() {
        Map<String, Object> dbRep = new HashMap<>();

        dbRep.put("username", username);
        dbRep.put("name", name);
        dbRep.put("email", email);
        dbRep.put("role", UserRole.roleToString(role));

        return dbRep;
    }
}
