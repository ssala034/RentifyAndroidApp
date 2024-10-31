package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group20.rentify.controller.CategoryController;
import com.group20.rentify.entity.Category;

import java.util.List;

public class ManageCategories extends AppCompatActivity {

    private CategoryController controller;
    private Button deleteCategoryBtn;
    private Button editCategory1Btn;
    private Button editCategory2Btn;
    private Button addCategoryBtn;

    private ListView categoriesListView;

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

        // Initialize buttons
        addCategoryBtn = (Button) findViewById(R.id.buttonAddCategory);

//        editCategory1Btn = (Button) findViewById(R.id.buttonEditCategory);
//        editCategory2Btn = (Button) findViewById(R.id.buttonEditCategory2);
//        deleteCategoryBtn = (Button) findViewById(R.id.buttonDeleteCategory1); // ??
//
//        categoriesListView = (ListView) findViewById(R.id.categoriesListView);

        // Set listeners
        addCategoryBtn.setOnClickListener(this::onAddCategoryBtnClicked);
        editCategory1Btn.setOnClickListener(this::onEditCategory1BtnClicked);
        editCategory2Btn.setOnClickListener(this::onEditCategory2BtnClicked);

        // How to make it to just one button???


        // load all categories
        loadCategories();
    }

    /*Fix*/
    private void loadCategories() {
        List<Category> categories = controller.getCategories();

        for(Category category: categories){
            View categoryView = getLayoutInflater().inflate(R.layout.activity_manage_categories, categoriesListView, false);

            TextView categoryNameTextView = categoryView.findViewById(R.id.category1Details);

            ImageView deleteBtn = categoryView.findViewById(R.id.buttonDeleteCategory1); // how can I make so just a category tag??


            deleteBtn.setTag(category.getName());

            // Set click listener to delete the category
            deleteBtn.setOnClickListener(v -> {
                String name = (String) v.getTag(); // Retrieve the tag
                deleteCategory(name);
            });

            // Add the category view to the list
            categoriesListView.addView(categoryView);
        }
    }

    /*NEED TO FIX THIS SO IT WORKS ON DELETING A CATEGORY, NOT WELL RIGHT NOW ASK FOR HELP*/
    private void deleteCategory(String categoryName) {
        Category tmp = controller.getCategory(categoryName);
        controller.removeCategory(tmp.getUniqueIdentifier());

        showConfirmationDialog("Delete Category " + tmp.getName(), () -> {
            controller.removeCategory(tmp.getUniqueIdentifier());
            Toast.makeText(this, "Category 2 deleted", Toast.LENGTH_SHORT).show();
        });

        // Refresh the category list
        categoriesListView.removeAllViews();

        loadCategories();  // Reload the list to reflect deletion
    }


    private void onEditCategory1BtnClicked(View view) {
        Intent intent = new Intent(this, EditCategory.class);
        startActivity(intent);
    }

    private void onEditCategory2BtnClicked(View view) {
        Intent intent = new Intent(this, EditCategory.class);
        startActivity(intent);
    }

    private void onAddCategoryBtnClicked(View view) {
        // should have heading added on next page !!!! FROM THIS POINT
        Intent intent = new Intent(this, EditCategory.class);
        intent.putExtra("action", "add"); // Indicate adding a new category
        startActivity(intent);
        finish();
    }

    private void showConfirmationDialog(String message, Runnable onConfirm) {
        // Implement a dialog to confirm deletion
        // On confirm, call onConfirm.run();
        new AlertDialog.Builder(this)
                .setTitle("Confirm Action")
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> {
                    onConfirm.run(); // Execute the confirmed action
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Dismiss on "No"
                .setCancelable(false) // Prevent closing by clicking outside
                .show();
    }


}