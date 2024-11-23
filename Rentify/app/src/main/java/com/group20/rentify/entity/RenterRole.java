package com.group20.rentify.entity;

import java.util.LinkedList;
import java.util.List;

public class RenterRole extends UserRole {
    private final List<Request> requests;
    private final List<String> requestIds;

    public RenterRole() {
        role = Role.renter;
        requests = new LinkedList<>();
        requestIds = new LinkedList<>();
    }

    public RenterRole(Account account) {
        super(account);
        requests = new LinkedList<>();
        requestIds = new LinkedList<>();
    }

    public void addRequest(Request request) {
        if (request != null && !requests.contains(request)) {
            requests.add(request);
            requestIds.add(request.getUniqueIdentifier());
        }
    }

    public void removeRequest(Request request) {
        if (request != null) {
            if (requests.remove(request)) {
                requestIds.remove(request.getUniqueIdentifier());
            }
        }
    }

    @Override
    public void delete() {
        for (Request request : requests) {
            request.delete();
        }
    }

    @Override
    public void loadFurther() {
        for (String id : requestIds) {
            requests.add(new Request(id, this));
        }
    }

    public List<String> getRequestIds() {  // DO NOT USE - for firebase
        return requestIds;
    }
}
