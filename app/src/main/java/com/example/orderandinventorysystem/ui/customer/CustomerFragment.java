package com.example.orderandinventorysystem.ui.customer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Customer;
import com.example.orderandinventorysystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerFragment extends Fragment implements CustomerListAdapter.ItemClickListener {

    CustomerListAdapter adapter;
    ArrayList<Customer> custList;
    RecyclerView recyclerView;
    View root;
    TextView con;

    private boolean shouldRefreshOnResume = false;

    public static CustomerFragment newInstance() {
        CustomerFragment fragment = new CustomerFragment();
        return fragment;
    }

    public CustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        if(shouldRefreshOnResume){
            custList = new ArrayList<>();
            ShowCustList showCustList = new ShowCustList();
            showCustList.execute("");
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new CustomerListAdapter(getContext(), custList);
            adapter.setClickListener(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ShowCustList showCustList = new ShowCustList();
        showCustList.execute("");
        custList = new ArrayList<>();
        root = inflater.inflate(R.layout.fragment_customer, container, false);
        recyclerView = root.findViewById(R.id.cust_recycler_view);
        con = root.findViewById(R.id.connection);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomerListAdapter(getContext(), custList);
        adapter.setClickListener(this);

        FloatingActionButton fab = root.findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), new_customer.class));
            }
        });

        return root;
    }

    @Override
    public void onItemClick(View view, int position,String id, String name) {
        Intent intent = new Intent(getContext(), CustomerMain.class);
        intent.putExtra("CustomerID", id);
        startActivity(intent);
    }

    public class ShowCustList extends AsyncTask<String,String,String> {

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

                    String query = " SELECT * FROM CUSTOMER WHERE STATUS='Available'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        custList.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13), rs.getString(14)));
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

            if (custList.size()==0) {

                con.setVisibility(View.VISIBLE);
            }
        }
    }
}