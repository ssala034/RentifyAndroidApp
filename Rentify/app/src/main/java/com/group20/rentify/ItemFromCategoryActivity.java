package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.group20.rentify.adapter.SpecialEntityListAdapter;
import com.group20.rentify.controller.SaveDataController;
import com.group20.rentify.entity.Category;
import com.group20.rentify.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemFromCategoryActivity extends SpecialManageEntities<Item>{

    private Category myCategory;
    private TextView heading;
    private SaveDataController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState, R.layout.activity_item_from_category);

        Intent intent = getIntent();

        String id = intent.getStringExtra("categoryId");
        controller = SaveDataController.getInstance();

        setCategory(id);

        heading = findViewById(R.id.heading);
    }

    @Override
    protected void initEntityList(){
        if(myCategory != null){
            entityList = myCategory.getItems();
        }else{
            entityList = new ArrayList<Item>();
        }
    }

    public void setCategory(String  id){
        controller.getEntity("category", id, Category.class, result -> {
            if(result!=null){
                myCategory = result;
                runOnUiThread(this::updateUI); // will update ui thread when category it set
            }
        });
    }

    public void updateUI(){
        if(myCategory!=null){
            notify(myCategory.getItems());
            String headingInfo = "Request for an Item from category: " + myCategory.getName();
            heading.setText(headingInfo);
        }
    }


    @Override
    public void onRequestEntity(Item item){
        Intent intent = new Intent(this, RequestItemActivity.class);
        intent.putExtra("itemId", item.getUniqueIdentifier());
        startActivity(intent);
        finish();
    }
}