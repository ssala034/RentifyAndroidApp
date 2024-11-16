package com.group20.rentify.entity;

import com.group20.rentify.controller.SaveDataController;

public class UserRole {
    protected static final SaveDataController dataSaver = SaveDataController.getInstance();
    public enum Role {admin, renter, lessor, lesser}

    protected Role role;

    public UserRole() {
        role = null;
    }

    public Role getRole() {
        return role;
    }
}
