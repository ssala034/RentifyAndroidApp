package com.group20.rentify.entity;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.group20.rentify.controller.Subscriber;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Item implements Entity {

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

    private String categoryId;

    /**
     * The lessor who created the listing for the item -- READ ONLY
     */
    private String owner;

    private Account ownerObject;

    /**
     * The requests that have been made on the item
     */
    @Exclude private final List<Request> requests;
    private final List<String> requestIds;
    @Exclude private final List<Subscriber<Request>> subscribers;

    //constructors
    public Item() {  // necessary for firebase; however normally Item would require a category to create
        requests = new LinkedList<>();
        requestIds = new LinkedList<>();
        subscribers = new LinkedList<>();
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
//        if(uniqueIdentifier == null){
//            dataSaver.generateUniqueIdentifier();
//        }else{
            this.uniqueIdentifier = uniqueIdentifier;
//        }
        this.rentalFee = rentalFee;
        this.rentalTime = rentalTime;

        this.categoryId = category.getUniqueIdentifier();
        setCategory(category);
      
        this.owner = owner.getUser().getUsername();
        ownerObject = owner.getUser();
        owner.addItem(this);

        requests = new LinkedList<>();
        requestIds = new LinkedList<>();
        subscribers = new LinkedList<>();
    }

    public static List<Item> getItems(Subscriber<Item> s) {
        return dataSaver.getItems(s);
    }

    @Override
    public void delete() {
        if (owner != null) {
            // this only updates firebase, assumes that if the account was loaded to disk
            // deletion is handled already
            dataSaver.getAccount(owner, data -> {
                owner = null;
                ((LessorRole) data.getAccountRole()).removeItem(this);
                data.save();
            });
        } else {
            if (category != null) {  // null check in case category is deleted first
                Category tempRef = category;
                category.removeItem(this);
                tempRef.save();
            }

            for (Request request : requests) {
                request.delete();
            }

            dataSaver.removeEntity(this);
        }
    }

    @Override
    public void save() {
        dataSaver.saveEntity(this);

        if (ownerObject != null) {
            ownerObject.save();
        } else if (owner != null) {
            dataSaver.getAccount(owner, Account::save);
        }

        if (category != null) {
            category.save();
        }

        for (Request request : requests) {
            request.save();
        }
    }

    @Override
    public void loadFurther(DataSnapshot ds) {
        for(Category c: dataSaver.getCategories(new Subscriber<Category>() {@Override public void notify(List<Category> updatedList) {}})){
            if(c.getUniqueIdentifier().equals(categoryId)){
                category = c;
            }
        }

        for (String id : requestIds) {
            requests.add(new Request(id, this));
        }
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
    @Exclude
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

    public String getCategoryId() {
        return categoryId;
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
            //category and owner must be updated afterwards to be synced with actual item (has an id)
            category.addItem(this);
            dataSaver.getAccount(owner, data -> {
                ((LessorRole) data.getAccountRole()).addItem(this);
            });

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
        if (category != null && !category.equals(this.category)) {
            if (this.category != null) {
                this.category.removeItem(this);
                this.category.save();
            }

            category.addItem(this);
            category.save();
        }

        this.category = category;
    }

    public void addRequest(Request request) {
        if (request != null && !requests.contains(request)) {
            requests.add(request);
            requestIds.add(request.getUniqueIdentifier());
            for (Subscriber<Request> s : subscribers) {
                s.notify(requests);
            }
        }
    }

    public void removeRequest(Request request) {
        if (request != null) {
            if (requests.remove(request)) {
                requestIds.remove(request.getUniqueIdentifier());
                for (Subscriber<Request> s : subscribers) {
                    s.notify(requests);
                }
            }
        }
    }

    @Exclude
    public List<Request> getRequests(Subscriber<Request> s) {
        subscribers.add(s);
        return requests;
    }

    public List<String> getRequestIds() {  // DO NOT USE - for firebase
        return requestIds;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Item other = (Item) obj;
        return (getUniqueIdentifier() == null && other.getUniqueIdentifier() == null)
                || (other.getUniqueIdentifier() != null
                && other.getUniqueIdentifier().equals(getUniqueIdentifier()));
    }
}
