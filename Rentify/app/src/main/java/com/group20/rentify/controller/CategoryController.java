package com.group20.rentify.controller;

import com.group20.rentify.entity.Category;

import java.util.List;

public class CategoryController {

    private static CategoryController instance;

    public CategoryController(){

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
        category.save();
        return true;
    }

    public void updateCategory(Category category, String newName, String newDescription){
        category.setDescription(newDescription);
        category.setName(newName);
        category.save();
    }

    public List<Category> getCategories(Subscriber<Category> s) {
        return Category.getCategories(s);
    }

}
