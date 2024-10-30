package com.group20.rentify;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

public class ManageCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        TestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_categories);
        binding.textCategoryTitle.setText("hello");


    }

    private void addText(String text){
        binding.textCategoryTitle.setText("hello world");
    }

    /*
     * TestBinding binding = DataBindingUtil.setContentView(this, activity_manage_categories);
     * binding.textCategoryTitle.setText("hello world");
     *
     * */

    //HelloWorldBinding binding =
    //    DataBindingUtil.setContentView(this, R.layout.hello_world);
    //binding.hello.setText("Hello World");
}