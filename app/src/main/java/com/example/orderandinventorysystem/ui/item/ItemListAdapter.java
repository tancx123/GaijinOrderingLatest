package com.example.orderandinventorysystem.ui.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.orderandinventorysystem.Model.Item;
import com.example.orderandinventorysystem.R;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private List<Item> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ItemListAdapter(Context context, List<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.id.setText(item.getItemID());
        holder.name.setText(item.getItemName());
        holder.desc.setText(item.getItemDesc());
        holder.price.setText(String.format("%.2f", item.getSellPrice()));
        holder.price2.setText(String.format("%.2f", item.getCostPrice()));
    }

    public void filterList(ArrayList<Item> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id, name, desc, quantity, unit, price, price2;

        ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.item_id);
            name = itemView.findViewById(R.id.item_name);
            desc = itemView.findViewById(R.id.item_desc);
//            unit = itemView.findViewById(R.id.item_unit);
            price = itemView.findViewById(R.id.price);
            price2 = itemView.findViewById(R.id.price2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(), id.getText().toString(), name.getText().toString(), Double.parseDouble(price.getText().toString()));
        }
    }

    // convenience method for getting data at click position
    public Item getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, String id, String name, double price);
    }
}
