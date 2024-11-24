package com.group20.rentify.entity;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
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
    private UserRole.Role role;

    private UserRole accountRole;

    private boolean enabled;

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
    public Account(String username, String email, UserRole.Role role, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName.isEmpty() ? username : firstName;
        this.lastName = lastName;
        enabled = true;
        this.role = role;
        this.accountRole = generateRole(role);
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

    @Override
    public void delete() {
        accountRole.delete();
        dataSaver.removeAccount(username);
    }

    @Override
    public void save() {
        dataSaver.updateAccount(this);
    }

    public void loadFurther(DataSnapshot ds) {
        if (ds.hasChild("accountRole")) {
            switch (role) {
                case admin:
                    accountRole = dataSaver.loadRole(ds.child("accountRole"), AdminRole.class);
                    break;
                case renter:
                    accountRole = dataSaver.loadRole(ds.child("accountRole"), RenterRole.class);
                    break;
                case lesser: case lessor:
                    accountRole = dataSaver.loadRole(ds.child("accountRole"), LessorRole.class);
                    break;
            }
            accountRole.loadFurther();
            accountRole.setUser(this);
        }
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
    public UserRole.Role getRole() { // for firebase compatibility
        return role;
    }

    public UserRole getAccountRole() {
        if (accountRole == null) {
            accountRole = generateRole(role);
        }
        return accountRole;
    }

    @Override
    public String getUniqueIdentifier() {
        return getUsername();
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public String displayDetails() {
        return String.format("%s\tRole: %s\n\tEmail: %s", enabled ? "" : "DISABLED\n", getRole(), getEmail());
    }

    public boolean getEnabled() {
        return enabled;
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
        boolean validUsr = dataSaver.verifyUniqueUsername(username);

        if (validUsr) {
            this.username = username;
        }

        return validUsr;
    }

    /**
     * Setter for the first name attribute
     * @param name  The new name
     */
    public void setFirstName(String name) {
        // modify the existing account in the db
        this.firstName = name;
    }

    /**
     * Setter for the last name attribute
     * @param name  The new name
     */
    public void setLastName(String name) {
        // modify the existing account in the db
        this.lastName = name;
    }

    @Override
    public boolean setUniqueIdentifier(String id) {
        return setUsername(id);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Account other = (Account) obj;
        return (getUniqueIdentifier() == null && other.getUniqueIdentifier() == null)
                || (other.getUniqueIdentifier() != null
                && other.getUniqueIdentifier().equals(getUniqueIdentifier()));
    }

    private UserRole generateRole(UserRole.Role role) {
        switch (role) {
            case admin:
                return new AdminRole(this);
            case renter:
                return new RenterRole(this);
            case lessor: case lesser:
                return new LessorRole(this);
            default:
                throw new IllegalArgumentException("Invalid Role");
        }
    }
}