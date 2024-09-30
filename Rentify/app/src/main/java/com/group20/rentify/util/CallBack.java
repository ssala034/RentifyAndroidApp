package com.group20.rentify.util;

import com.group20.rentify.entity.Account;

import java.util.ArrayList;

public interface CallBack {
    void onAccountRetrieved(Account account);
    void onAccountRemoval(Account account);
    void onError();
    void onAccountList(ArrayList<Account> listOfAccounts);
}
