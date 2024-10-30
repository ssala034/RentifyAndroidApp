package com.group20.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewAllCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_categories);

        LinearLayout parent = findViewById(R.id.categoriesListView);
        addCategoryView(parent,"Test1","i am test 1");
        addCategoryView(parent,"Test","i am a test");
        addCategoryView(parent,"Test","i am a test");



    }


    private void addCategoryView(LinearLayout parentLayout, String title, String description){
        //Use this method when creating new categories to get correct layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View categoryView = inflater.inflate(R.layout.view_category,parentLayout,false);

        //individual text, title and description
        TextView categoryTitle = categoryView.findViewById(R.id.categoryTitle);
        TextView categoryDescription = categoryView.findViewById(R.id.categoryDetails);

        categoryTitle.setText(title);
        categoryDescription.setText(description);
        //add to layout
        parentLayout.addView(categoryView);
    }
}