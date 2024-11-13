package com.group20.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewItemActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText descriptionInput;
    private EditText periodInput;
    private EditText feeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_item);


        FloatingActionButton addItemButton = findViewById(R.id.buttonAddItem);
        addItemButton.setOnClickListener(this:: onAddItemPressed);
    }

    private void onAddItemPressed(View view) {
        showAddItemDialog();
    }

    private void showAddItemDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_item, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog d = dialogBuilder.create();
        d.show();

        nameInput = dialogView.findViewById(R.id.textItemName);
        descriptionInput =dialogView.findViewById(R.id.textItemDescription);
        periodInput =dialogView.findViewById(R.id.textItemRentalPeriod);
        feeInput =dialogView.findViewById(R.id.textItemRentalFee);

        dialogView.findViewById(R.id.buttonCreateItem).setOnClickListener(v -> {
            String itemName = nameInput.getText().toString().trim();
            String itemDescription = descriptionInput.getText().toString().trim();
            int itemPeriod = Integer.parseInt(periodInput.getText().toString().trim());
            int itemFee = Integer.parseInt(feeInput.getText().toString().trim());

        });

    }
    //Generic Validation methods
    private boolean validateNotEmpty(EditText field) {
        if (field.getText().toString().isEmpty()) {
            field.setError("Input field is required");
            return false;
        }
        return true;
    }
    // Method 1: Checks if a string contains only alphabetic (Latin) characters and numbers.
    private  boolean isAlphaNumeric(String input) {
        return input.matches("[a-zA-Z0-9 ]+");
    }

    private boolean hasAlphaWithSpecialChars(String input) {
        return input.matches(".*[a-zA-Z ]+.*") && input.matches("[a-zA-Z0-9\\W ]+");
    }

    //Specific validation methods
    



}