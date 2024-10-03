package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

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
    }

    public void signInClick(@NonNull View view){
        String email = ((EditText) findViewById(R.id.text_inputEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.text_inputPassword)).getText().toString();
        /* maybe use try catch
        // validate(email);
        //validate(password);
        */
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void createAccountClick(@NonNull View view){
        // go to create account activity
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }

}