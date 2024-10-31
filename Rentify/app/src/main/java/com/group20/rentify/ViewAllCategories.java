package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.divider.MaterialDivider;
import com.group20.rentify.controller.CategoryController;
import com.group20.rentify.entity.Category;

import java.util.List;

public class ViewAllCategories extends AppCompatActivity {

    private CategoryController controller;
    private Button manageCategories;
//    ListView categoriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_categories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = CategoryController.getInstance();

        manageCategories = findViewById(R.id.buttonManageCategories);
        manageCategories.setOnClickListener(this::onClickedManageCategories);

        displayCategories();
    }

    private void displayCategories() {
        List<Category> categories = controller.getCategories();
        LinearLayout categoriesList = findViewById(R.id.categoriesListView);

        for (Category category : categories) {
            createSingleDisplay(category.getName(), category.getDescription(), categoriesList);
        }
    }

    private void createSingleDisplay(String name, String description, LinearLayout view) {
        TextView toAdd = new TextView(this);
        toAdd.setText(String.format("\n%s\n\tName: %s\n\tDescription: %s\n", name, description));
        toAdd.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        toAdd.setTextSize(18);
        toAdd.setVisibility(TextView.VISIBLE);

        MaterialDivider divider = new MaterialDivider(this);
        divider.setMinimumHeight(2);

        view.addView(toAdd);
        view.addView(divider);
    }

    private void onClickedManageCategories(View view) {
        Intent intent = new Intent(this, ManageCategories.class);
        startActivity(intent);
        finish();
    }


}