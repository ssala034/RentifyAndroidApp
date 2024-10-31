package com.group20.rentify.entity;

public class RenterRole extends UserRole {
    @Override
    public RoleName getRoleName() {
        return RoleName.renter;
    }
}
