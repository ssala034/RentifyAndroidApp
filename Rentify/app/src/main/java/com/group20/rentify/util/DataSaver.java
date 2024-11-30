package com.group20.rentify.util;

import com.group20.rentify.entity.Entity;
import com.group20.rentify.util.callback.AuthenticationCallback;
import com.group20.rentify.util.callback.ChangeListenerCallback;
import com.group20.rentify.util.callback.DataRetrievalCallback;
import com.group20.rentify.util.callback.ErrorHandlerCallback;

public interface DataSaver {

    String USERNAME_PATH = "usernames";
    String USER_PATH = "users";
    String EMAIL_PATH = "emails";
    String ADMIN_PATH = "admins";
    String CATEGORY_PATH = "categories";
    String ITEM_PATH = "items";
    String REQUEST_PATH = "requests";

    /**
     * Get the data item at the path provided by key
     *
     * @param key       The full path to the entity (i.e., users.username.name)
     * @param callback  A callback for processing the retrieved data
     */
    <T extends Entity> void retrieveEntity(String key, Class<T> cls, DataRetrievalCallback<T> callback);

    /**
     * Remove the data item at the path provided by key
     *
     * @param key       The full path to the entity (i.e., users.username.name)
     */
    void removeEntity(String key);

    /**
     * Save the entity at the path provided by key, if it is possible
     *
     * @param entity    The entity to be saved
     * @param path      The full path to where the entity should be saved
     */
    void saveEntity(Entity entity, String path);

    /**
     * General data saver
     * @param key   The full path to the location of the data field to update
     * @param value The value to set the field to
     */
    void saveOrUpdateData(String key, Object value);

    /**
     * Retrieve the data stored at a location
     * @param key       The full path to the location of the data field to retrieve
     * @param callback  The callback for processing the data stored at the key location
     */
    <T> void retrieveData(String key, Class<T> cls, DataRetrievalCallback<T> callback);
    /**
     * Authenticate a login
     *
     * @param email     The email of a user
     * @param password  The associated password
     * @param callback  A callback for processing successful or failed authentications
     */
    void authenticate(String email, String password, AuthenticationCallback callback);

    /**
     * Add a set of login credentials to the authenticator
     * @param email     The email of a user
     * @param password  The associated password
     */
    void addToAuth(String email, String password);

    /**
     * Attach the callback to a data change listener
     * @param path          The path to the node to attach the listener to
     * @param callback      The callback to attach to the data change listener
     * @param errorCallback The callback to attach to the error listener
     */
    void addDataChangeListener(String path, ChangeListenerCallback callback, ErrorHandlerCallback errorCallback);

    /**
     * Create a unique identifier for a saved entity
     * @param path  The path to the location for which a unique identifier will be generated
     * @return      The generated id
     */
    String generateUniqueIdentifier(String path);
}
