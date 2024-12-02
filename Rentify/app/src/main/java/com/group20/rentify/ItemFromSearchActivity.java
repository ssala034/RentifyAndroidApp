package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.group20.rentify.adapter.SpecialEntityListAdapter;
import com.group20.rentify.controller.ItemController;
import com.group20.rentify.controller.SaveDataController;
import com.group20.rentify.entity.Category;
import com.group20.rentify.entity.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemFromSearchActivity extends SpecialManageEntities<Item>{

    private List<Item> items;
    private TextView heading;
    private SaveDataController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState, R.layout.activity_items_from_search);

        Intent intent = getIntent();

        List<String> itemIds =  intent.getStringArrayListExtra("item_ids");
        controller = SaveDataController.getInstance();

        setItems(itemIds);

        String prefix = intent.getStringExtra("prefix");
        heading = findViewById(R.id.heading);
        heading.setText("Search results for prefix: " +prefix);
    }


    @Override
    protected void initEntityList(){
        if(items!=null){
            entityList = items;
        }else{
            entityList = new ArrayList<Item>();
        }

    }

    public void setItems(List<String>  ids){
        final List<Item> res = new ArrayList<Item>();
        final int[] i = {0};
        for(String id: ids){
            controller.getEntity("item", id, Item.class, result -> {
                if(result!=null){
                    res.add(result);
                }

                if(i[0] == ids.size()-1){
                    items = res;
                    runOnUiThread(this::updateUI); // will update ui thread when category it set
                }
                i[0]++; // for asynchronous behaviour

            });
        }
    }

    public void updateUI(){
        int x;
        if(items!=null){
            notify(items);
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


//    public void setCategory(String  id){
//        controller.getEntity("category", id, Category.class, result -> {
//            if(result!=null){
//                myCategory = result;
//                runOnUiThread(this::updateUI); // will update ui thread when category it set
//            }
//        });
//    }

//    public void updateUI(){
//        if(myCategory!=null){
//            notify(myCategory.getItems());
//            String headingInfo = "Request for an Item from category: " + myCategory.getName();
//            heading.setText(headingInfo);
//        }
//    }