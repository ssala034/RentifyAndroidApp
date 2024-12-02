package com.group20.rentify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.group20.rentify.R;
import com.group20.rentify.entity.Entity;

import java.util.List;

public class SpecialEntityListAdapter<E extends Entity> extends RecyclerView.Adapter<SpecialEntityListAdapter.ListItemViewHolder>  {
    private final List<E> entities;
    private final SpecialEntityListAdapter.SpecialEntityActionListener<E> actionListener;
    private final int layout;

    public SpecialEntityListAdapter(List<E> entities, SpecialEntityListAdapter.SpecialEntityActionListener<E> actionListener) {
        this(entities, actionListener, R.layout.special_layout_entity_list);
    }

    public SpecialEntityListAdapter(List<E> entities, SpecialEntityListAdapter.SpecialEntityActionListener<E> actionListener, @LayoutRes int layout) {
        this.entities = entities;
        this.actionListener = actionListener;
        this.layout = layout;
    }

    @NonNull
    @Override
    public SpecialEntityListAdapter.ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each category item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false); // Assume category_item.xml is the layout for each item
        return new SpecialEntityListAdapter.ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialEntityListAdapter.ListItemViewHolder holder, int position) {
        // Get the category object at the current position
        E entity = entities.get(position);
        entity.loadFurther(null);

        // Bind the category data to the views
        holder.entityTitle.setText(entity.getName());
        holder.entityDetails.setText(entity.displayDetails());

        // Set click listeners for request button
        holder.requestButton.setOnClickListener(view -> {
            // Calls the activity’s onEditCategory for the specific category
            actionListener.onRequestEntity(entity);
        });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    // ViewHolder for holding each category's view elements
    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView entityTitle, entityDetails;
        ImageView requestButton;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            // Initialize views
            entityTitle = itemView.findViewById(R.id.entityTitle);
            entityDetails = itemView.findViewById(R.id.entityDetails);
            requestButton = itemView.findViewById(R.id.buttonRequestEnity);
        }
    }

    // Interface to communicate actions back to the activity
    public interface SpecialEntityActionListener<E> {
        void onRequestEntity(E entity);
    }
}
