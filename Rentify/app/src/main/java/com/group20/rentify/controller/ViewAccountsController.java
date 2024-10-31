package com.group20.rentify.controller;

import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.RoleName;
import com.group20.rentify.entity.UserRole;

import java.util.List;

public class ViewAccountsController {
    private static ViewAccountsController instance;

    private final SaveDataController saveDataController;

    private ViewAccountsController() {
        saveDataController = SaveDataController.getInstance();
    }

    public static ViewAccountsController getInstance() {
        if (instance == null) {
            instance = new ViewAccountsController();
        }

        return instance;
    }

    public List<Account> getAccountList() {
        return saveDataController.getAccounts();
    }

    public void disableAccount(String username) {
        saveDataController.getAccount(username, account -> {
            if (account.getRole().getRoleName() == RoleName.admin) {
                throw new IllegalArgumentException("Cannot disable admin account.");
            }

            ((UserRole) account.getRole()).setEnabled(false);
        });
    }

    public void deleteAccount(String username) {
        saveDataController.removeAccount(username);
    }
}
