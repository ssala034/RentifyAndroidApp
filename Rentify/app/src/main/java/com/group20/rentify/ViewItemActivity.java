package com.group20.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import com.group20.rentify.controller.CategoryController;
import com.group20.rentify.controller.ItemController;
import com.group20.rentify.controller.Subscriber;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.Category;
import com.group20.rentify.entity.Item;
import com.group20.rentify.entity.LessorRole;

import java.util.List;

public class ViewItemActivity extends ManageEntitiesActivity<Item> {

    private ItemController controller;
    private CategoryController categoryController;
    private List<Category> categoriesList;

    private EditText nameInput;
    private EditText descriptionInput;
    private EditText periodInput;
    private EditText feeInput;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.heading)).setText(R.string.itemPageTitle);

        categoryController = CategoryController.getInstance();
        initCategoryList();
    }

    @Override
    protected void initEntityList() {
        controller = ItemController.getInstance();
        entityList = controller.getItems(this);
    }

    private void initCategoryList(){

        categoriesList = categoryController.getCategories(
                new Subscriber<Category>() {
                    @Override
                    public void notify(List<Category> updatedList) {
                        categoriesList = updatedList;
                    }
                }
        );

    }

    @Override
    protected void onAddEntityPressed(View view) {
        showUpdateItemDialog(true,null);
    }

    @Override
    protected void beforeDeleteEntity(Item item) {
        controller.deleteItem(item);
    }

    @Override
    public void onEditEntity(Item item){
        showUpdateItemDialog(false,item);
    }

    private void showUpdateItemDialog(Boolean create,Item item){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_item, null);
        // testing dropdown menu
        Spinner spinnerCategories = dialogView.findViewById(R.id.categoryOptions);
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);

        dialogBuilder.setView(dialogView);

        if(create){
            ((TextView) dialogView.findViewById(R.id.textItemHeading)).setText("Add Iem");
        }else{
            ((TextView) dialogView.findViewById(R.id.textItemHeading)).setText("Edit Item");
            // category cannot be edited
            spinnerCategories.setVisibility(View.GONE);
            (dialogView.findViewById(R.id.textViewCategoryInstruction)).setVisibility(View.GONE);
        }
        final AlertDialog d = dialogBuilder.create();
        d.show();

        nameInput = dialogView.findViewById(R.id.textItemName);
        descriptionInput = dialogView.findViewById(R.id.textItemDescription);
        periodInput = dialogView.findViewById(R.id.textItemRentalPeriod);
        feeInput = dialogView.findViewById(R.id.textItemRentalFee);

        dialogView.findViewById(R.id.buttonCreateItem).setOnClickListener(v -> {

            boolean correct = validateName(nameInput);
            correct = validateDescription(descriptionInput) && correct;
            correct = validatePeriod(periodInput) && correct;
            correct = validateFee(feeInput) && correct; // force java to eval all four

            if(correct){
                String itemName = nameInput.getText().toString().trim();
                String itemDescription = descriptionInput.getText().toString().trim();
                double itemPeriod = Double.parseDouble(periodInput.getText().toString().trim());
                double itemFee = Double.parseDouble(feeInput.getText().toString().trim());
                category = (Category) spinnerCategories.getSelectedItem();

                if(create){
                   addItem(itemName,itemDescription,itemPeriod,itemFee,category);
                }else{
                    editItem(item,itemName,itemDescription,itemPeriod,itemFee);
                }
                d.dismiss();
            }

        });

    }

    private void editItem(Item item, String itemName, String itemDescription, double itemPeriod, double itemFee) {
        if (item != null) {
            controller.updateItem(item, itemName, itemDescription, itemPeriod, itemFee);
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating item", Toast.LENGTH_SHORT).show();
        }
    }

    private void addItem(String itemName, String itemDescription, double itemPeriod, double itemFee, Category category) {
        Item newItem = new Item(
                itemName,
                itemDescription,
                null,
                itemFee,
                itemPeriod,
                category,
                (LessorRole) Account.getSessionAccount().getAccountRole());

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
    //check if only numbers (including float)
    private boolean isNumeric(String input){
        return input.matches("\\d+(\\.\\d+)?");
    }

    private boolean isDollarFormat(String input) {
        return input.matches("^[0-9]+(\\.[0-9][0-9])?$");
    }

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
            if(!isNumeric(field.getText().toString())){
                field.setError("Period can only contain numbers");
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean validateFee(EditText field) {
        if(validateNotEmpty(field)){
            if(!isDollarFormat(field.getText().toString())){
                field.setError("Fee must be a number with 0 or 2 decimals");
                return false;
            }
            return true;
        }
        return false;
    }

}