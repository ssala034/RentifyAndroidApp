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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group20.rentify.controller.CategoryAdapter;
import com.group20.rentify.controller.CategoryController;
import com.group20.rentify.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class ManageCategories extends AppCompatActivity implements CategoryAdapter.CategoryActionListener {

    private CategoryController controller;
    private List<Category> categoriesList;
    private CategoryAdapter adapter;

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
        categoriesList = controller.getCategories();
        adapter = new CategoryAdapter(categoriesList, this);
        recyclerView.setAdapter(adapter);


        // Set up the add button listener
        FloatingActionButton addCategoryButton = findViewById(R.id.buttonAddCategory);
        addCategoryButton.setOnClickListener(v -> {

            Intent intent = new Intent(ManageCategories.this, EditCategory.class);
            intent.putExtra("action", "add");
            startActivityForResult(intent, 1); // Request code 1 for adding

            // Add a new category
//            Category newCategory = new Category("New Category", "Description", null);
//            categoriesList.add(newCategory);
//            adapter.notifyItemInserted(categoriesList.size() - 1);
        });
    }

    @Override
    public void onDeleteCategory(Category category) {
        // Handle delete action
        int position = categoriesList.indexOf(category);
        if (position >= 0) {
            categoriesList.remove(position);
            adapter.notifyItemRemoved(position);
            controller.removeCategory(category.getUniqueIdentifier());
        }
    }

    @Override
    public void onEditCategory(Category category) {

        Intent intent = new Intent(ManageCategories.this, EditCategory.class);
        intent.putExtra("action", "edit");
        intent.putExtra("category", category);
        startActivityForResult(intent, 2); // Request code 2 for editing



//        // Handle edit action - you could open a new activity or dialog to edit the category
//        int position = categoriesList.indexOf(category);
//        if (position >= 0) {
//            // Open edit dialog or activity (Example shown with a Toast for simplicity)
//            Toast.makeText(this, "Editing: " + category.getName(), Toast.LENGTH_SHORT).show();
//
//            // Here, you might update the category's details in a dialog, then refresh the view
//            category.setName("Edited Title"); // Simulate edit
//            category.setDescription("Updated Details");
//            adapter.notifyItemChanged(position);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("categoryName");
            String description = data.getStringExtra("categoryDescription");

            if (requestCode == 1) { // Adding a new category
//                Category newCategory = new Category(name, description, null);
//                controller.addCategory(newCategory);
//                categoriesList.add(newCategory);
            } else if (requestCode == 2) { // Editing an existing category
                Category category = controller.getCategory(name);
                if (category != null) {
                    category.setDescription(description);
                    category.setName(name);
                    controller.updateCategory(category, description);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

}





/*
*  private void loadCategories() {
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
* private void onEditCategory1BtnClicked(View view) {
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

*     /*NEED TO FIX THIS SO IT WORKS ON DELETING A CATEGORY, NOT WELL RIGHT NOW ASK FOR HELP*/

/*

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
        private Button deleteCategoryBtn;
    private Button editCategory1Btn;
    private Button editCategory2Btn;
    private Button addCategoryBtn;
 */
