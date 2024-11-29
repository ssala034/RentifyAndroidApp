package com.group20.rentify.controller;

import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.Item;
import com.group20.rentify.entity.LessorRole;

import java.util.List;

public class ItemController {
    private static ItemController instance;

    public ItemController(){

    }

    public static ItemController getInstance(){
        if(instance == null){
            instance = new ItemController();
        }
        return instance;
    }

    /**
     * Save a category into the database.
     * @param item                       The category to save to the database
     * @return                          True if category is saved successfully
     */
    public boolean addItem(Item item){ // should admin logic go here??
        item.save();
        return true;
    }

    public void updateItem(Item item, String newName, String newDescription,  double newPeriod, double newFee){
      item.setName(newName);
      item.setDescription(newDescription);
      item.setRentalTime(newPeriod);
      item.setRentalFee(newFee);
      ((LessorRole) Account.getSessionAccount().getAccountRole()).refreshList();
      item.save();
    }

    public List<Item> getItems(Subscriber<Item> s) {
        return ((LessorRole) (Account.getSessionAccount().getAccountRole())).getItems(s);
    }

    public void deleteItem(Item item) {
        ((LessorRole) (Account.getSessionAccount().getAccountRole())).removeItem(item);
    }

}
