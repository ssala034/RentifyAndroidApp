package com.group20.rentify.entity;

import com.group20.rentify.controller.SaveDataController;
import com.group20.rentify.controller.Subscriber;

import java.util.List;

public class Item implements Entity {

    private static final SaveDataController dataSaver = SaveDataController.getInstance();

    // instance variables

    /**
     * The name of the item
     */
    private String name;

    /**
     * The description of the item
     */
    private String description;

    /**
     * The unique identifier for the item
     */
    private String uniqueIdentifier;

    /**
     * The rental fee of the item in dollars (flat fee)
     */
    private double rentalFee;

    /**
     * The rental time period of the item in days
     */
    private double rentalTime;

    /**
     * The category in which the item belongs to
     */
    private Category category;

    /**
     * The lessor who created the listing for the item -- READ ONLY
     */
    private String owner;

    //constructors
    public Item() {  // necessary for firebase; however normally Item would require a category to create

    }

    /**
     * Full argument constructor.
     * @param name The name of the item
     * @param description The description providing information about the item
     * @param uniqueIdentifier The unique identifier of the item
     * @param rentalFee The price of renting the item
     * @param rentalTime The rental time period of the item
     * @param category The category in which the item belongs to
     * @param owner The username of the lessor the item belongs to
     * @throws IllegalArgumentException if the category is null
     */
    public Item(String name, String description, String uniqueIdentifier,
                double rentalFee, double rentalTime,
                Category category, LessorRole owner){
        if (category == null){
            throw new IllegalArgumentException("An item must belong to a category!");
        }

        if (owner == null) {
            throw new IllegalArgumentException("An item must belong to a lessor");
        }

        this.name = name;
        this.description = description;
        this.uniqueIdentifier = uniqueIdentifier;
        this.rentalFee = rentalFee;
        this.rentalTime = rentalTime;

        category.addItem(this);
        this.category = category;

        owner.addItem(this);
        this.owner = owner.getUser().getUsername();
    }

    public static List<Item> getItems(Subscriber<Item> s) {
        return dataSaver.getItems(s);
    }

    @Override
    public void delete() {
        // do not need to remove from owner first, assume that owner will from list before calling delete
        if (category != null) {  // null check in case category is deleted first
            category.removeItem(this);
        }
        dataSaver.removeEntity(this);
    }

    @Override
    public void save() {
        dataSaver.saveEntity(this);
    }

    // getters

    /**
     * Gets the name of the entity
     * @return "item"
     */
    @Override
    public String getEntityTypeName() {
        return "item";
    }

    @Override
    public String displayDetails() {
        return String.format("\t%-20s%s\n\t%-20s$%s\n\t%-20s%s\n\n%s",
                "Category:", category,
                "Rental Fee:", rentalFee,
                "Rental Time:", rentalTime,
                description
        );
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

    /**
     * Getter for rentalFee attribute
     * @return rentalFee
     */
    public double getRentalFee(){
        return rentalFee;
    }

    /**
     * Getter for rentalTime attribute
     * @return rentalTime
     */
    public double getRentalTime(){
        return rentalTime;
    }

    /**
     * Getter for category
     * @return category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Getter for owner
     * @return  owner
     */
    public String getOwner() {
        return owner;
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
     * Setter for rentalFee attribute
     * @param rentalFee The rental fee price of the item
     */
    public void setRentalFee(double rentalFee){
        this.rentalFee = rentalFee;
    }

    /**
     * Setter for rentalTime attribute
     * @param rentalTime The rental time period of the item
     */
    public void setRentalTime(double rentalTime){
        this.rentalTime = rentalTime;
    }

    /**
     * Setter for category attribute
     * @param category The category in which this item belongs to
     */
    public void setCategory(Category category){
        if (category != this.category) {
            if (this.category != null) {
                this.category.removeItem(this);
            }
            if (category != null) {
                category.addItem(this);
            }
            this.category = category;
        }
    }

}
