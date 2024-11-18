package com.group20.rentify.entity;

import com.group20.rentify.controller.Subscriber;

import java.util.List;

public class AdminRole extends UserRole {

    public AdminRole() {
        role = Role.admin;
    }

    public AdminRole(Account account) {
        super(account);
    }

    public List<Account> getAccountList(Subscriber<Account> s) {
        return dataSaver.getAccounts(s);
    }
}
