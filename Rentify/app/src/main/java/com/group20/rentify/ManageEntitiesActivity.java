package com.group20.rentify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group20.rentify.adapter.EntityListAdapter;
import com.group20.rentify.controller.Subscriber;
import com.group20.rentify.entity.Entity;

import java.util.List;

public abstract class ManageEntitiesActivity<E extends Entity> extends AppCompatActivity implements EntityListAdapter.EntityActionListener<E>, Subscriber<E> {
    protected List<E> entityList;
    protected EntityListAdapter<E> adapter;
    protected FloatingActionButton addEntityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_entities);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEntities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set LayoutManager

        initEntityList();

        adapter = new EntityListAdapter<>(entityList, this);
        recyclerView.setAdapter(adapter);

        // Set up the add button listener
        addEntityButton = findViewById(R.id.buttonAddEntity);
        addEntityButton.setOnClickListener(this::onAddEntityPressed);
    }

    // TODO @Emily use this constructor to send your custom adapter when inheriting from this class
    //      i.e. super.onCreate(savedInstanceState, new EntityListAdapter<Request>(...))
    protected void onCreate(Bundle savedInstanceState, EntityListAdapter<E> adapter) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEntities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set LayoutManager

        initEntityList();

        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    /**
     * Initialise the entity list by calling the appropriate controller
     */
    protected abstract void initEntityList();

    protected abstract void onAddEntityPressed(View view);

    /**
     * Hook called whenever entity is deleted
     */
    protected void onDeleteSuccess() {
    }

    /**
     * Hook called before entity is deleted
     * Can be used to do any extra cleanup necessary before deleting an entity
     */
    protected void beforeDeleteEntity(E entity) {

    }

    @Override
    public void notify(List<E> updatedList) {
        entityList = updatedList;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteEntity(E entity) {
        showDeleteConfirmationDialogue(entity);
    }

    private void showDeleteConfirmationDialogue(E entity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_confirmation_dialogue, null);
        dialogBuilder.setView(dialogView);

        ((TextView) dialogView.findViewById(R.id.dialogueMessage)).setText(
                "Warning: Deleting " + entity.getEntityTypeName() + " will permanently delete all related data"
        );

        Button confirmButton = dialogView.findViewById(R.id.buttonConfirm);
        Button cancelButton = dialogView.findViewById(R.id.buttonCancel);

        confirmButton.setText("Delete");
        cancelButton.setText("Cancel");

        dialogBuilder.setTitle("Confirm Delete");

        final AlertDialog b = dialogBuilder.create();
        b.show();

        confirmButton.setOnClickListener(view -> {
            // Handle delete action
            beforeDeleteEntity(entity);
            int position = entityList.indexOf(entity);
            if (position >= 0) {
                entityList.remove(position);
                adapter.notifyItemRemoved(position);
                entity.delete();
                onDeleteSuccess();
            }
            b.dismiss();
        });

        cancelButton.setOnClickListener(view -> {
            b.dismiss();
        });
    }
}
