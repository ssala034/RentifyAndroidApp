package com.group20.rentify.entity;

public class AdminRole extends AccountRole {
    @Override
    public String getRoleName() {
        return "admin";
    }
}
