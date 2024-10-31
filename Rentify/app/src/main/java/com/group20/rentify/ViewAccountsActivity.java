package com.group20.rentify;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.divider.MaterialDivider;
import com.group20.rentify.controller.ViewAccountsController;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.AccountRole;
import com.group20.rentify.entity.UserRole;

import java.util.List;

public class ViewAccountsActivity extends AppCompatActivity {

    private ViewAccountsController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_accounts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = ViewAccountsController.getInstance();
        displayAllUsernames();
    }

    private void displayAllUsernames() {
        // temporary for now instead of displaying all account
        Log.d("WelcomeActivity", "Display button clicked");
        List<Account> accounts = controller.getAccountList();
        LinearLayout accountList = findViewById(R.id.accountListView);

        for (Account account : accounts) {
            createSingleDisplay(account.getUsername(), account.getRole(), account.getEmail(), accountList);
        }
    }

    private void createSingleDisplay(String username, AccountRole role, String email, LinearLayout view) {
        TextView toAdd = new TextView(this);
        toAdd.setText(String.format("\n%s\n\tRole: %s\n\tEmail: %s\n", username, role, email));
        toAdd.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        toAdd.setTextSize(18);
        toAdd.setVisibility(TextView.VISIBLE);

        MaterialDivider divider = new MaterialDivider(this);
        divider.setMinimumHeight(2);

        view.addView(toAdd);
        view.addView(divider);
    }
}