package com.example.orderandinventorysystem.ui.vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.orderandinventorysystem.Model.Customer;
import com.example.orderandinventorysystem.Model.Vendor;
import com.example.orderandinventorysystem.R;

import java.util.ArrayList;
import java.util.List;

public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.ViewHolder>{

    private List<Vendor> mData;
    private LayoutInflater mInflater;
    private VendorListAdapter.ItemClickListener mClickListener;
    private List<Vendor> mDataAll;

    // data is passed into the constructor
    public VendorListAdapter(Context context, List<Vendor> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mDataAll = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public VendorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.vendor_list_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(VendorListAdapter.ViewHolder holder, int position) {
        Vendor vend = mData.get(position);
        holder.myTextView.setText(vend.getVenName());
        holder.comp.setText(vend.getCompanyName());
        holder.phone.setText(vend.getPhone());
        holder.mobile.setText(vend.getMobile());
        holder.id.setText(vend.getVenID());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void filterList(ArrayList<Vendor> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView, comp, phone, mobile, id;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.vend_name_view);
            comp = itemView.findViewById(R.id.com_name_view);
            phone = itemView.findViewById(R.id.vphone_view);
            mobile = itemView.findViewById(R.id.vmobile_view);
            id = itemView.findViewById(R.id.ven_id);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(), id.getText().toString(), myTextView.getText().toString());
        }
    }

    // convenience method for getting data at click position
    public Vendor getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(VendorListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position, String id, String name);
    }
}
