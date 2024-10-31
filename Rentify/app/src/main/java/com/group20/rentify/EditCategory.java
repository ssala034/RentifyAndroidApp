package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group20.rentify.controller.CategoryController;
import com.group20.rentify.entity.Category;

public class EditCategory extends AppCompatActivity {

    private CategoryController controller;
    private TextView textCategoryHeading;
    private boolean isAdd;

    private EditText nameInput;
    private EditText descriptionInput;

    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textCategoryHeading = findViewById(R.id.textCategoryHeading);
        controller = CategoryController.getInstance();

        nameInput = findViewById(R.id.textCategoryName);
        descriptionInput = findViewById(R.id.textCategoryDescription);

        doneButton = findViewById(R.id.buttonCreateCategory);

        // make a boolean
         Intent intent = getIntent();

         String action = intent.getStringExtra("action");

        if ("add".equals(action)) {
            textCategoryHeading.setText("Add Category");
            isAdd = true;
        } else {
            textCategoryHeading.setText("Edit Category"); // Default case for editing
            isAdd = false;
        }

        doneButton.setOnClickListener(this::onDoneBtnClicked);

    }

    private void onDoneBtnClicked(View view) {
        String categoryName = nameInput.getText().toString().trim();
        String categoryDescription = descriptionInput.getText().toString().trim();

        if(isAdd){
            addCategory(categoryName,categoryDescription);
        }else{
            editCategory(categoryName, categoryDescription);
        }
    }

    private void editCategory(String categoryName, String categoryDescription) {

        if(!verifyName(categoryName)){
            // fix so it is on enter!!!!!!, NOT just on Done button
            Toast.makeText(this, "Enter Category Name in Database", Toast.LENGTH_SHORT).show();
            nameInput.setError("Please edit an existing category");
        }else{

            Category category = controller.getCategory(categoryName);
            if (category != null) {
//                String id = category.getUniqueIdentifier();
                controller.updateCategory(category, categoryDescription);

                // shouldn't be able to change category name
                Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error updating category", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean verifyName(String categoryName) {
        for(Category c: controller.getCategories()){
            if(c.getName().equals(categoryName)){
                return true;
            }
        }
        return false;
    }

    private void addCategory(String categoryName, String categoryDescription) {
        Category newCategory = new Category(categoryName, categoryDescription, "901");
        boolean done = controller.addCategory(newCategory);
        if(done) {
            Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
        }else{
            Log.d("Database Error", "Couldn't add category successfully");
        }

    }
}