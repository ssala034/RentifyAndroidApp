package com.group20.rentify.entity;

public class LesserRole extends UserRole {
    @Override
    public RoleName getRoleName() {
        return RoleName.lesser;
    }
}
