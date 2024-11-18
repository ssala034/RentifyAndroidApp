package com.group20.rentify.entity;

public class RenterRole extends UserRole {
    public RenterRole() {
        role = Role.renter;
    }

    public RenterRole(Account account) {
        super(account);
    }
}
