package com.group20.rentify.controller;


import android.util.Log;


import com.group20.rentify.entity.Entity;
import com.group20.rentify.util.callback.DataRetrievalCallback;

import java.util.List;

public class CategoryController {

    private static CategoryController instance;

    private final SaveDataController dataController;

    public CategoryController(){
        dataController = SaveDataController.getInstance();
    }

    public static CategoryController getInstance(){
        if(instance == null){
            instance = new CategoryController();
        }

        return instance;
    }


    /**
     * Save a category into the database.
     * @param category                       The category to save to the database
     * @return                          True if category is saved successfully
     */
    public boolean addCategory( Category category ){ // should admin logic go here??
        dataController.saveEntity(category);

        return true;
    }

    /**
     * Given an id, synchronously remove it from the db.
     *
     * @param identifier                  the id of the category to be removed
     */
    public void removeCategory(String identifier) {

        dataController.getEntity("category", identifier, Category.class, result -> {
            if(result != null){
                dataController.removeEntity(result, identifier);
            }else{
                Log.d("Category Error", "Category doesn't exists");

            }
        });
    }

    /**
     * Given the category id, asynchronously retrieves the category if it exists else return null pointer.
     * The returned object is passed to the callback for processing.
     *
     * @param identifier    category id
     * @param callback      callback object overriding onEntityRetrieved
     */    public void getCategory(String identifier, DataRetrievalCallback<Entity> callback){
        dataController.getEntity("category", identifier, Category.class, callback);

    }

    public List<Category> getCategories() {
        return dataController.getCategories();
    }

}
