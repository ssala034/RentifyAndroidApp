package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group20.rentify.adapter.SpecialEntityListAdapter;
import com.group20.rentify.controller.CategoryController;
import com.group20.rentify.entity.Category;
import com.group20.rentify.entity.Item;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchItemActivity extends SpecialManageEntities<Category> {
    private CategoryController controller;
    private EditText itemName;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this::onSearchPressed);
        itemName = findViewById(R.id.textItemName);
    }

    private void onSearchPressed(View view){
        String name = itemName.getText().toString().trim();


        ArrayList<String> itemIds;
        if(validateNotEmpty(itemName)) {
            itemIds = itemNames(name);
            if (!itemIds.isEmpty()) {
                // go to request activity, pass item found
                if(itemIds.size() == 1) {
                    Intent intent = new Intent(this, RequestItemActivity.class);
                    intent.putExtra("itemId", itemIds.get(0));
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(this, ItemFromSearchActivity.class);
                    intent.putStringArrayListExtra("item_ids", itemIds);
                    intent.putExtra("prefix", name);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(this, "Item does not exits. Re-select an existing item", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Field is Empty", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void initEntityList(){
        controller = CategoryController.getInstance();
        entityList = controller.getCategories(this);
    }

    @Override
    public void onRequestEntity(Category category){
        if(category.getItems().isEmpty()){
            Toast.makeText(this, "Category has no items. Pick another category to choose items from!", Toast.LENGTH_SHORT).show();
            return;
        }
        // go to the next activity & pass the list of items from the chosen category

        Intent intent = new Intent(this, ItemFromCategoryActivity.class);
        intent.putExtra("categoryId", category.getUniqueIdentifier());
        startActivity(intent);
        finish();
    }

    private ArrayList<String> itemNames(String name){
        ArrayList<String> res = new ArrayList<String>();
        for(Category category : entityList){
            for(Item item: category.getItems()) {
                if (item.getName().startsWith(name)) {
                    res.add(item.getUniqueIdentifier());
                }
            }
        }
        return res;
    }

    private boolean validateNotEmpty(EditText field) {
        if (field.getText().toString().isEmpty()) {
            field.setError("Input field is required");
            return false;
        }
        return true;
    }
}
