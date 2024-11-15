package com.group20.rentify.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group20.rentify.R;
import com.group20.rentify.entity.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categories;
    private CategoryActionListener actionListener;

    public CategoryAdapter(List<Category> categories, CategoryActionListener actionListener) {
        this.categories = categories;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each category item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false); // Assume category_item.xml is the layout for each item
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Get the category object at the current position
        Category category = categories.get(position);

        // Bind the category data to the views
        holder.categoryTitle.setText(category.getName());
        holder.categoryDetails.setText(category.getDescription());

        // Set click listeners for delete and edit buttons
        holder.deleteButton.setOnClickListener(view -> {
            // Calls the activity’s onDeleteCategory for the specific category
            actionListener.onDeleteCategory(category);
        });

        holder.editButton.setOnClickListener(view -> {
            // Calls the activity’s onEditCategory for the specific category
            actionListener.onEditCategory(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void notifyDataSetChange(List<Category> categories){
        this.categories = categories;
    }

    // ViewHolder for holding each category's view elements
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle, categoryDetails;
        ImageView deleteButton, editButton;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            // Initialize views
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            categoryDetails = itemView.findViewById(R.id.categoryDetails);
            deleteButton = itemView.findViewById(R.id.buttonDeleteCategory);
            editButton = itemView.findViewById(R.id.buttonEditCategory);
        }
    }

    // Interface to communicate actions back to the activity
    public interface CategoryActionListener {
        void onDeleteCategory(Category category);
        void onEditCategory(Category category);
        //void onCategoryClicked(Category category);
    }

}
