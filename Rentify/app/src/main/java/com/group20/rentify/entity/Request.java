package com.group20.rentify.entity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

public class Request implements Entity {

    /**
     * The renter making the request
     */
    RenterRole renter;

    /**
     * The item that the request is for
     */
    Item item;

    /**
     * Whether the request has been accepted
     */
    boolean accepted;

    /**
     * The unique identifier of the request
     */
    String uniqueIdentifier;

    /**
     * Constructor for request object
     *
     * @param renter
     * @param item
     */
    public Request(RenterRole renter, Item item) {
        this.renter = renter;
        this.item = item;
        this.accepted = false;
    }

    public Request(String id, RenterRole renter) {
        this.renter = renter;

        dataSaver.getField("requests/" + id + "/" + "itemId", (itemId) -> {
            dataSaver.getEntity("item", itemId, Item.class, (item) -> {
                this.item = item;
            });
        });

        dataSaver.getField("requests/" + id + "/" + "accepted", (accepted) -> {
            this.accepted = accepted.equals("true");
        });
    }

    public Request(String id, Item item) {
        this.item = item;

        dataSaver.getField("requests/" + id + "/" + "renterId", (renterId) -> {
            dataSaver.getAccount(renterId, (renter) -> {
                this.renter = (RenterRole) renter.getAccountRole();
            });
        });

        dataSaver.getField("requests/" + id + "/" + "accepted", (accepted) -> {
            this.accepted = accepted.equals("true");
        });
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
        return String.format("\tItem:\t%s\n\tUser:\t%s", item.getName(), renter.getUser().getName());
    }

    @Override
    public void delete() {
        // TODO remove the request from the renter and item list
        item.removeRequest(this);
        item.save();
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

    public boolean setAccepted(LessorRole caller, boolean accept) {
        if (!item.getOwner().equals(caller.getUser().getName())) {
            return false;  // only the owner of the item can call set accepted
        }

        this.accepted = accept;
        save();
        return true;
    }
}
