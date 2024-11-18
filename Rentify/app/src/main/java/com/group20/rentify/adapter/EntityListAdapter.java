package com.group20.rentify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group20.rentify.R;
import com.group20.rentify.entity.Entity;

import java.util.List;

public class EntityListAdapter<E extends Entity> extends RecyclerView.Adapter<EntityListAdapter.ListItemViewHolder> {
    private final List<E> entities;
    private final EntityActionListener<E> actionListener;

    public EntityListAdapter(List<E> entities, EntityActionListener<E> actionListener) {
        this.entities = entities;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each category item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_entity_list_item, parent, false); // Assume category_item.xml is the layout for each item
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        // Get the category object at the current position
        E entity = entities.get(position);

        // Bind the category data to the views
        holder.entityTitle.setText(entity.getName());
        holder.entityDetails.setText(entity.displayDetails());

        // Set click listeners for delete and edit buttons
        holder.deleteButton.setOnClickListener(view -> {
            // Calls the activity’s onDeleteCategory for the specific category
            actionListener.onDeleteEntity(entity);
        });

        holder.editButton.setOnClickListener(view -> {
            // Calls the activity’s onEditCategory for the specific category
            actionListener.onEditEntity(entity);
        });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    // ViewHolder for holding each category's view elements
    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView entityTitle, entityDetails;
        ImageView deleteButton, editButton;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            // Initialize views
            entityTitle = itemView.findViewById(R.id.entityTitle);
            entityDetails = itemView.findViewById(R.id.entityDetails);
            deleteButton = itemView.findViewById(R.id.buttonDeleteEntity);
            editButton = itemView.findViewById(R.id.buttonEditEntity);
        }
    }

    // Interface to communicate actions back to the activity
    public interface EntityActionListener<E> {
        void onDeleteEntity(E entity);
        void onEditEntity(E entity);
    }

}