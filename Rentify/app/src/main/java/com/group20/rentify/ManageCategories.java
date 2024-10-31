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

import java.util.ArrayList;
import java.util.List;

import com.group20.rentify.entity.Category;


public class ManageCategories extends AppCompatActivity {

    private boolean isManaging = false;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_categories);
        test();
        //test of addCategoryView, replace with proper
        //addCategory("Test1","hello i am test 1","01");
        LinearLayout parent = findViewById(R.id.categoriesListView);
        for (Category category : categories) {
            addCategoryView(parent, category.getName(), category.getDescription());
        }

        Button manage = findViewById(R.id.buttonManageCategories);
        manage.setOnClickListener(this::onManageCategoriesButtonClicked);

    }
    private void onManageCategoriesButtonClicked(View view) {
        Button button = (Button) view;
        //does not have functionality rn so commented as to not break code
        //List<Category> categories = controller.getCategories();
        if(isManaging){
            button.setText("Manage Categories");
            ImageView edit = findViewById(R.id.buttonEditCategory);
            edit.setVisibility(ImageView.GONE);
            isManaging = false;
        } else{
            button.setText("Done");
            ImageView edit = findViewById(R.id.buttonEditCategory);
            edit.setVisibility(ImageView.VISIBLE);

            isManaging = true;
        }

    }
    protected void addCategoryView(LinearLayout parentLayout, String title, String description){
        //Use this method when creating new categories to get correct layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View categoryView = inflater.inflate(R.layout.view_screen,parentLayout,false);
        //setting up delete button tag
        ImageView delete = categoryView.findViewById(R.id.buttonDeleteCategory);
        Category currCategory = categories.get(categories.size() - 1);
        delete.setTag(currCategory);

        //individual text, title and description
        TextView categoryTitle = categoryView.findViewById(R.id.textName);
        TextView categoryDescription = categoryView.findViewById(R.id.textDescription);

        categoryTitle.setText(title);
        categoryDescription.setText(description);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategoryView(v, parentLayout);
            }
        });
        //add to layout
        parentLayout.addView(categoryView);
    }

    private void deleteCategoryView(View view, LinearLayout parent){
        Category categoryToDelete = (Category) view.getTag();

        View categoryView = (View) view.getParent();
        parent.removeView(categoryView);

        categories.remove(categoryToDelete);

    }

    private void test(){
       categories = new ArrayList<>();
       Category category;
       for(int i = 0; i < 5; i++){
           category = new Category("Test " +i, "hello", "0"+i);
           categories.add(category);
       }
    }
}