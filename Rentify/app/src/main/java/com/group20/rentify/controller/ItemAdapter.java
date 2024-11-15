package com.group20.rentify.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group20.rentify.R;
import com.group20.rentify.ViewItemActivity;
import com.group20.rentify.entity.Item;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CategoryViewHolder>{

    private List<Item> items;
    private ViewItemActivity actionListener;

    public ItemAdapter(List<Item> item, ViewItemActivity actionListener) {
        this.items = items;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each category item (category_item.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);  // Return a new instance of CategoryViewHolder
    }


    @Override
    //???
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        // Get the category object at the current position
        Item item = items.get(position);

        // Bind the category data to the views
        holder.categoryTitle.setText(item.getName());
        String description = item.getDescription() +"\nRental Fee: "+ item.getRentalFee() +"\nRental Time: "+ item.getRentalTime();
        holder.categoryDetails.setText(description);

        // Set click listeners for delete and edit buttons
        holder.deleteButton.setOnClickListener(view -> {
            // Calls the activity’s onDeleteCategory for the specific category
            actionListener.onDeleteItem(item);
        });

        holder.editButton.setOnClickListener(view -> {
            // Calls the activity’s onEditCategory for the specific category
            actionListener.onEditItem(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void notifyDataSetChange(List<Item> items){
        this.items = items;
    }

    // ViewHolder for holding each category's view elements
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle, categoryDetails;
        ImageView deleteButton, editButton;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            // Initialize views from category_item.xml
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            categoryDetails = itemView.findViewById(R.id.categoryDetails);
            deleteButton = itemView.findViewById(R.id.buttonDeleteCategory);
            editButton = itemView.findViewById(R.id.buttonEditCategory);
        }
    }

    // Interface to communicate actions back to the activity
    public interface ItemActionListener {
        void onDeleteItem(Item item);
        void onEditItem(Item item);
    }


}
