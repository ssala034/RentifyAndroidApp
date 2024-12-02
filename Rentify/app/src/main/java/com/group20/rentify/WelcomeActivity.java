package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.AdminRole;
import com.group20.rentify.entity.UserRole;

public class WelcomeActivity extends AppCompatActivity {

    public static final String GREETING = "Hello";
    public static final String WELCOME = "Welcome to Rentify!";
    public static final String ROLE_PREFIX = "You are logged in as";

    private Button launchMainScreen;
    private boolean skip;
    private Account signedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set up listeners
        ((CheckBox) findViewById(R.id.checkBox)).setOnCheckedChangeListener(
                (view, isChecked) -> skip = isChecked
        );

        launchMainScreen = findViewById(R.id.btnLaunchMain);
        launchMainScreen.setOnClickListener(this::onStartExploringPressed);

        syncWithState();
    }

    private void syncWithState() {
        signedIn = Account.getSessionAccount();
        ((TextView) findViewById(R.id.RegisterTitle)).setText(
                String.format("%s, %s!\n%s", GREETING, signedIn.getFirstName(), WELCOME)
        );
        ((TextView) findViewById(R.id.userRole)).setText(
                String.format("%s: %s", ROLE_PREFIX, signedIn.getRole())
        );

        if (skip) {
            onStartExploringPressed(launchMainScreen);
        }
    }

    private void onStartExploringPressed(View view) {
        if (signedIn.getRole() == UserRole.Role.admin) {
            Intent intent = new Intent(this, AdminDashboard.class);
            startActivity(intent);
            finish();
        } else if (signedIn.getRole() == UserRole.Role.lessor || signedIn.getRole() == UserRole.Role.lesser) {
            Intent intent = new Intent(this, ViewItemActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, SearchItemActivity.class);
            startActivity(intent);
            finish();
        }
    }
}