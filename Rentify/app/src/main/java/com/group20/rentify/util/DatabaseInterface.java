package com.group20.rentify.util;

import static java.sql.DriverManager.println;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group20.rentify.entity.Entity;
import com.group20.rentify.util.callback.AuthenticationCallback;
import com.group20.rentify.util.callback.ChangeListenerCallback;
import com.group20.rentify.util.callback.DataRetrievalCallback;
import com.group20.rentify.util.callback.ErrorHandlerCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * This is our App point of interaction with our Firebase Realtime Database.
 * Some of the method must be used with the intention of overriding a callback method in DatabaseCallBack class.
 * Since Realtime Database gets the data asynchronous, we cannot directly return the data because we don't know if dB has finished the getting it.
 * basically it lets you run other things in the background. The callback waits till its done. A bit more code but can be done.
 * Look at bottom of class to see how to use the callbacks.
 */
public class DatabaseInterface implements DataSaver {

    public static final String[] ROOTS = {USERNAME_PATH, USER_PATH, EMAIL_PATH};

    private static DatabaseInterface instance;

    private final FirebaseDatabase db;
    private final FirebaseAuth auth;

    private DatabaseInterface() {
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        createRoots();
    }

    public static DatabaseInterface getInstance() {
        if (instance == null) {
            instance = new DatabaseInterface();
        }

        return instance;
    }

    @Override
    public <T extends Entity> void retrieveEntity(String key, Class<T> cls, DataRetrievalCallback<T> callback) {
        DatabaseReference node = db.getReference().child(key);

        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    T res = dataSnapshot.getValue(cls);
                    res.loadFurther(dataSnapshot);
                    callback.onDataRetrieved(res);
                } else {
                    Log.d("Database ERROR", "Entity does not exist");
                    callback.onDataRetrieved(null);
                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database ERROR", "The read failed: " + databaseError.getCode());
                callback.onDataRetrieved(null);
            }
        });
    }

    @Override
    public void removeEntity(String key) {
        DatabaseReference node = db.getReference().child(key);

        node.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Node and all its children have been successfully deleted
                Log.d("Database INFO","Node removed successfully");
            } else {
                // Handle the error
                Log.e("Database ERROR", "Entity not in database", task.getException());
            }
        });
    }

    @Override
    public void saveEntity(Entity entity, String path) {
        DatabaseReference node = db.getReference().child(path);

        node.setValue(entity).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Entity added successfully
                Log.d("Database INFO", "Entity added successfully");
            }else{
                Log.e("Database ERROR", "Failed to add entity", task.getException());
            }
        });
    }

    @Override
    public void saveOrUpdateData(String key, Object value) {
        DatabaseReference node = db.getReference().child(key);
        node.setValue(value).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Entity added successfully
                Log.d("Database INFO", "Value updated successfully");
            }else{
                Log.e("Database ERROR", "Failed to update value", task.getException());
            }
        });
    }

    @Override
    public <T> void retrieveData(String key, Class<T> cls, DataRetrievalCallback<T> callback) {
        DatabaseReference node = db.getReference().child(key);
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    println(key);
                    T res = dataSnapshot.getValue(cls);
                    callback.onDataRetrieved(res);
                } else {
                    Log.d("Database ERROR", "Object does not exist");
                    callback.onDataRetrieved(null);
                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database ERROR", "The read failed: " + databaseError.getCode());
                callback.onDataRetrieved(null);
            }
        });
    }

    @Override
    public void authenticate(String email, String password, AuthenticationCallback callback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            callback.onAuthenticationCompleted(task.isSuccessful());
        });
    }

    @Override
    public void addToAuth(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password);
    }

    @Override
    public void addDataChangeListener(String path, ChangeListenerCallback callback, ErrorHandlerCallback errorCallback) {
        DatabaseReference node = db.getReference().child(path);
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> data = new HashMap<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.exists()) {
                            data.put(ds.getKey(), ds);
                        }
                    }
                    callback.onDataChange(data);
                } else {
                    Log.d("Database ERROR", "Path does not exist");
                    callback.onDataChange(null);
                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database ERROR", "The read failed: " + databaseError.getCode());
                errorCallback.onError(databaseError.toException());
            }
        });
    }

    @Override
    public String generateUniqueIdentifier(String path) {
        return db.getReference(path).push().getKey();
    }

    private void createRoots() {
        // create root nodes
        for (String root : ROOTS) {
            db.getReference().child(root);
        }
    }
}
