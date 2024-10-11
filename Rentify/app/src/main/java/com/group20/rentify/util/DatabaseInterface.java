package com.group20.rentify.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.Entity;
import com.group20.rentify.entity.UserRole;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This is our App point of interaction with our Firebase Realtime Database.
 * Some of the method must be used with the intention of overriding a callback method in DatabaseCallBack class.
 * Since Realtime Database gets the data asynchronous, we cannot directly return the data because we don't know if dB has finished the getting it.
 * basically it lets you run other things in the background. The callback waits till its done. A bit more code but can be done.
 * Look at bottom of class to see how to use the callbacks.
 */
public class DatabaseInterface {


    /* all instances of DbInterface must interact with same Db, thus same reference*/
    private static FirebaseDatabase db;
    private static int adminCount = 0;
    /*current way of keeping track of existing users, to do via a search with Firebase need a callback. LATER*/
    private static HashSet<String> userSet;

    public DatabaseInterface(){
        db = FirebaseDatabase.getInstance();
        /* all nodes will be under root node users*/
        db.getReference().child("users");
        userSet = new HashSet<>();
    }


    /* create account in our db with all its attributes
    * @param account to add
    * @return boolean
    * */
    public boolean createAccount(String username, String email, UserRole role, String name){
        // worry about if an admin account is passed
        return saveAnAccount(new Account(username, email, role, name));
    }

    /*create account in our db with all its attributes
     * @param account to add
    * @return boolean
    * */
    public boolean createAccount(Account acc){
        return saveAnAccount(acc);
    }

    /*Given an account user, checks if that username is already taken.
     * @param account username
    * @return boolean
    *   */
    public boolean checksUsername(String username){
        return userSet.contains(username);
    }

    /*Saves an account into our database. Returns true if done so, and returns false if username already taken.
    * Throws an error if admin requirements not there or can put account
    * @param account to add
    * @return boolean
    *  */
    private boolean saveAnAccount(Account acc) throws IllegalStateException{

        // checks if admin already created or not done yet
        if (acc.getRole() != UserRole.admin && adminCount == 0) {
            throw new IllegalStateException("Admin must be create first");
        } else if(acc.getRole() == UserRole.admin && adminCount >= 1){
            throw new IllegalStateException("Maximum 1 admin account possible");
        }

        if(checksUsername(acc.getUsername())){
            Log.d("Username in DB already","Username in DB already "+ acc.getUsername());
            return false;
        }

        adminCount = 1;

        // try to make so can add username in the listener
        userSet.add(acc.getUsername());

        // Add the rentor/lessor under the "users" node with their username as the key, Asynchronous
        db.getReference().child("users").child(acc.getUsername()).setValue(acc).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Rentor added successfully
                Log.d("Success", "User added successfully: " + acc.getUsername());
            }else{
                Log.e("Fail", "Failed to add user: " + acc.getUsername(), task.getException());
                throw new IllegalStateException("failed to add user");
            }
        });

        return true;

    }

    /*
    *Given a username retrieves the account if it exists else return null pointer.
    * Must use callback to retrieve it, can be done by overriding the DataBaseCallBack method you want at the time you call the method.
    * This is how Realtime Database asynchronous approach works, otherwise not sure if correct value is returned before retrieved data is complete.
    * The callback method will store the Account and that is how you can use it asynchronously when retrieved. Then do what you want with it as such.
    *
    * @param string username of account, context the MainActivity context, Callback to override.
    * */
    public void getAccount(String username, Context context, DatabaseCallBack callBack) throws IllegalStateException{
        db.getReference().child("users").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username already exists, handle this case (e.g., show error)
                    Entity entity = dataSnapshot.getValue(Account.class);
                    callBack.onEntityRetrieved(entity);
                } else {
                    // Username does not exist
                    throw new IllegalStateException("Username not there.");
                }

            };
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                callBack.onEntityRetrieved(null);
            }
        });

    }

    /*Given an account it will remove it from the db. You can get that account via the callback and manipulate as you want.
    * */
    public Account removeAccount(Account acc) throws  IllegalStateException{
        DatabaseReference node = db.getReference().child("users").child(acc.getUsername());

        node.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Node and all its children have been successfully deleted
                Log.d("A","success");
            } else {
                // Handle the error
                throw new IllegalStateException("Account not in Database");
            }
        });

        return acc;

    }

    /*Given an account it will remove it from the db. You can get that account via the callback.
    * */
    public void removeAccount(String username, Context context, DatabaseCallBack callBack) {
        DatabaseReference node = db.getReference().child("users").child(username);

        // First, get the account details
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Convert the snapshot to an Entity object, Maybe an account careful!!
                    Entity entity =  dataSnapshot.getValue(Account.class);
                    if (entity != null) {
                        // Now remove the account from Firebase
                        node.removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Successfully removed, pass the account to the callback
                                callBack.onEntityRemoval(entity);
                            } else {
                                // Handle removal failure
                                Log.d("A", "Failed to remove account '" + username + "'");
                            }
                        });
                    } else {
                        Log.d("a", "Failed to retrieve account for '" + username + "'");
                    }
                } else {
                    // Account does not exist
                    Log.d("a", "Username '" + username + "' does not exist in the database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.d("a", "Database error: " + databaseError.getMessage());
            }
        });
    }



    /*Given a admin account, it will retrieve a list of user created so far, including the admin account.
    * You must override the callback method, or add your own to manipualte that list as you want.
    * */

    public void retreiveAccounts(String username, Context context, DatabaseCallBack callBack){
       getAccount(username, context, new DatabaseCallBack(){
           @Override
           public void onEntityRetrieved(Entity entity){
               Account acc;

               if (entity instanceof Account) {
                    acc = (Account) entity;

                   if(acc.getRole() == UserRole.admin) {
                       db.getReference("users").get().addOnCompleteListener(task -> {
                           if(task.isSuccessful()){
                               ArrayList<Entity> entityList = new ArrayList<>();
                               for(DataSnapshot snapshot: task.getResult().getChildren()){
                                   Account tmp = snapshot.getValue(Account.class);
                                   if(tmp!=null){
                                       entityList.add(tmp);
                                   }
                               }
                               callBack.onEntityList(entityList);
                           }else{
                               Log.d("a", "failed to get user accounts");
                           }
                       });
                   }else{
                       Log.d("a", "User must be an admin to get list of all users");
                   }
               } else {
                   Log.d("a", "Retrieval error: Entity retrieved is not an Account");
               }

           }
       });
    }

    /* DEMO FOR CALLBACK:

     dataBase.getAccount("Joey", this "(main activity context)" , new DatabaseCallBack() {
            @Override
            public void onAccountRetrieved(Account account) {
                //handle the account as you want
                if(account !=null) {
                    Log.d(account.getRole(), account.getRole());
                    Log.d("name", account.getName());
                }
            }
        });

       dataBase.removeAccount("kyle", this, new DatabaseCallBack(){
           @Override
           public void onAccountRemoval(Account account){
               if(account!=null){
                   Log.d(account.getRole(), account.getRole());
                   Log.d("name", "Removed: "+ account.getName());
               }
           }
       });

       dataBase.retreiveAccounts("john", this, new DatabaseCallBack(){
           @Override
           public void onAccountList(ArrayList<Account> accountList){
               int count = 0;
               for(Account a: accountList){
                   Log.d("a", "User "+ count + " " + a.getUsername());
                   count++;
               }
           }
       });
       *
       *
    * */



}
