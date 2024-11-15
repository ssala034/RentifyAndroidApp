package com.group20.rentify;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group20.rentify.controller.CategoryAdapter;
import com.group20.rentify.controller.ItemController;
import com.group20.rentify.controller.Subscriber;
import com.group20.rentify.entity.Category;
import com.group20.rentify.entity.Item;


import java.util.List;

public class ViewItemActivity extends AppCompatActivity implements Subscriber<Item> {

    private ItemController controller;
    private List<Item> itemsList;

    private EditText nameInput;
    private EditText descriptionInput;
    private EditText periodInput;
    private EditText feeInput;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_item);

//        controller = ItemController.getInstance();
//
//        // Initialize RecyclerView and Adapter
//        RecyclerView recyclerView = findViewById(R.id.recyclerViewItems);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set LayoutManager
//        controller.getItems(this);

        //adapter = new CategoryAdapter(categoriesList, this);
        //recyclerView.setAdapter(adapter);

        FloatingActionButton addItemButton = findViewById(R.id.buttonAddItem);
        addItemButton.setOnClickListener(this:: onAddItemPressed);


    }
//when category is clicked open this page and send category with it?
    private void onAddItemPressed(View view) {
        showUpdateItemDialog(true,null);
    }
    private void onItemPressed(View view){
        //get category from item pressed
        //showUpdateItemDialog(false);
    }

    private void showUpdateItemDialog(Boolean create,Item item){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_item, null);
        //testing dropdown menu
        Spinner spinnerCategories = dialogView.findViewById(R.id.categoryOptions);

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerCategories.setAdapter(adapter);
        dialogBuilder.setView(dialogView);


        if(create){
            ((TextView) dialogView.findViewById(R.id.textItemHeading)).setText("Add Category");
        }else{
            ((TextView) dialogView.findViewById(R.id.textItemHeading)).setText("Edit Category");
        }
        final AlertDialog d = dialogBuilder.create();
        d.show();

        nameInput = dialogView.findViewById(R.id.textItemName);
        descriptionInput = dialogView.findViewById(R.id.textItemDescription);
        periodInput = dialogView.findViewById(R.id.textItemRentalPeriod);
        feeInput = dialogView.findViewById(R.id.textItemRentalFee);

        dialogView.findViewById(R.id.buttonCreateItem).setOnClickListener(v -> {
            String itemName = nameInput.getText().toString().trim();
            String itemDescription = descriptionInput.getText().toString().trim();
            int itemPeriod = Integer.parseInt(periodInput.getText().toString().trim());
            int itemFee = Integer.parseInt(feeInput.getText().toString().trim());
            category = spinnerCategories.getSelectedItem().toString();

            boolean correct =
            validateName(nameInput) &&
            validateDescription(descriptionInput) &&
            validatePeriod(periodInput) &&
            validateFee(feeInput);

            if(correct){
                if(create){
                   //addItem(itemName,itemDescription,itemPeriod,itemFee,category);
                }else{
                    editItem(item,itemName,itemDescription,itemPeriod,itemFee);
                }
                d.dismiss();
            }

        });

    }

    private void editItem(Item item, String itemName, String itemDescription, int itemPeriod, int itemFee) {
        if (item != null) {
            controller.updateItem(item, itemName, itemDescription, itemPeriod, itemFee);
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating item", Toast.LENGTH_SHORT).show();
        }
    }

    private void addItem(String itemName, String itemDescription, int itemPeriod, int itemFee, Category category) {
        Item newItem = new Item(itemName,itemDescription,null,itemFee,itemPeriod, category);

        boolean done = controller.addItem(newItem);
        if(done) {
            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Couldn't add item successfully", Toast.LENGTH_SHORT).show();
        }
    }

    //Generic Validation methods
    private boolean validateNotEmpty(EditText field) {
        if (field.getText().toString().isEmpty()) {
            field.setError("Input field is required");
            return false;
        }
        return true;
    }
    // Method 1: Checks if a string contains only alphabetic (Latin) characters and numbers.
    private  boolean isAlphaNumeric(String input) {
        return input.matches("[a-zA-Z0-9 ]+");
    }

    private boolean hasAlphaWithSpecialChars(String input) {
        return input.matches(".*[a-zA-Z ]+.*") && input.matches("[a-zA-Z0-9\\W ]+");
    }
    //check if only integers
    private boolean isNumeric(String input){return input.matches("\\d+");}

    //Specific validation methods
    private boolean validateName(EditText field) {
        if (validateNotEmpty(field)) {
            if (!isAlphaNumeric(field.getText().toString())) {
                field.setError("Name can only contain letters and numbers");
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean validateDescription(EditText field){
        if(validateNotEmpty(field)){
            if (!hasAlphaWithSpecialChars(field.getText().toString())) {
                field.setError("Description must contain letters and may contain special characters");
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean validatePeriod(EditText field){
        if(validateNotEmpty(field)){
            if(! isNumeric(field.getText().toString())){
                field.setError("Period can only contain numbers");
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean validateFee(EditText field) {
        if(validateNotEmpty(field)){
            if(! isNumeric(field.getText().toString())){
                field.setError("Fee can only contain numbers");
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void notify(List<Item> updatedList) {
        itemsList = updatedList;
        //adapter.notifyDataSetChanged();
    }
}