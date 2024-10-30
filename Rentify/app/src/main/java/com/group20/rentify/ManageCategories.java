package com.group20.rentify;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.group20.rentify.databinding.ActivityManageCategoriesBinding;

public class ManageCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_categories);
        //test of addCategoryView, replace with proper
        LinearLayout parent = findViewById(R.id.categoriesListView);
        addCategoryView(parent,"Test1","hello i am test 1");
        addCategoryView(parent,"test2", "hello i am test 2!!");
        addCategoryView(parent,"test3", "hello i am test 3!!!");

    }

    private void addCategoryView(LinearLayout parentLayout, String title, String description){
        //Use this method when creating new categories to get correct layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View categoryView = inflater.inflate(R.layout.manage_category,parentLayout,false);

        //individual text, title and description
        TextView categoryTitle = categoryView.findViewById(R.id.textCategoryTitle);
        TextView categoryDescription = categoryView.findViewById(R.id.textCategoryDescription);

        categoryTitle.setText(title);
        categoryDescription.setText(description);
        //add to layout
        parentLayout.addView(categoryView);
    }
}