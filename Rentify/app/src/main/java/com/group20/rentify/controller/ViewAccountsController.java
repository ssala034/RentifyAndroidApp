package com.group20.rentify.controller;

import com.group20.rentify.entity.Account;

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
}
