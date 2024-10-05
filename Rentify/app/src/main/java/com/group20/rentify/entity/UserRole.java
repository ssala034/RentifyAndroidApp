package com.group20.rentify.entity;

public enum UserRole {
    admin,
    renter,
    lessor;

    public static UserRole stringToRole(String role) {
        if (role == null) {
            throw new IllegalArgumentException();
        } else {
            if (role.equals("admin")) {
                return admin;
            } else if (role.equals("renter")) {
                return renter;
            } else if (role.equals("lessor")) {
                return lessor;
            }
        }

        throw new IllegalArgumentException();
    }

    public static String roleToString(UserRole role) {
        if (role == admin) {
            return "admin";
        } else if (role == renter) {
            return "renter";
        } else if (role == lessor) {
            return "lessor";
        }

        throw new IllegalArgumentException();
    }
}
