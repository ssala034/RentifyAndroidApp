package com.group20.rentify.controller;

import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.RenterRole;
import com.group20.rentify.entity.Request;

import java.util.List;

public class RequestController {
    private static RequestController instance;

    public RequestController(){}

    public static RequestController getInstance(){
        if(instance == null){
            instance = new RequestController();
        }

        return instance;
    }

    /**
     * Save a request into the database.
     * @param request                       The request to save to the database
     * @return                          True if request is saved successfully
     */
    public boolean addRequest( Request request ){ // should admin logic go here??
        request.save();
        return true;
    }

    public void deleteRequest(Request request) {
        ((RenterRole) (Account.getSessionAccount().getAccountRole())).removeRequest(request);
    }


    public List<Request> getRequests(Subscriber<Request> s) {
        return Request.getRequests(s);
    }


}
