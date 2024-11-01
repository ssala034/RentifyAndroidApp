package com.group20.rentify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.group20.rentify.entity.Entity;

import java.util.List;

public class EntityList extends ArrayAdapter<Entity> {
    private Activity context;
    List<Entity> items;

    public EntityList(Activity context, List<Entity> items) {
        super(context, R.layout.layout_list_item, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_list_item, parent, false);

        TextView textViewItemName = listViewItem.findViewById(R.id.itemName);
        TextView textViwItemDetails = listViewItem.findViewById(R.id.itemDetails);

        Entity item = items.get(position);
        textViewItemName.setText(item.getName());
        textViwItemDetails.setText(item.getDescription());
        return listViewItem;
    }
}
