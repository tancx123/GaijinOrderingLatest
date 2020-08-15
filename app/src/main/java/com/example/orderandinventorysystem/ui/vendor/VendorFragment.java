package com.example.orderandinventorysystem.ui.vendor;

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
import com.example.orderandinventorysystem.Model.Vendor;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.customer.CustomerFragment;
import com.example.orderandinventorysystem.ui.customer.CustomerListAdapter;
import com.example.orderandinventorysystem.ui.customer.CustomerMain;
import com.example.orderandinventorysystem.ui.customer.new_customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class VendorFragment extends Fragment implements VendorListAdapter.ItemClickListener {

    VendorListAdapter vadapter;
    ArrayList<Vendor> vendList;
    RecyclerView recyclerView;
    View root;
    TextView con;

    private boolean shouldRefreshOnResume = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_vendor, container, false);
        ShowVenList showVenList = new ShowVenList();
        showVenList.execute("");
        vendList= new ArrayList<>();
        con = root.findViewById(R.id.connection);
        recyclerView = root.findViewById(R.id.vendor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        vadapter = new VendorListAdapter(getContext(), vendList);
        vadapter.setClickListener(this);
        recyclerView.setAdapter(vadapter);

        FloatingActionButton fab = root.findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), add_new_vendor.class));
            }
        });

        return root;
    }

    @Override
    public void onItemClick(View view, int position, String id, String name) {
        Intent intent = new Intent(getContext(), VendorMain.class);
        intent.putExtra("VendorID", id);
        startActivity(intent);
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
            vendList = new ArrayList<>();
            ShowVenList showVenList = new ShowVenList();
            showVenList.execute("");
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            vadapter = new VendorListAdapter(getContext(), vendList);
            vadapter.setClickListener(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    public class ShowVenList extends AsyncTask<String,String,String> {

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

                    String query = " SELECT * FROM VENDOR WHERE STATUS='Available'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        vendList.add(new Vendor(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10),  rs.getString(11), rs.getString(12)));
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
            recyclerView.setAdapter(vadapter);

            if (vendList.size()==0) {

                con.setVisibility(View.VISIBLE);
            }
        }
    }

}