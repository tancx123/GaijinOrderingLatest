package com.example.orderandinventorysystem.ui.sales;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Sales;
import com.example.orderandinventorysystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class sales_remove extends Fragment implements SalesListAdapter.ItemClickListener   {

    SalesListAdapter adapter;
    ArrayList<Sales> salesList;
    RecyclerView recyclerView;
    View root;
    TextView con;
    private boolean shouldRefreshOnResume = false;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sales_r, container, false);
        salesList = new ArrayList<>();
        ShowSalesList showSalesList = new ShowSalesList();
        showSalesList.execute("");
        con = root.findViewById(R.id.connection);
        recyclerView = root.findViewById(R.id.sales_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SalesListAdapter(getContext(), salesList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = root.findViewById(R.id.add_fab);
        fab.setVisibility(View.GONE);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        if(shouldRefreshOnResume){
            con.setVisibility(View.INVISIBLE);
            salesList = new ArrayList<>();
            ShowSalesList showSalesList = new ShowSalesList();
            showSalesList.execute("");
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new SalesListAdapter(getContext(), salesList);
            adapter.setClickListener(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), SalesOrderMainFragment.class);
        intent.putExtra("Sales", salesList.get(position).getSalesID());
        startActivity(intent);
    }

    public class ShowSalesList extends AsyncTask<String,String,String> {

        String checkConnection = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            ConnectionPhpMyAdmin connectionClass = new ConnectionPhpMyAdmin();
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    checkConnection = "No";
                } else {

                    String query = " SELECT * FROM SALES WHERE SALESSTATUS='Removed' ";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        salesList.add(new Sales(rs.getString(1),rs.getString(2),
                                rs.getString(3), rs.getString(4), rs.getDouble(6), rs.getString(5)
                        ));
                        Log.d("Success", rs.getString(1));
                    }

                    checkConnection = "Yes";
                    isSuccess = true;

                }
            } catch (Exception ex) {
                Log.d("Error", ex.toString());
                isSuccess = false;
                checkConnection = "No";
            }

            return checkConnection;
        }

        @Override
        protected void onPostExecute(String s) {
            recyclerView.setAdapter(adapter);

            if (salesList.size()==0) {

                con.setVisibility(View.VISIBLE);
            }
        }
    }
}
