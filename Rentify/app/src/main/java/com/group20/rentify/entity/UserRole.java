package com.group20.rentify.entity;

import com.google.firebase.database.Exclude;
import com.group20.rentify.controller.SaveDataController;

public class UserRole {
    protected static final SaveDataController dataSaver = SaveDataController.getInstance();
    public enum Role {admin, renter, lessor, lesser}

    protected Role role;
    @Exclude protected Account user;

    public UserRole() {
    }

    public UserRole(Account account) {
        user = account;
    }

    public Role getRole() {
        return role;
    }

    @Exclude
    public Account getUser() {
        return user;
    }

    void setUser(Account user) {
        this.user = user;
    }

    public void loadFurther() {

    }

    public void delete() {

    }
}
