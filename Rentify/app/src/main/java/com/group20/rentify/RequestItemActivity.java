package com.group20.rentify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group20.rentify.controller.SaveDataController;
import com.group20.rentify.entity.Account;
import com.group20.rentify.entity.Item;
import com.group20.rentify.entity.RenterRole;
import com.group20.rentify.entity.Request;

public class RequestItemActivity extends AppCompatActivity {


    private  Item myItem;
    private Button requestBtn;
    private TextView name;
    private TextView description;
    private TextView fee;
    private TextView time;
    private TextView owner;

    private SaveDataController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String id = intent.getStringExtra("itemId");
        controller = SaveDataController.getInstance();
        setItem(id);



        name = findViewById(R.id.reqName);
        description = findViewById(R.id.reqDescription);
        fee = findViewById(R.id.reqFee);
        time = findViewById(R.id.reqTime);
        name = findViewById(R.id.reqName);
        owner = findViewById(R.id.reqOwner);

        requestBtn = findViewById(R.id.btnRequest);
        requestBtn.setOnClickListener(this::onRequestPressed);
    }

    private void onRequestPressed(View view) {
        // make request to owner, make sure connections to requests are correct

        RenterRole renter =  (RenterRole) Account.getSessionAccount().getAccountRole();
        Request currRequest = new Request(renter, myItem);

        currRequest.save();
        Account.getSessionAccount().save();
        myItem.save();

        Toast.makeText(this, "Request sent", Toast.LENGTH_SHORT).show();
    }

    public void setItem(String id){
        controller.getEntity("item", id, Item.class, result ->{
            if(result != null){
                myItem = result;
                runOnUiThread(this::updateUI); // will update ui thread when item it set
            }
        });
    }

    private void updateUI() {
        if (myItem != null) {
            String nameInfo = "Item Name: " + myItem.getName();
            String descriptionInfo = "Item Description: " + myItem.getDescription();
            String feeInfo = "Item Rental Fee: $" + myItem.getRentalFee();
            String timeInfo = "Item Rental Time: " + myItem.getRentalTime() + " days";
            String ownerInfo = "Item Owner: " + myItem.getOwner();

            name.setText(nameInfo);
            description.setText(descriptionInfo);
            fee.setText(feeInfo);
            time.setText(timeInfo);
            owner.setText(ownerInfo);
        } else {
            Log.d("Error", "Item is null, cannot update UI");
        }
    }
}