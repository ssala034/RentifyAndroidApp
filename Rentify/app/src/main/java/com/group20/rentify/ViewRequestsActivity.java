package com.group20.rentify;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.group20.rentify.adapter.EntityListAdapter;
import com.group20.rentify.controller.RequestController;
import com.group20.rentify.entity.Item;
import com.group20.rentify.entity.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewRequestsActivity extends ManageEntitiesActivity<Request> {

    private List<Request> requestList;
    private RequestController controller;

    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

        String itemId = getIntent().getStringExtra("itemId");

        controller = RequestController.getInstance();

        Map<Item,List<Request>> requestMap = controller.getRequests(this);

        Set<Item> itemSet = requestMap.keySet();

        for(Item item: itemSet){
            if(item.getUniqueIdentifier().equals(itemId)){
                requestList = requestMap.get(item);
            }
        }

        super.onCreate(savedInstanceState,
                new EntityListAdapter<>(requestList,this,R.layout.layout_entity_list_item)
        );

        ((TextView) findViewById(R.id.heading)).setText(R.string.requestPageTitle);

    }

    @Override
    protected void initEntityList() {
        entityList = requestList;
    }

    @Override
    protected void onAddEntityPressed(View view) {}

    @Override
    public void onDeleteEntity(Request entity) {

    }


    @Override
    public void onEditEntity(Request entity) {

    }

    //item has .getRequests() -> List<Request>
}