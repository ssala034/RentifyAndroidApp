package com.group20.rentify.controller;

import android.util.Log;

import com.google.firebase.database.DatabaseException;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.UserRole;
import com.group20.rentify.util.DataSaver;
import com.group20.rentify.util.DatabaseInterface;
import com.group20.rentify.util.callback.AuthenticationCallback;
import com.group20.rentify.util.callback.EntityRetrievalCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaveDataController {

    private static SaveDataController instance;

    private final DataSaver dataSaver;
    private final Set<String> usernames;
    private final List<Account> accounts;
    private boolean adminCreated;

    private SaveDataController() {
        dataSaver = DatabaseInterface.getInstance();
        usernames = new HashSet<>();
        accounts = new ArrayList<>();

        // add listeners
        dataSaver.addDataChangeListener(DataSaver.USERNAME_PATH,
                data -> {
                    if (data != null) {
                        Set<String> updatedUsernames = data.keySet();
                        usernames.retainAll(updatedUsernames);
                        usernames.addAll(updatedUsernames);
                    } else {
                        usernames.clear();
                    }
                },
                error -> {throw (DatabaseException) error;});

        dataSaver.addDataChangeListener(DataSaver.USER_PATH,
                data -> {
                    accounts.clear();
                    if (data != null) {
                        for (Object account : data.values()) {
                            if (account instanceof Account) {
                                accounts.add((Account) account);
                            }
                        }
                    }
                },
                error -> {throw (DatabaseException) error;});

        adminCreated = false;
    }

    public static SaveDataController getInstance() {
        if (instance == null) {
            instance = new SaveDataController();
        }

        return instance;
    }

    /**
     * Given an account user, checks if that username is already taken.
     * @param username  A potential account username
     * @return          True if the username does not belong to any other users in the system.
     */
    public boolean verifyUniqueUsername(String username){
        return !usernames.contains(username);
    }

    /**
     * Save an account into the database.
     * @param acc                       The account to save to the database
     * @return                          True if account can be created and is saved successfully
     * @throws IllegalStateException    If admin requirements are not met
     */
    public boolean saveAccount(Account acc) {

        if(acc.getRole() == UserRole.admin){
            if (adminCreated)
                throw new IllegalStateException("Maximum 1 admin account possible");
            adminCreated = true;
        }

        if(!verifyUniqueUsername(acc.getUsername())){
            Log.d("Business Rule ERROR","Username in DB already "+ acc.getUsername());
            return false;
        }

        // update the username -> email table
        // this also updates the username set through the event listener
        dataSaver.saveOrUpdateData(DataSaver.USERNAME_PATH + "/" + acc.getUsername(), acc.getEmail());
        dataSaver.saveOrUpdateData(DataSaver.EMAIL_PATH + "/" + replaceIllegalCharacters(acc.getEmail()), acc.getUsername());

        // Add the account under the "users" node with their username as the key, Asynchronous
        dataSaver.saveEntity(acc, DataSaver.USER_PATH + "/" + acc.getUsername());

        return true;
    }

    /**
     * Given a username or email asynchronously retrieves the account if it exists else return null pointer.
     * The returned object is passed to the callback for processing.
     *
     * @param identifier    username or email of account
     * @param callback      callback object overriding onEntityRetrieved
     */
    public void getAccount(String identifier, EntityRetrievalCallback callback) {
        String username;
        if (usernames.contains(identifier)) {
            username = identifier;
        } else {
            username = dataSaver.retrieveData(DataSaver.EMAIL_PATH + "/" + identifier).toString();
        }
        dataSaver.retrieveEntity(DataSaver.USER_PATH + "/" + username, Account.class, callback);
    }

    /**
     * Given a username, synchronously remove it from the db.
     *
     * @param username                  the username of the account to be removed
     * @throws IllegalStateException    if the account does not exist in the database
     */
    public void removeAccount(String username) {
        dataSaver.removeEntity(DataSaver.USER_PATH + "/" + username);
        dataSaver.saveOrUpdateData(DataSaver.USERNAME_PATH + "/" + username, null);
    }

    /**
     * Getter for the list of accounts, synchronized with the saved data
     * @return  A list of all accounts currently existing in the system
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Authenticate a login
     * @param credential    The username or email of the user
     * @param password      The associated password
     * @param callback      The callback to handle if the authentication succeeds or fails
     */
    public void authenticate(String credential, String password, AuthenticationCallback callback) {
        if (usernames.contains(credential)) {
            credential = dataSaver.retrieveData(DataSaver.USERNAME_PATH + "/" + credential).toString();
        }

        dataSaver.authenticate(credential, password, callback);
    }

    /**
     * Add a user credential set to the authenticator
     * @param email     The email of the user
     * @param password  The associated password
     */
    public void addToAuth(String email, String password) {
        dataSaver.addToAuth(email, password);
    }

    private String replaceIllegalCharacters(String str) {
        return str.replace('.', '-');
    }
}
