package com.group20.rentify.entity;

import com.google.firebase.database.Exclude;
import com.group20.rentify.controller.Subscriber;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    @Exclude
    public List<Item> getItems(Subscriber<Item> s) {
        subscribers.add(s);
        return items;
    }

    public List<Item> getItems() {  // for firebase data saving should not be used
        return items;
    }
}
