package com.group20.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.group20.rentify.controller.CategoryController;
import com.group20.rentify.controller.Subscriber;
import com.group20.rentify.entity.Category;

import java.util.List;

public class ManageCategories extends AppCompatActivity implements CategoryAdapter.CategoryActionListener, Subscriber<Category> {

    private CategoryController controller;
    private List<Category> categoriesList;
    private CategoryAdapter adapter;
    private EditText nameInput;
    private EditText descriptionInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_categories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = CategoryController.getInstance();

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set LayoutManager
        categoriesList = controller.getCategories(this);
        adapter = new CategoryAdapter(categoriesList, this);
        recyclerView.setAdapter(adapter);


        // Set up the add button listener
        FloatingActionButton addCategoryButton = findViewById(R.id.buttonAddCategory);
        addCategoryButton.setOnClickListener(this::onAddCategoryPressed);
    }

    @Override
    public void notify(List<Category> updatedList) {
        categoriesList = updatedList;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteCategory(Category category) {
        // Handle delete action
        int position = categoriesList.indexOf(category);
        if (position >= 0) {
            categoriesList.remove(position);
            adapter.notifyItemRemoved(position);
            category.delete();
        }
    }

    @Override
    public void onEditCategory(Category category) {
        showAddUpdateDialog(false, category);

    }

    public void onAddCategoryPressed(View view) {
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
