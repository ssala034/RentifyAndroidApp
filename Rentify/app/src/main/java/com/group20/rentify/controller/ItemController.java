package com.group20.rentify.controller;


import android.util.Log;

import com.group20.rentify.entity.Category;
import com.group20.rentify.entity.Entity;
import com.group20.rentify.entity.Item;
import com.group20.rentify.util.callback.DataRetrievalCallback;

import java.util.List;

public class ItemController {
    private static ItemController instance;
    private final SaveDataController dataController;

    public ItemController(){
        dataController = SaveDataController.getInstance();
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
    public boolean addItem(Item item ){ // should admin logic go here??
        dataController.saveEntity(item);
        return true;
    }

    public void updateItem(Item item, String newName, String newDescription,  int newPeriod, int newFee){
      item.setName(newName);
      item.setDescription(newDescription);
      item.setRentalTime(newPeriod);
      item.setRentalFee(newFee);
      dataController.updateEntity(item);

    }

    /**
     * Given an id, synchronously remove it from the db.
     *
     * @param identifier                  the id of the item to be removed
     */
    public void removeItem(String identifier) {

        dataController.getEntity("item", identifier, Item.class, result -> {
            if(result != null){
                dataController.removeEntity(result);
            }else{
                Log.d("Item Error", "Item doesn't exists");

            }
        });
    }

    /**
     * Given the item id, asynchronously retrieves the category if it exists else return null pointer.
     * The returned object is passed to the callback for processing.
     *
     * @param identifier    item id
     * @param callback      callback object overriding onEntityRetrieved
     */
    public void getItem(String identifier, DataRetrievalCallback<Entity> callback){
        dataController.getEntity("item", identifier, Item.class, callback);
    }

    public List<Item> getItems(Subscriber<Item> s) {
        return dataController.getItems(s);
    }

}
