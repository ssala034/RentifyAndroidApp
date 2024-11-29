package com.group20.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.group20.rentify.controller.ViewAccountsController;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.UserRole;

public class ViewAccountsActivity extends ManageEntitiesActivity<Account> {

    private ViewAccountsController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) findViewById(R.id.heading)).setText(R.string.accountPageTitle);
        addEntityButton.setVisibility(View.GONE);
    }

    @Override
    protected void initEntityList() {
        controller = ViewAccountsController.getInstance();
        entityList = controller.getAccountList(this);
    }

    @Override
    protected void onAddEntityPressed(View view) {
        throw new IllegalStateException("Cannot add Accounts");
    }

    @Override
    protected void onDeleteSuccess() {
        Toast.makeText(getApplicationContext(), "Account deleted successfully.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditEntity(Account entity) {
        showDisableDialogue(entity);
    }

    private void showDisableDialogue(Account account) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_confirmation_dialogue, null);
        dialogBuilder.setView(dialogView);

        ((TextView) dialogView.findViewById(R.id.dialogueMessage)).setText(
                account.displayDetails()
        );

        Button disableButton = dialogView.findViewById(R.id.buttonConfirm);
        Button enableButton = dialogView.findViewById(R.id.buttonCancel);

        disableButton.setText("Disable");
        enableButton.setText("Enable");

        dialogBuilder.setTitle("Edit Account");

        if (account.getRole() == UserRole.Role.admin) {
            ((TextView) dialogView.findViewById(R.id.dialogueMessage)).setText(
                    "Cannot edit admin accounts"
            );
            disableButton.setVisibility(View.GONE);
            enableButton.setVisibility(View.GONE);
        }

        final AlertDialog b = dialogBuilder.create();
        b.show();

        disableButton.setOnClickListener(view -> {
            if (controller.disableAccount(account)) {
                Toast.makeText(getApplicationContext(), "Account disabled successfully.", Toast.LENGTH_SHORT).show();
            }
            b.dismiss();
        });

        enableButton.setOnClickListener(view -> {
            if (controller.enableAccount(account)) {
                Toast.makeText(getApplicationContext(), "Account enabled successfully.", Toast.LENGTH_SHORT).show();
            }
            b.dismiss();
        });
    }

}