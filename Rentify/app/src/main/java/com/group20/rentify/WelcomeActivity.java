package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.RoleName;

public class WelcomeActivity extends AppCompatActivity {

    public static final String GREETING = "Hello";
    public static final String WELCOME = "Welcome to Rentify!";
    public static final String ROLE_PREFIX = "You are logged in as";

    private Button viewAccountBtn;

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
        viewAccountBtn = findViewById(R.id.viewAccountsBtn);
        viewAccountBtn.setOnClickListener(this::onViewAccountsBtnClicked);

        syncWithState();
    }

    private void syncWithState() {
        Account signedIn = Account.getSessionAccount();
        ((TextView) findViewById(R.id.RegisterTitle)).setText(
                String.format("%s, %s!\n%s", GREETING, signedIn.getFirstName(), WELCOME)
        );
        ((TextView) findViewById(R.id.userRole)).setText(
                String.format("%s: %s", ROLE_PREFIX, signedIn.getRole())
        );

        if (signedIn.getRole().getRoleName() == RoleName.admin) {
            setUpAdminPrivileges();
        }
    }

    private void setUpAdminPrivileges() {
        viewAccountBtn.setVisibility(TextView.VISIBLE);
    }

    private void onViewAccountsBtnClicked(View view) {
        Intent intent = new Intent(this, ViewAccountsActivity.class);
        startActivity(intent);
        finish();
    }
}