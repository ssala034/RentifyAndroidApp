package com.group20.rentify.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group20.rentify.R;
import com.group20.rentify.ViewRequestsActivity;
import com.group20.rentify.entity.Entity;
import com.group20.rentify.entity.Item;

import java.util.List;

public class EntityListAdapter<E extends Entity> extends RecyclerView.Adapter<EntityListAdapter.ListItemViewHolder> {
    private final List<E> entities;
    private final EntityActionListener<E> actionListener;
    private final int layout;

    public EntityListAdapter(List<E> entities, EntityActionListener<E> actionListener) {
        this(entities, actionListener, R.layout.layout_entity_list_item);
    }

    // TODO @Emily use this constructor to pass a slightly modified layout with the different images
    //      so you can reuse this class for the request display
    public EntityListAdapter(List<E> entities, EntityActionListener<E> actionListener, @LayoutRes int layout) {
        this.entities = entities;
        this.actionListener = actionListener;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each category item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false); // Assume category_item.xml is the layout for each item
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

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            // Open ViewRequestsActivity when an item is clicked
            if (entity instanceof Item) {
                Item selectedItem = (Item) entity;  // Cast to Item
                Context context = v.getContext();
                Intent intent = new Intent(context, ViewRequestsActivity.class);
                intent.putExtra("itemID", selectedItem.getUniqueIdentifier());  // Pass the item
                intent.putExtra("itemName", selectedItem.getName());  // Pass the item name
                context.startActivity(intent);
            }
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
