package com.group20.rentify.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A model class for the categories of the Rentify app.
 */
public class Category implements Entity {

    // instance variables

    /**
     * The name of the category
     */
    private String name;

    /**
     * The description of the category
     */
    private String description;

    /**
     * The unique identifier for the category
     */
    private String uniqueIdentifier;

    /**
     * List of items contained in the category
     */
    private List<Item> items;


    // constructors

    public Category() {
    }

    /**
     * Full argument constructor.
     * @param name The name of the category
     * @param description The description providing information about the category
     * @param uniqueIdentifier The unique identifier of the category
     */
    public Category(String name, String description, String uniqueIdentifier){
        this.name = name;
        this.description = description;
        this.uniqueIdentifier = uniqueIdentifier;
        this.items = new ArrayList<>();
    }


    // getters

    /**
     * Gets the name of the entity
     * @return "category"
     */
    @Override
    public String getEntityTypeName() {
        return "category";
    }

    /**
     * Getter for name attribute
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for description attribute
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for id attribute
     * @return id
     */
    @Override
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }


    // setters

    /**
     * Setter for name attribute
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for description attribute
     * @param description The new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * Setter for id attribute
     *
     * @param id The new identifier
     * @return True if the new identifier is set successfully (not null or empty), false otherwise
     */
    @Override
    public boolean setUniqueIdentifier(String id) {
        if (id != null && !id.isEmpty()){
            this.uniqueIdentifier = id;
            return true;
        }
        return false;
    }

    /**
     * Adds a new item to a category
     * @param item The item to be added
     */
    public void addIItem(Item item) {
        if (item != null && !items.contains(item)){
            items.add(item); //add the new item to the list of items that belong to this category
            item.setCategory(this); //set the category reference in the item
        }
    }

    /**
     * Removes an item from the category
     * @param item The item to be removed
     */
    public void removeItem(Item item){
        if (item != null && items.contains(item)) {
            items.remove(item);
            item.setCategory(null);
        }
    }

    /**
     * Deletes the category and clears all its associated items
     */
    public void delete(){
        for (Item item : items){
            item.setCategory(null); //dissociate each item from this category
        }
        items.clear(); //clear all items in the category
    }
}

