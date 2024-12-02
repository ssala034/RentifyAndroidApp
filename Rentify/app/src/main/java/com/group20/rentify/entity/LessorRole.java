package com.group20.rentify.entity;

import com.google.firebase.database.Exclude;
import com.group20.rentify.controller.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LessorRole extends UserRole {

    private final List<Item> items;

    @Exclude private final List<Subscriber<Item>> subscribers;

    public LessorRole() {
        role = Role.lessor;
        items = new ArrayList<>();
        subscribers = new LinkedList<>();
    }

    public LessorRole(Account account) {
        super(account);
        items = new ArrayList<>();
        subscribers = new LinkedList<>();
    }

    public void addItem(Item item) {
        if (item != null) {
            items.add(item);

            for (Subscriber<Item> s: subscribers) {
                s.notify(items);
            }

            getUser().save();
        }
    }

    public void removeItem(Item item) {
        if (item != null) {
            items.remove(item);
            item.delete();

            for (Subscriber<Item> s: subscribers) {
                s.notify(items);
            }

            getUser().save();
        }
    }

    public void refreshList() {
        for (Subscriber<Item> s: subscribers) {
            s.notify(items);
        }

        getUser().save();
    }

    public void loadFurther() {
        for (Item item: items) {
            item.loadFurther(null);
        }
    }

    @Override
    public void delete() {
        for (Item item : items) {
            item.delete();
        }
    }

    @Exclude
    public List<Item> getItems(Subscriber<Item> s) {
        subscribers.add(s);
        return items;
    }

    /**
     * Compute and return a map of all the requests for this lessor, keyed by item.
     * <p>
     *     NOTE: the subscriber will receive a new LIST each time update is called, ignore this.
     *     The update method notifies the subscriber that the map they have has changed.
     *     The map object is changed directly, so the subscriber only needs to call a re-render method.
     * </p>
     *
     * @param s     the subscriber which will receive a notification whenever the requests change
     * @return      the map of requests
     */
    @Exclude
    public Map<Item, List<Request>> getRequests(Subscriber<Request> s) {
        Map<Item, List<Request>> requests = new HashMap<>();

        for (Item item : items) {
            requests.put(item, item.getRequests(s));
        }

        return requests;
    }

    public List<Item> getItems() {  // for firebase data saving should not be used
        return items;
    }
}
