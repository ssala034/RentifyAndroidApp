package com.group20.rentify.entity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.group20.rentify.controller.Subscriber;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Request implements Entity {

    /**
     * The renter making the request
     */
    @Exclude private RenterRole renter;

    /**
     * The item that the request is for
     */
    @Exclude private Item item;


    private String renterId;
    private String itemId;

    /**
     * Whether the request has been accepted
     */
    private boolean accepted;

    /**
     * The unique identifier of the request
     */
    private String uniqueIdentifier;

    /**
     * The list of requests
     */
    private final List<Request> requests;

    @Exclude private final List<Subscriber<Request>> subscribers;

    //constructors
    public Request() {  // necessary for firebase; however normally Request would require a more variables to create
        requests = new LinkedList<>();
        subscribers = new LinkedList<>();
    }

    /**
     * Constructor for request object
     *
     * @param renter    the renter making the request
     * @param item      the item that the request is for
     */
    public Request(RenterRole renter, Item item) {
        this.renter = renter;
        this.renterId = renter.getUser().getUsername();

        renter.addRequest(this);

        this.item = item;
        this.itemId = item.getUniqueIdentifier();
        item.addRequest(this);
        this.accepted = false;

        requests = new LinkedList<>();
        subscribers = new LinkedList<>();

        requests.add(this);
    }

    public Request(String id, RenterRole renter) {
        this.renter = renter;
        this.renterId = renter.getUser().getUsername();

        renter.addRequest(this);

        dataSaver.getField("requests/" + id + "/" + "itemId", (itemId) -> {
            dataSaver.getEntity("item", itemId, Item.class, (item) -> {
                this.item = item;
                this.itemId = item.getUniqueIdentifier();
                item.addRequest(this);
            });
        });

        dataSaver.getField("requests/" + id + "/" + "accepted", (accepted) -> {
            this.accepted = accepted.equals("true");
        });

        requests = new LinkedList<>();
        subscribers = new LinkedList<>();

        requests.add(this);
    }

    public Request(String id, Item item) {
        this.item = item;
        this.itemId = item.getUniqueIdentifier();
        item.addRequest(this);

        dataSaver.getField("requests/" + id + "/" + "renterId", (renterId) -> {
            dataSaver.getAccount(renterId, (renter) -> {
                this.renter = (RenterRole) renter.getAccountRole();
                this.renterId = this.renter.getUser().getUsername();
                this.renter.addRequest(this);
            });
        });

        dataSaver.getField("requests/" + id + "/" + "accepted", (accepted) -> {
            this.accepted = accepted.equals("true");
        });

        requests = new LinkedList<>();
        subscribers = new LinkedList<>();

        requests.add(this);
    }

    @Override
    public String getEntityTypeName() {
        return "request";
    }

    @Override
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    @Override
    public boolean setUniqueIdentifier(String id) {
        if (id != null && !id.isEmpty()){
            this.uniqueIdentifier = id;
            return true;
        }
        return false;
    }

    @Override
    @Exclude
    public String getName() {
        return "Request number: " + getUniqueIdentifier();
    }

    @Override
    public String displayDetails() {
        return String.format("%s\n\tUser:\t%s",
                accepted ? "ACCEPTED\n" : "", renterId);
    }

    @Override
    public void delete() {
        // item does not have a list of request
        // renter does not have a list of requests

        requests.remove(this);

        dataSaver.removeEntity(this);
    }

    @Override
    public void save() {
        dataSaver.saveEntity(this);
    }

    @Override
    public void loadFurther(DataSnapshot ds) {
        // do nothing (requests are never loaded directly)
    }

    @Exclude
    public Account getRenter() {
        return renter.getUser();
    }

    public String getRenterId() {
        return renterId;
    }

    @Exclude
    public Item getItem() {
        return item;
    }

    public String getItemId() {
        return itemId; // item.getUniqueIdentifier()
    }

    public boolean getAccepted() {
        return accepted;
    }

    public boolean accept() {

        this.accepted = true;
        save();
        return true;
    }

    public boolean reject() {

        delete();
        return true;
    }

    private boolean checkNotOwnerIllegalState() {
        // only the owner of the item can accept or reject requests
        //not necessary because logged in earlier
        return !Account.getSessionAccount().getUsername().equals(item.getOwner());
    }

    public static List<Request> getRequests(Subscriber<Request> s) {
        return dataSaver.getRequests(s);
    }

}
