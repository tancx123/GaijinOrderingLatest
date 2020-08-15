package com.example.orderandinventorysystem.ui.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.orderandinventorysystem.Model.Purchase;
import com.example.orderandinventorysystem.R;

import java.util.List;

public class PurchaseListAdapter extends RecyclerView.Adapter<PurchaseListAdapter.ViewHolder>{

    private List<Purchase> mData;
    private LayoutInflater mInflater;
    private PurchaseListAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public PurchaseListAdapter(Context context, List<Purchase> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public PurchaseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.purchase_list_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(PurchaseListAdapter.ViewHolder holder, int position) {
        Purchase pur = mData.get(position);
        holder.myTextView.setText(pur.getVenName());
        holder.id.setText(pur.getpID());
        holder.pDate.setText(pur.getdDate());
        holder.pStatus.setText(pur.getpStatus());
        holder.pPrice.setText(String.format("MYR%.2f", pur.getpAmount()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView, pPrice, pDate, pStatus, id;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.vend_name_view);
            id = itemView.findViewById(R.id.pID);
            pDate = itemView.findViewById(R.id.pDate);
            pPrice = itemView.findViewById(R.id.pPrice);
            pStatus = itemView.findViewById(R.id.pStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Purchase getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(PurchaseListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
