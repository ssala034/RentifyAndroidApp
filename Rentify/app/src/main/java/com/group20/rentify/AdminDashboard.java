package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set up listeners
        findViewById(R.id.viewUsersBtn).setOnClickListener(this::onViewAccountsBtnPressed);
        findViewById(R.id.showCategoriesBtn).setOnClickListener(this::onShowCategoriesBtnPressed);
    }

    private void onViewAccountsBtnPressed(View view) {
        Intent intent = new Intent(this, ViewAccountsActivity.class);
        startActivity(intent);
    }

    private void onShowCategoriesBtnPressed(View view) {
        Intent intent = new Intent(this, ManageCategoriesActivity.class);
        startActivity(intent);
    }
}