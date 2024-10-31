package com.group20.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import com.group20.rentify.entity.Category;


public class ManageCategories extends AppCompatActivity {

    //List<Category> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_categories);
        //test of addCategoryView, replace with proper
        //addCategory("Test1","hello i am test 1","01");
        LinearLayout parent = findViewById(R.id.categoriesListView);

        addCategoryView(parent,"Test2","hello i am test 2");

        Button manage = findViewById(R.id.buttonManageCategories);

        manage.setOnClickListener(this::onManageCategoriesButtonClicked);

    }

    private void onManageCategoriesButtonClicked(View view) {
       Button button = (Button) view;
       button.setText("Done");
       // loop through categories in categoryList from controller
        /*
        List<Category> categories = controller.getCategories();
        for (Category category : categories) {
            ImageView delete = findViewById(R.id.buttonDeleteCategory);
            delete.setVisibility(ImageView.VISIBLE);
            ImageView edit = findViewById(R.id.buttonEditCategory);
            edit.setVisibility(ImageView.VISIBLE);
        }*/


    }

    private void addCategoryView(LinearLayout parentLayout, String title, String description){
        //Use this method when creating new categories to get correct layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View categoryView = inflater.inflate(R.layout.view_screen,parentLayout,false);

        //individual text, title and description
        TextView categoryTitle = categoryView.findViewById(R.id.textName);
        TextView categoryDescription = categoryView.findViewById(R.id.textDescription);

        categoryTitle.setText(title);
        categoryDescription.setText(description);


        //add to layout
        parentLayout.addView(categoryView);
    }


}