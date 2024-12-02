package com.group20.rentify;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group20.rentify.adapter.SpecialEntityListAdapter;
import com.group20.rentify.controller.Subscriber;
import com.group20.rentify.entity.Entity;

import java.util.List;

public abstract class SpecialManageEntities<E extends Entity> extends AppCompatActivity implements SpecialEntityListAdapter.SpecialEntityActionListener<E>, Subscriber<E> {
    protected List<E> entityList;
    protected SpecialEntityListAdapter<E> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_item); // need right one !!
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEntitiesSpecial); // might have to change id if picks wrong one
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set LayoutManager

        initEntityList();

        adapter = new SpecialEntityListAdapter<>(entityList, this);
        recyclerView.setAdapter(adapter);
    }

    protected void onCreate(Bundle savedInstanceState, int layoutID) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(layoutID); // need right one !!
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEntitiesSpecial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set LayoutManager

        initEntityList();

        adapter = new SpecialEntityListAdapter<>(entityList, this);
        recyclerView.setAdapter(adapter);

    }



    /**
     * Initialise the entity list by calling the appropriate controller
     */
    protected abstract void initEntityList();



    @Override
    public void notify(java.util.List<E> updatedList) {
        entityList.clear();
        entityList.addAll(updatedList);
        adapter.notifyDataSetChanged();
    }


}
