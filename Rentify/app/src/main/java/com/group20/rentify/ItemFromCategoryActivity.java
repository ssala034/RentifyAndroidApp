package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.group20.rentify.controller.ItemController;
import com.group20.rentify.controller.SaveDataController;
import com.group20.rentify.entity.Category;
import com.group20.rentify.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemFromCategoryActivity extends SpecialManageEntities<Item>{

    private static Category myCategory;
    private TextView heading;
    private SaveDataController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState, R.layout.activity_item_from_category);

//        Intent intent = getIntent();
//
//        String id = intent.getStringExtra("categoryId");
//        controller = ItemController.getInstance();
//
//        setMyCategory(id);
//        initEntityList();

        heading = findViewById(R.id.heading);
        String headingInfo = "Request for an Item from category: " + myCategory.getName();
        heading.setText(headingInfo);


    }

    @Override
    protected void initEntityList(){
        entityList = myCategory.getItems();
    }

//    @Override
//    protected void initEntityList(){
//        Intent intent = getIntent();
//        String id = intent.getStringExtra("categoryId");
//
//        controller = SaveDataController.getInstance();
//
//        controller.getEntity("category", id, Category.class, result ->{
//            if(result != null){
//                myCategory = result;
//                entityList = myCategory.getItems();
//                String headingInfo = "Request for an Item from category: " + myCategory.getName();
//
//                heading.setText(headingInfo);
//                if(adapter!= null)
//                    adapter.notifyDataSetChanged();
//            }
//        });
//
//
//    }


    public static void setMyCategory(Category  category){
        myCategory = category;
    }


    @Override
    public void onRequestEntity(Item item){
        RequestItemActivity.setItem(item);
        Intent intent = new Intent(this, RequestItemActivity.class);
//        intent.putExtra("itemId", item.getUniqueIdentifier());
        startActivity(intent);
        finish();
    }

//    public static void setCategory(Category category){
//        myCategory = category;
//    }


}
