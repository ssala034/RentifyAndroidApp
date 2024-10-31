package com.group20.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.group20.rentify.entity.Category;


public class ManageCategories extends AppCompatActivity {

    private boolean isManaging = false;
    private final List<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_categories);
        //parent cannot be a local variable, has to be in on create
        LinearLayout parent = findViewById(R.id.categoriesListView);

        for(int i = 0; i < 20; i++){
            addCategory();
        }

        Button manage = findViewById(R.id.buttonManageCategories);
        manage.setOnClickListener(this::onManageCategoriesButtonClicked);

        ImageView delete = parent.findViewById(R.id.buttonDeleteCategory);
        delete.setOnClickListener(this:: onDeleteButtonClicked);


    }



    private void onManageCategoriesButtonClicked(View view) {
        Button button = (Button) view;
        //does not have functionality rn so commented as to not break code
        //List<Category> categories = controller.getCategories();
        if(isManaging){
            button.setText(R.string.text_manage_categories);
            ImageView edit = findViewById(R.id.buttonEditCategory);
            edit.setVisibility(ImageView.GONE);
            isManaging = false;
        } else{
            button.setText(R.string.text_done);
            ImageView edit = findViewById(R.id.buttonEditCategory);
            edit.setVisibility(ImageView.VISIBLE);


            isManaging = true;
        }

    }
    private void addCategoryView(LinearLayout parentLayout,String title, String description){
        Category currCategory = categories.get(categories.size() - 1);


        //Use this method when creating new categories to get correct layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View categoryView = inflater.inflate(R.layout.view_screen,parentLayout,false);

        ImageView delete = categoryView.findViewById(R.id.buttonDeleteCategory);
        delete.setTag(currCategory);
        delete.setOnClickListener(this:: onDeleteButtonClicked);


        //individual text, title and description
        TextView categoryTitle = categoryView.findViewById(R.id.textName);
        TextView categoryDescription = categoryView.findViewById(R.id.textDescription);

        categoryTitle.setText(title);
        categoryDescription.setText(description);


        //add to layout
        parentLayout.addView(categoryView);
    }

    private void onDeleteButtonClicked(View view) {
        int id = view.getId();
        Category tag = (Category) view.getTag();
        View category = (View) view.getParent().getParent();
        ViewGroup parentLayout = (ViewGroup) category.getParent();
        parentLayout.removeView(category);
        categories.remove(tag);


    }

    private void addCategory(){
        int i = categories.size();
        Category newOne = new Category("Test" +i, "hello", "0"+i);
        categories.add(newOne);
        String title = newOne.getName();
        String description = newOne.getDescription();
        LinearLayout parent = findViewById(R.id.categoriesListView);
        addCategoryView(parent,title,description);

    }
}