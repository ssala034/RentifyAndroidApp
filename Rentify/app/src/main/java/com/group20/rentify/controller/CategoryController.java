package com.group20.rentify.controller;


import android.util.Log;


import com.group20.rentify.entity.Category;
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

    public void updateCategory(String identifier, String newDescription){

        getCategory(identifier, new DataRetrievalCallback<Entity>() {
            @Override
            public void onDataRetrieved(Entity data) {
                Category curr = (Category) data;
                if (curr != null) {
                    curr.setDescription(newDescription);
                    // Update the category description in the database
                    dataController.updateEntity("categories" + "/" + identifier, curr);
                } else {
                    Log.d("Database Error", "Unable to successfully update Category ");
                }
            }
        });
    }

    // Make a a way to go from category name to id and then id to the actual category object

    /**
     * Given an id, synchronously remove it from the db.
     *
     * @param identifier                  the id of the category to be removed
     */
    public void removeCategory(String identifier) {

        dataController.getEntity("category", identifier, Category.class, result -> {
            if(result != null){
                dataController.removeEntity(result);
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
     */
    public void getCategory(String identifier, DataRetrievalCallback<Entity> callback){
        dataController.getEntity("category", identifier, Category.class, callback);

    }

    public Category getCategory(String categoryName){
        for(Category curr : dataController.getCategories()){
            if(curr.getName().equals(categoryName)){
                return curr;
            }
        }
        return null;
    }

    public List<Category> getCategories() {
        return dataController.getCategories();
    }

}
