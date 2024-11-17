package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group20.rentify.controller.FormController;
import com.group20.rentify.entity.UserRole;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText usernameInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText passwordInput;
    private EditText passwordReInput;

    private FormController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initialize controller
        controller = FormController.getInstance();

        // create listeners
        emailInput = findViewById(R.id.emailEnter);
        usernameInput = findViewById(R.id.usernameEnter);
        firstNameInput = findViewById(R.id.firstNameEnter);
        lastNameInput = findViewById(R.id.lastNameEnter);
        passwordInput = findViewById(R.id.passwordEnter);
        passwordReInput = findViewById(R.id.passwordEnter2);

        createFocusListeners();

        // create click listener
        findViewById(R.id.btnRegister).setOnClickListener(this::onRegisterBtnClicked);
        findViewById(R.id.adminCreate).setOnClickListener(this::onAdminCheckToggled);
    }

    private void onRegisterBtnClicked(View view) {
        if (validateForm()) {
            if (createAccount()) {
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this,
                    "Please complete all required fields",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void onAdminCheckToggled(View view) {
        enableRoleRadioGroup(!((CheckBox) view).isChecked());
    }

    private void enableRoleRadioGroup(boolean enable) {
        findViewById(R.id.renterSelect).setEnabled(enable);
        findViewById(R.id.lenderSelect).setEnabled(enable);
    }

    private void createFocusListeners() {
        emailInput.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                validateEmailInput();
            }
        });

        usernameInput.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                validateUsernameInput();
            }
        });

        passwordInput.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                validatePasswordInput(passwordInput);
            }
        });

        passwordReInput.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                validatePasswordInput(passwordReInput);
            }
        });
    }

    private boolean createAccount() {
        try {
            return controller.createAccount(
                    usernameInput.getText().toString(),
                    emailInput.getText().toString(),
                    getRole(),
                    firstNameInput.getText().toString(),
                    lastNameInput.getText().toString(),
                    controller.hashPassword(passwordInput.getText().toString())
            );
        } catch (IllegalStateException e) {
            Toast.makeText(this,
                    "Creation of more than one admin account currently not supported",
                    Toast.LENGTH_SHORT).show();
            findViewById(R.id.adminCreate).setEnabled(false);
            ((CheckBox) findViewById(R.id.adminCreate)).setChecked(false);
            enableRoleRadioGroup(true);
            return false;
        }
    }

    private boolean validateNotEmpty(EditText field) {
        if (field.getText().toString().isEmpty()) {
            field.setError("Input field is required");
            return false;
        }
        return true;
    }

    private boolean validateEmailInput() {
        if (validateNotEmpty(emailInput)) {
            if (!controller.verifyEmail(emailInput.getText().toString())) {
                emailInput.setError("Email should be in the form someone@email.id");
                return false;
            }

            if (!controller.verifyUniqueEmail(emailInput.getText().toString())) {
                emailInput.setError("Email already associated with existing account");
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean validateUsernameInput() {
        if (validateNotEmpty(usernameInput)) {
            if (!controller.verifyUsername(usernameInput.getText().toString())) {
                usernameInput.setError("Username already taken");
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean validatePasswordInput(EditText passwordField) {
        if (validateNotEmpty(passwordField)) {
            String complexityMessage = controller.verifyPasswordComplexity(passwordField.getText().toString());
            if (!complexityMessage.isEmpty()) {
                passwordField.setError(complexityMessage);
                return false;
            }

            String password = controller.hashPassword(passwordInput.getText().toString());
            String rePassword = controller.hashPassword(passwordReInput.getText().toString());
            if (!controller.verifyPassword(password, rePassword)) {
                passwordInput.setError("Passwords do not match");
                passwordReInput.setError("Passwords do not match");
                return false;
            }
            // clear the error messages
            passwordInput.setError(null);
            passwordReInput.setError(null);

            return true;
        }
        return false;
    }

    private boolean validateForm() {
        // call each validate to ensure screen logic is executed even with lazy boolean eval
        boolean usernameCheck = validateUsernameInput();
        boolean emailCheck = validateEmailInput();
        boolean passwordCheck = validatePasswordInput(passwordInput) && validatePasswordInput(passwordReInput);

        return usernameCheck && emailCheck && passwordCheck;
    }

    private UserRole.Role getRole() {
        CheckBox admin = findViewById(R.id.adminCreate);
        if (admin.isChecked()) {
            return UserRole.Role.admin;
        } else {
            int selected = ((RadioGroup) findViewById(R.id.selectRole)).getCheckedRadioButtonId();
            return selected == R.id.renterSelect ? UserRole.Role.renter : UserRole.Role.lesser;
        }
    }
}