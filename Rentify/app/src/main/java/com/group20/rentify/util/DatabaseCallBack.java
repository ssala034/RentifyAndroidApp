package com.group20.rentify.util;

import com.group20.rentify.entity.Account;

import java.util.ArrayList;


public class DatabaseCallBack implements CallBack{
    @Override
    public void onAccountRetrieved(Account account) {

    }

    @Override
    public void onAccountRemoval(Account account) {

    }
    public void onAccountList(ArrayList<Account> listOfAccounts){
    }

    @Override
    public void onError() {

    }


}
