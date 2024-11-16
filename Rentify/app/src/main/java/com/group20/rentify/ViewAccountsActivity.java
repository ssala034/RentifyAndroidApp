package com.group20.rentify;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group20.rentify.adapter.EntityList;
import com.group20.rentify.controller.Subscriber;
import com.group20.rentify.controller.ViewAccountsController;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.Entity;
import com.group20.rentify.entity.UserRole;

import java.util.ArrayList;
import java.util.List;

public class ViewAccountsActivity extends AppCompatActivity implements Subscriber<Account> {

    private ViewAccountsController controller;
    private ListView accountList;

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
        accountList = findViewById(R.id.accountListView);
        List<Account> accounts = controller.getAccountList(this);

        accountList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Account account = accounts.get(i);
            showDisableDeleteDialog(account);
            return true;
        });

        displayAllUsernames(accountList, accounts);
    }

    @Override
    public void notify(List<Account> updatedList) {
        displayAllUsernames(accountList, updatedList);
    }

    private void displayAllUsernames(ListView accountList, List<Account> accounts) {
        // temporary for now instead of displaying all account
        Log.d("WelcomeActivity", "Display button clicked");
        updateDisplayList(accounts, accountList);
    }

    private void updateDisplayList(List<Account> accounts, ListView view) {
        List<Entity> castedList = new ArrayList<>(accounts);
        view.setAdapter(new EntityList(ViewAccountsActivity.this, castedList));
    }

    private void showDisableDeleteDialog(Account account) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_manage_account_dialogue, null);
        dialogBuilder.setView(dialogView);

        ((TextView) dialogView.findViewById(R.id.dialogueTitle)).setText("Manage Account");
        ((TextView) dialogView.findViewById(R.id.accountName)).setText(account.getName());
        ((TextView) dialogView.findViewById(R.id.accountDescription)).setText(account.getDescription());

        if (account.getRole() == UserRole.Role.admin) {
            dialogView.findViewById(R.id.buttonDisable).setVisibility(View.GONE);
        }

        final AlertDialog b = dialogBuilder.create();
        b.show();

        dialogView.findViewById(R.id.buttonDisable).setOnClickListener(view -> {
            if (controller.disableAccount(account)) {
                Toast.makeText(getApplicationContext(), "Account disabled successfully.", Toast.LENGTH_SHORT).show();
            }
            b.dismiss();
        });

        dialogView.findViewById(R.id.buttonDelete).setOnClickListener(view -> {
            showDeleteConfirmationDialogue(account);
            b.dismiss();
        });
    }

    private void showDeleteConfirmationDialogue(Account account) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_confirmation_dialogue, null);
        dialogBuilder.setView(dialogView);

        ((TextView) dialogView.findViewById(R.id.dialogueMessage)).setText(
                "Warning: Deleting account will permanently delete all related data"
        );

        Button confirmButton = dialogView.findViewById(R.id.buttonConfirm);
        Button cancelButton = dialogView.findViewById(R.id.buttonCancel);

        confirmButton.setText("Delete");
        cancelButton.setText("Cancel");

        dialogBuilder.setTitle("Confirm Delete");

        final AlertDialog b = dialogBuilder.create();
        b.show();

        confirmButton.setOnClickListener(view -> {
            controller.deleteAccount(account);
            Toast.makeText(getApplicationContext(), "Account deleted successfully.", Toast.LENGTH_SHORT).show();
            b.dismiss();
        });

        cancelButton.setOnClickListener(view -> {
            b.dismiss();
        });
    }

}