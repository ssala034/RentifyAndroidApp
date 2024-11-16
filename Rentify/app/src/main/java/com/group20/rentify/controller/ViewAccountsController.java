package com.group20.rentify.controller;

import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.AdminRole;
import com.group20.rentify.entity.UserRole;

import java.util.List;

public class ViewAccountsController {
    private static ViewAccountsController instance;

    private ViewAccountsController() {

    }

    public static ViewAccountsController getInstance() {
        if (instance == null) {
            instance = new ViewAccountsController();
        }

        return instance;
    }

    public List<Account> getAccountList(Subscriber<Account> s) {
        Account account = Account.getSessionAccount();

        if (account.getRole() == UserRole.Role.admin) {
            return ((AdminRole) account.getAccountRole()).getAccountList(s);
        } else {
            throw new IllegalStateException("Only admin account can retrieve account list");
        }
    }

    public boolean disableAccount(Account account) {

        if (account.getRole() == UserRole.Role.admin) {
            return false;
        }

        account.setEnabled(false);
        account.save();

        return true;
    }

    public boolean enableAccount(Account account) {
        if (account.getRole() == UserRole.Role.admin) {
            return false;
        }

        account.setEnabled(true);
        account.save();

        return true;
    }
}
