package com.group20.rentify.entity;

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

}
