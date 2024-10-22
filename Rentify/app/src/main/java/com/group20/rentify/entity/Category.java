package com.group20.rentify.entity;

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


    // constructors

    public Category() {

    }

    /**
     * Full argument constructor.
     * @param name The name of the category
     * @param description The description providing information about the category
     */
    public Category(String name, String description){
        this.name = name;
        this.description = description;
    }


    // getters

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
     * Overrides method from interface Entity
     * <p>
     * Return the name of the entity type, usually the same as the class name
     * </p>
     */
    @Override
    public String getEntityTypeName() {
        return "";
    }

    /**
     * Overrides method from interface Entity
     * <p>
     * Return the identifier which uniquely identifies the entity
     * </p>
     */
    @Override
    public String getUniqueIdentifier() {
        return "";
    }

    /**
     *
     * Overrides method from interface Entity
     * <p>
     * Set the identifier which uniquely identifies the entity
     *</p>
     * @param id The new identifier
     * @return True if the new identifier is set successfully, false otherwise
     */
    @Override
    public boolean setUniqueIdentifier(String id) {
        return false;
    }
}

