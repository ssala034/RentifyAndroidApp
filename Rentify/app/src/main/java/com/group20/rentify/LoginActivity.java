package com.group20.rentify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group20.rentify.controller.FormController;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private FormController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // create listeners
        emailInput = findViewById(R.id.text_inputEmail);
        passwordInput = findViewById(R.id.text_inputPassword);

        createFocusListeners();

        // initialize controller
        controller = FormController.getInstance();
    }

    public void signInClick(@NonNull View view) {
        if (validateNotEmpty(emailInput) && validateNotEmpty(passwordInput)) {
            String email = emailInput.getText().toString();
            String password = controller.hashPassword(
                    passwordInput.getText().toString()
            );

            controller.verifyLogin(email, password,
                    success -> {
                        if (success) {
                            Intent intent = new Intent(this, WelcomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this,
                                    "Incorrect Username or Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void createAccountClick(@NonNull View view){
        // go to create account activity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void createFocusListeners() {
        emailInput.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                validateNotEmpty(emailInput);
            }
        });

        passwordInput.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                validateNotEmpty(passwordInput);
            }
        });
    }

    private boolean validateNotEmpty(EditText field) {
        if (field.getText().toString().isEmpty()) {
            field.setError("Input field is required");
            return false;
        }
        return true;
    }

}