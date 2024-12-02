package com.group20.rentify;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.group20.rentify.adapter.EntityListAdapter;
import com.group20.rentify.controller.RequestController;
import com.group20.rentify.entity.Request;

import java.util.ArrayList;
import java.util.List;

public class ViewRequestsActivity extends ManageEntitiesActivity<Request> {

    private List<Request> requestList;
    private RequestController controller;
    private String itemName;

    protected void onCreate(Bundle savedInstanceState) {

        String itemId = getIntent().getStringExtra("itemID");
        itemName = getIntent().getStringExtra("itemName");

        controller = RequestController.getInstance();

        List<Request> allRequests = controller.getRequests(this);

        requestList = new ArrayList<>();

        for (Request request: allRequests){
            if(request.getItemId().equals(itemId)){
                requestList.add(request);
            }
        }

        super.onCreate(savedInstanceState,
                new EntityListAdapter<>(requestList,this,R.layout.layout_request_list_item)
        );

        ((TextView) findViewById(R.id.heading)).setText("Requests for " + itemName);

    }

    @Override
    protected void initEntityList() {
        entityList = requestList;
    }

    @Override
    //Not used for requests
    protected void onAddEntityPressed(View view) {}

    @Override
    //Rejecting request
    public void onDeleteEntity(Request request) {
        if(!request.getAccepted()){
            if(request.reject()) {
                request.delete();
                Toast.makeText(this, "Request successfully rejected", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Request already accepted", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    //Accepting request
    public void onEditEntity(Request request) {
        if(!request.getAccepted()){
            if(request.accept()){
                Toast.makeText(this, "Request successfully accepted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}