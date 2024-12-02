package com.group20.rentify.controller;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.Category;
import com.group20.rentify.entity.Entity;
import com.group20.rentify.entity.Item;
import com.group20.rentify.entity.Request;
import com.group20.rentify.entity.UserRole;
import com.group20.rentify.util.DataSaver;
import com.group20.rentify.util.DatabaseInterface;
import com.group20.rentify.util.callback.AuthenticationCallback;
import com.group20.rentify.util.callback.DataRetrievalCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SaveDataController {

    private static SaveDataController instance;

    private final DataSaver dataSaver;
    private final Set<String> usernames;
    private final Set<String> emails;
    private final List<Account> accounts;
    private final List<Category> categories;
    private final List<Item> items;
    private final List<Request> requests;
    private boolean adminCreated;

    private final List<Subscriber<Account>> accountSubscribers;
    private final List<Subscriber<Category>> categorySubscribers;
    private final  List<Subscriber<Item>> itemSubscribers;
    private final List<Subscriber<Request>> requestSubscribers;

    private SaveDataController() {
        dataSaver = DatabaseInterface.getInstance();
        usernames = new HashSet<>();
        emails = new HashSet<>();
        accounts = new ArrayList<>();
        categories = new ArrayList<>();
        items = new ArrayList<>();
        requests = new ArrayList<>();
        accountSubscribers = new LinkedList<>();
        categorySubscribers = new LinkedList<>();
        itemSubscribers = new LinkedList<>();
        requestSubscribers = new LinkedList<>();

        // add listeners
        dataSaver.addDataChangeListener(DataSaver.USERNAME_PATH,
                data -> {
                    if (data != null) {
                        Set<String> updatedUsernames = data.keySet();
                        usernames.clear();
                        usernames.addAll(updatedUsernames);
                    } else {
                        usernames.clear();
                    }
                },
                error -> {throw (DatabaseException) error;});

        dataSaver.addDataChangeListener(DataSaver.EMAIL_PATH,
                data -> {
                    if (data != null) {
                        Set<String> updatedEmails = data.keySet();
                        emails.clear();
                        emails.addAll(updatedEmails);
                    } else {
                        emails.clear();
                    }
                },
                error -> {throw (DatabaseException) error;});

        dataSaver.addDataChangeListener(DataSaver.USER_PATH,
                data -> {
                    accounts.clear();
                    if (data != null) {
                        for (Object account : data.values()) {
                            // should find a cleaner way to do this
                            // so that database logic is abstracted from this class
                            Account castedAccount = ((DataSnapshot) account).getValue(Account.class);
                            castedAccount.loadFurther((DataSnapshot) account);
                            accounts.add(castedAccount);
                        }
                    }

                    for (Subscriber<Account> s: accountSubscribers) {
                        s.notify(accounts);
                    }
                },
                error -> {throw (DatabaseException) error;});

        dataSaver.addDataChangeListener(DataSaver.CATEGORY_PATH,
                data -> {
                    categories.clear();
                    if (data != null) {
                        for (Object category : data.values()) {
                            // should find a cleaner way to do this
                            // so that database logic is abstracted from this class
                            Category castedCategory = ((DataSnapshot) category).getValue(Category.class);
                            castedCategory.loadFurther((DataSnapshot) category);
                            categories.add(castedCategory);
                        }
                    }

                    for (Subscriber<Category> s: categorySubscribers) {
                        s.notify(categories);
                    }
                },
                error -> {throw (DatabaseException) error;});

        dataSaver.addDataChangeListener(DataSaver.ITEM_PATH,
                data -> {
                    items.clear();
                    if (data != null) {
                        for (Object item : data.values()) {
                            // should find a cleaner way to do this
                            // so that database logic is abstracted from this class
                            Item castedItem = ((DataSnapshot) item).getValue(Item.class);
                            castedItem.loadFurther((DataSnapshot) item);
                            items.add(castedItem);
                        }
                    }

                    for (Subscriber<Item> s: itemSubscribers) {
                        s.notify(items);
                    }
                },
                error -> {throw (DatabaseException) error;});

        dataSaver.addDataChangeListener(DataSaver.REQUEST_PATH,
                data -> {
                    requests.clear();
                    if (data != null) {
                        for (Object request : data.values()) {
                            // should find a cleaner way to do this
                            // so that database logic is abstracted from this class
                            Request castedRequest = ((DataSnapshot) request).getValue(Request.class);
                            castedRequest.loadFurther((DataSnapshot) request);
                            requests.add(castedRequest);
                        }
                    }

                    for (Subscriber<Request> s: requestSubscribers) {
                        s.notify(requests);
                    }
                },
                error -> {throw (DatabaseException) error;});


        dataSaver.retrieveData(DataSaver.ADMIN_PATH + "/admin", Boolean.class,
                result -> adminCreated = result != null);
    }

    public static SaveDataController getInstance() {
        if (instance == null) {
            instance = new SaveDataController();
        }

        return instance;
    }

    /**
     * Given an account user, check if that username is already taken.
     * @param username  A potential account username
     * @return          True if the username does not belong to any other users in the system.
     */
    public boolean verifyUniqueUsername(String username){
        return !usernames.contains(username);
    }

    /**
     * Given an account email, check if that email is already taken.
     * @param email  A potential account email
     * @return       True if the email is not associated with any other accounts in the system.
     */
    public boolean verifyUniqueEmail(String email) {
        return !emails.contains(replaceIllegalCharacters(email));
    }

    /**
     * Save an account into the database.
     * @param acc                       The account to save to the database
     * @return                          True if account can be created and is saved successfully
     * @throws IllegalStateException    If admin requirements are not met
     */
    public boolean saveAccount(Account acc) {
        if(acc.getRole() == UserRole.Role.admin){
            if (adminCreated)
                throw new IllegalStateException("Maximum 1 admin account possible");
            dataSaver.saveOrUpdateData(DataSaver.ADMIN_PATH + "/" + acc.getUsername(), true);
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

    public void updateAccount(Account acc) {
        dataSaver.saveEntity(acc, DataSaver.USER_PATH + "/" + acc.getUsername());
    }

    /**
     * Given a username or email asynchronously retrieves the account if it exists else return null pointer.
     * The returned object is passed to the callback for processing.
     *
     * @param identifier    username or email of account
     * @param callback      callback object overriding onEntityRetrieved
     */
    public void getAccount(String identifier, DataRetrievalCallback<Account> callback) {
        if (usernames.contains(identifier)) {
            dataSaver.retrieveEntity(DataSaver.USER_PATH + "/" + identifier, Account.class,
                    callback);
        } else {
            dataSaver.retrieveData(
                    DataSaver.EMAIL_PATH + "/" + replaceIllegalCharacters(identifier), String.class,
                    result -> {
                        dataSaver.retrieveEntity(DataSaver.USER_PATH + "/" + result,
                                Account.class, callback);
                    });
        }
    }

    /**
     * Given a username, synchronously remove it from the db.
     *
     * @param username                  the username of the account to be removed
     * @throws IllegalStateException    if the account does not exist in the database
     */
    public void removeAccount(String username) {
        dataSaver.retrieveData(DataSaver.USERNAME_PATH + "/" + username, String.class, email ->
                dataSaver.saveOrUpdateData(DataSaver.EMAIL_PATH + "/" + replaceIllegalCharacters(email), null));
        dataSaver.removeEntity(DataSaver.USER_PATH + "/" + username);
        dataSaver.saveOrUpdateData(DataSaver.USERNAME_PATH + "/" + username, null);
    }

    public <T extends UserRole> UserRole loadRole(DataSnapshot ds, Class<T> cls) {
        return ds.getValue(cls);
    }

    /**
     * Getter for the list of accounts, synchronized with the saved data
     * @return  A list of all accounts currently existing in the system
     */
    public List<Account> getAccounts(Subscriber<Account> s) {
        accountSubscribers.add(s);
        return accounts;
    }

    /**
     * Getter for the list of categories, synchronized with the saved data
     * @return  A list of all categories currently existing in the system
     */
    public List<Category> getCategories(Subscriber<Category> s) {
        categorySubscribers.add(s);
        return categories;
    }

    /**
     * Getter for the list of items, synchronized with the saved data
     * @return  A list of all items currently existing in the system
     */
    public List<Item> getItems(Subscriber<Item> s) {
        itemSubscribers.add(s);
        return items;
    }

    /**
     * Getter for the list of requests, synchronized with the saved data
     * @return  A list of all requests currently existing in the system
     */
    public List<Request> getRequests(Subscriber<Request> s) {
        requestSubscribers.add(s);
        return requests;
    }

    /**
     * Authenticate a login
     * @param credential    The username or email of the user
     * @param password      The associated password
     * @param callback      The callback to handle if the authentication succeeds or fails
     */
    public void authenticate(String credential, String password, AuthenticationCallback callback) {
        if (usernames.contains(credential)) {
            dataSaver.retrieveData(
                    DataSaver.USERNAME_PATH + "/" + credential, String.class,
                    result -> {
                        dataSaver.authenticate(result, password, callback);
                    });
        } else {
            dataSaver.authenticate(credential, password, callback);
        }
    }

    /**
     * Add a user credential set to the authenticator
     * @param email     The email of the user
     * @param password  The associated password
     */
    public void addToAuth(String email, String password) {
        dataSaver.addToAuth(email, password);
    }

    /**
     * If the entity exists in the saved data, update the existing entity;
     * else, save a new entity by generating a unique id
     *
     * @param entity    The entity to save
     */
    public void saveEntity(Entity entity) {
        String entityRootPath = pluralize(entity.getEntityTypeName());
        String id = entity.getUniqueIdentifier();

        if (id == null) {
            id = dataSaver.generateUniqueIdentifier(entityRootPath);
            entity.setUniqueIdentifier(id);
        }

        dataSaver.saveEntity(entity, entityRootPath + "/" + id);

//        dataSaver.retrieveEntity(entityRootPath + "/" + id, entity.getClass(),
//                result -> {
//                    if (result != null) {
//                        dataSaver.saveEntity(entity, entityRootPath + "/" + id);
//                    } else {
//                        String newId = dataSaver.generateUniqueIdentifier(entityRootPath);
//                        entity.setUniqueIdentifier(newId);
//                        dataSaver.saveEntity(entity, entityRootPath + "/" + newId);
//                    }
//                });
    }

    /**
     * Given a unique identifier asynchronously retrieves the entity if it exists else return null pointer.
     * The returned object is passed to the callback for processing.
     *
     * @param entityType    the entity type returned by Entity.getEntityTypeName()
     * @param identifier    the unique identifier returned by Entity.getUniqueIdentifier()
     * @param cls           the target entity class
     * @param callback      callback object overriding onEntityRetrieved
     */
    public <T extends Entity> void getEntity(String entityType, String identifier, Class<T> cls, DataRetrievalCallback<T> callback) {
        dataSaver.retrieveEntity(pluralize(entityType) + "/" + identifier, cls, callback);
    }

    /**
     * Given an entity, synchronously remove it from the db.
     *
     * @param entity                    the entity to be removed
     * @throws IllegalStateException    if the entity does not exist in the database
     */
    public void removeEntity(Entity entity) {
        dataSaver.removeEntity(pluralize(entity.getEntityTypeName()) + "/" + entity.getUniqueIdentifier());
    }

    /**
     * Gets the string value of the given path's node
     *
     * @param path      the full path to the value to retrieve (i.e., items/< item id >/name
     * @param callback  callback overriding onDataRetrieved(String)
     */
    public void getField(String path, DataRetrievalCallback<String> callback) {
        dataSaver.retrieveData(path, String.class, callback);
    }

    private String replaceIllegalCharacters(String str) {
        return str.replace('.', '-');
    }

    private String restoreReplaced(String str) {
        return str.replace('-', '.');
    }

    private String pluralize(String str) {
        if (str.charAt(str.length() - 1) == 'y') {
            return str.substring(0, str.length() - 1) + "ies";
        }

        return str + "s";
    }
}
