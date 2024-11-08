package com.group20.rentify.entity;

public class Item implements Entity {

    // instance variables

    /**
     * The category in which the item belongs to
     */
    private Category category;

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
     * The rental fee of the item
     */
    private int rentalFee;

    /**
     * The rental time period of the item
     */
    private int rentalTime;

    //constructors

    public Item() {

    }

    /**
     * Full argument constructor.
     * @param name The name of the item
     * @param description The description providing information about the item
     * @param uniqueIdentifier The unique identifier of the item
     * @param rentalFee The price of renting the item
     * @param rentalTime The rental time period of the item
     * @param category The category in which the item belongs to
     */
    public Item(String name, String description, String uniqueIdentifier, int rentalFee, int rentalTime, Category category){
        this.name = name;
        this.description = description;
        this.uniqueIdentifier = uniqueIdentifier;
        this.rentalFee = rentalFee;
        this.rentalTime = rentalTime;
        this.category = category;
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
    public int getRentalFee(){
        return rentalFee;
    }

    /**
     * Getter for rentalTime attribute
     * @return rentalTime
     */
    public int getRentalTime(){
        return rentalTime;
    }

    /**
     * Getter for category
     * @return category
     */
    public Category getCategory() {
        return category;
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
    public void setRentalFee(int rentalFee){
        this.rentalFee = rentalFee;
    }

    /**
     * Setter for rentalTime attribute
     * @param rentalTime The rental time period of the item
     */
    public void setRentalTime(int rentalTime){
        this.rentalTime = rentalTime;
    }

    /**
     * Setter for item category
     * @param category
     */
    public void setCategory(Category category) {

    }

    public boolean hasCategory(){
        return category != null;
    }

}
