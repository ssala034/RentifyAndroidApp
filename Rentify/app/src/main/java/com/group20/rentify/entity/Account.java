package com.group20.rentify.entity;

import com.group20.rentify.controller.SaveDataController;

/**
 * A model class for user accounts of the Rentify app.
 */
public class Account implements Entity {

    private static Account sessionAccount;

    // instance variables
    /**
     * The user's first name, which is used as their display name.
     * <p>
     *     If not provided, the default behaviour is to
     *     use the user's username for the name.
     * </p>
     */
    private String firstName;

    /**
     * The user's last name.
     * <p>
     *     If not provided, the default behaviour is to
     *     leave it blank.
     * </p>
     */
    private String lastName;

    /**
     * The user's unique username which can be used for sign in.
     */
    private String username;
  
    /**
     * The user's associated email address.
     * <p>Currently READ ONLY.</p>
     */
    private String email;

    /**
     * The user's role in the system.
     * <p>One of {Renter, Lessor, Admin}</p>
     * <p>READ ONLY</p>
     */
    private UserRole role;

    // constructors
    public Account() {

    }

    /**
     * Full argument constructor.
     * @param username  The unique identifier for the user
     * @param email     The primary email of the user
     * @param role      One of {admin, renter, lessor}
     * @param firstName The first name and display name of the user
     * @param lastName  The last name of the user
     */
    public Account(String username, String email, UserRole role, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.firstName = firstName.isEmpty() ? username : firstName;
        this.lastName = lastName;
    }

    @Override
    public String getEntityTypeName() {
        return "account";
    }

    public static Account getSessionAccount() {
        return sessionAccount;
    }

    public static void setSessionAccount(Account currentSession) {
        sessionAccount = currentSession;
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
     * Getter for the first name attribute
     * @return  firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for the last name attribute
     * @return  lastName
     */
    public String getLastName() {
        return lastName;
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

    @Override
    public String getUniqueIdentifier() {
        return getUsername();
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
        boolean validUsr = SaveDataController.getInstance().verifyUniqueUsername(username);

        if (validUsr) {
            this.username = username;
        }

        return validUsr && SaveDataController.getInstance().saveAccount(this);
    }

    /**
     * Setter for the first name attribute
     * @param name  The new name
     */
    public boolean setFirstName(String name) {
        // modify the existing account in the db
        this.firstName = name;
        return SaveDataController.getInstance().saveAccount(this);
    }

    /**
     * Setter for the last name attribute
     * @param name  The new name
     */
    public boolean setLastName(String name) {
        // modify the existing account in the db
        this.lastName = name;
        return SaveDataController.getInstance().saveAccount(this);
    }

    @Override
    public boolean setUniqueIdentifier(String id) {
        return setUsername(id);
    }
}