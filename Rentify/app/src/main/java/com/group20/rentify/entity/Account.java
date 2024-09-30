package com.group20.rentify.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Account extends Entity {

    //class variables
    protected static ArrayList<Account> accounts;
    // may remove this attribute and query the db instead
    // temporarily using an array list until the best data structure can be decided



    // instance variables
    private String name;
    private String username;
    private String email;
    private String role;  // this may be changed to a different type later

    public Account(){

    }
    public Account(String name,String username, String email, String role){
        this.name = name;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getRole(){
        return this.role;
    }

    public  String getName(){
        return this.name;
    }

    public String getUsername(){
        return this.username;
    }
    public void setRole(String newRole){
        this.role = newRole;
    }

    @NonNull
    @Override
    public String toString(){
        return "("+name+", " + username+")";
    }
}
