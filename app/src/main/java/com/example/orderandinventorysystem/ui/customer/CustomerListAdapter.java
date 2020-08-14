package com.example.orderandinventorysystem.ui.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.orderandinventorysystem.Model.Customer;
import com.example.orderandinventorysystem.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    private List<Customer> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<Customer> mDataAll;

    // data is passed into the constructor
    public CustomerListAdapter(Context context, List<Customer> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mDataAll = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.customer_list_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Customer cust = mData.get(position);
        holder.id.setText(cust.getCustID());
        holder.myTextView.setText(cust.getCustName());
        holder.comp.setText(cust.getCompanyName());
        holder.phone.setText(cust.getPhone());
        holder.mobile.setText(cust.getMobile());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void filterList(ArrayList<Customer> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id, myTextView, comp, phone, mobile;

        ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.cust_id);
            myTextView = itemView.findViewById(R.id.cust_name_view);
            comp = itemView.findViewById(R.id.comp_name_view);
            phone = itemView.findViewById(R.id.phone_view);
            mobile = itemView.findViewById(R.id.mobile_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(), id.getText().toString(), myTextView.getText().toString());
        }
    }

    // convenience method for getting data at click position
    public Customer getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, String id, String name);
    }
}
