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
    private RenterRole renter;

    /**
     * The item that the request is for
     */
    private Item item;

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
        renter.addRequest(this);
        this.item = item;
        item.addRequest(this);
        this.accepted = false;

        requests = new LinkedList<>();
        subscribers = new LinkedList<>();
    }

    public Request(String id, RenterRole renter) {
        this.renter = renter;
        renter.addRequest(this);

        dataSaver.getField("requests/" + id + "/" + "itemId", (itemId) -> {
            dataSaver.getEntity("item", itemId, Item.class, (item) -> {
                this.item = item;
                item.addRequest(this);
            });
        });

        dataSaver.getField("requests/" + id + "/" + "accepted", (accepted) -> {
            this.accepted = accepted.equals("true");
        });

        requests = new LinkedList<>();
        subscribers = new LinkedList<>();
    }

    public Request(String id, Item item) {
        this.item = item;
        item.addRequest(this);

        dataSaver.getField("requests/" + id + "/" + "renterId", (renterId) -> {
            dataSaver.getAccount(renterId, (renter) -> {
                this.renter = (RenterRole) renter.getAccountRole();
                this.renter.addRequest(this);
            });
        });

        dataSaver.getField("requests/" + id + "/" + "accepted", (accepted) -> {
            this.accepted = accepted.equals("true");
        });

        requests = new LinkedList<>();
        subscribers = new LinkedList<>();
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
        return String.format("%s\tItem:\t%s\n\tUser:\t%s",
                accepted ? "ACCEPTED\n" : "",
                item.getName(),
                renter.getUser().getName());
    }

    @Override
    public void delete() {
        // remove the request from the item's list
        item.removeRequest(this);
        item.save();

        // remove the requst from the renter's list
        renter.removeRequest(this);
        renter.getUser().save();

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
        return renter.getUser().getUniqueIdentifier();
    }

    @Exclude
    public Item getItem() {
        return item;
    }

    public String getItemId() {
        return item.getUniqueIdentifier();
    }

    public boolean getAccepted() {
        return accepted;
    }

    public boolean accept() {
        if (checkNotOwnerIllegalState()) {
            return false;
        }

        this.accepted = true;
        save();
        return true;
    }

    public boolean reject() {
        if (checkNotOwnerIllegalState()) {
            return false;
        }

        delete();
        return true;
    }

    private boolean checkNotOwnerIllegalState() {
        // only the owner of the item can accept or reject requests
        return !Account.getSessionAccount().getUsername().equals(item.getOwner());
    }

    public static List<Request> getRequests(Subscriber<Request> s) {
        return dataSaver.getRequests(s);
    }

}
