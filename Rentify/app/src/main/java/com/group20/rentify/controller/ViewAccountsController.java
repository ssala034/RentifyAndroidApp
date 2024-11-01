package com.group20.rentify.controller;

import com.group20.rentify.entity.Account;
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

    public List<Account> getAccountList(Subscriber<Account> s) {
        return saveDataController.getAccounts(s);
    }

    public boolean disableAccount(Account account) {

        if (account.getRole() == UserRole.admin) {
            return false;
        }

        account.setEnabled(false);
        saveDataController.updateAccount(account);

        return true;
    }

    public void deleteAccount(Account account) {
        saveDataController.removeAccount(account.getUsername());
    }
}
