package com.group20.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.group20.rentify.controller.CategoryController;
import com.group20.rentify.entity.Category;

public class ManageCategoriesActivity extends ManageEntitiesActivity<Category> {

    private CategoryController controller;
    private EditText nameInput;
    private EditText descriptionInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.heading)).setText(R.string.categoryPageTitle);
    }

    @Override
    protected void initEntityList() {
        controller = CategoryController.getInstance();
        entityList = controller.getCategories(this);
    }

    @Override
    public void onEditEntity(Category category) {
        showAddUpdateDialog(false, category);
    }

    @Override
    protected void onAddEntityPressed(View view) {
        showAddUpdateDialog(true, null);
    }

    private void showAddUpdateDialog(boolean create, Category category) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_edit_category, null);
        dialogBuilder.setView(dialogView);

        if(create){
            ((TextView) dialogView.findViewById(R.id.textCategoryHeading)).setText("Add Category");
        }else{
            ((TextView) dialogView.findViewById(R.id.textCategoryHeading)).setText("Edit Category");
        }
        final AlertDialog b = dialogBuilder.create();
        b.show();

        nameInput = dialogView.findViewById(R.id.textCategoryName);
        descriptionInput = dialogView.findViewById(R.id.textDescription);

        dialogView.findViewById(R.id.buttonCreateCategory).setOnClickListener(view ->{
            String categoryName = nameInput.getText().toString().trim();
            String categoryDescription = descriptionInput.getText().toString().trim();

            // split call to prevent lazy eval from skipping second validation
            boolean correct = validateName(nameInput);
            correct = validateDescription(descriptionInput) && correct;

            if(correct){
                if(create){
                    addCategory(categoryName,categoryDescription);
                }else{
                    editCategory(category, categoryName, categoryDescription);
                }

                b.dismiss();
            }
        });
    }

    private void editCategory(Category category, String categoryName, String categoryDescription) {
        if (category != null) {
            controller.updateCategory(category, categoryName, categoryDescription);
            Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error updating category", Toast.LENGTH_SHORT).show();
        }

    }

    private void addCategory(String categoryName, String categoryDescription) {
        Category newCategory = new Category(categoryName, categoryDescription, null);
        boolean done = controller.addCategory(newCategory);
        if(done) {
            Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Couldn't add category successfully", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validateNotEmpty(EditText field) {
        if (field.getText().toString().isEmpty()) {
            field.setError("Input field is required");
            return false;
        }
        return true;
    }

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

    private boolean validateDescription(EditText field) {
        if (validateNotEmpty(field)) {
            if (!hasAlphaWithSpecialChars(field.getText().toString())) {
                field.setError("Description must contain letters and may contain special characters");
                return false;
            }
            return true;
        }
        return false;
    }

    // Method 1: Checks if a string contains only alphabetic (Latin) characters and numbers.
    private  boolean isAlphaNumeric(String input) {
        return input.matches("[a-zA-Z0-9 ]+");
    }


    private boolean hasAlphaWithSpecialChars(String input) {
        return input.matches(".*[a-zA-Z ]+.*") && input.matches("[a-zA-Z0-9\\W ]+");
    }
}
