package com.group20.rentify.entity;

public class AdminRole extends AccountRole {
    @Override
    public RoleName getRoleName() {
        return RoleName.admin;
    }
}
