package com.example.orderandinventorysystem.ui.pack;

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
import com.example.orderandinventorysystem.Model.Pack;
import com.example.orderandinventorysystem.Model.Sales;
import com.example.orderandinventorysystem.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class packages_shipped extends Fragment implements PackageListAdapter.ItemClickListener{

    PackageListAdapter adapter;
    ArrayList<Pack> packList;
    RecyclerView recyclerView;
    View root;
    TextView con;
    ArrayList<Sales> salesList;
    private boolean shouldRefreshOnResume = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_packages_ship, container, false);
        packList = new ArrayList<>();
        salesList = new ArrayList<>();
        ShowPackShipped showPackShipped = new ShowPackShipped();
        showPackShipped.execute("");
        con = root.findViewById(R.id.connection);
        recyclerView = root.findViewById(R.id.pack_all_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PackageListAdapter(getContext(), packList, salesList);
        adapter.setClickListener(this);

        return root;
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
            con.setVisibility(View.INVISIBLE);
            packList = new ArrayList<>();

            Log.d("HAHA", Integer.toString(packList.size()));
            ShowPackShipped showPackShipped = new ShowPackShipped();
            showPackShipped.execute("");
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new PackageListAdapter(getContext(), packList, salesList);
            recyclerView.setAdapter(adapter);
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
        Intent intent = new Intent(getContext(), PackageMain.class);
        intent.putExtra("Package", packList.get(position).getPackID());
        startActivityForResult(intent, 3);
    }

    public class ShowPackShipped extends AsyncTask<String,String,String> {

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
                    String query = " SELECT * FROM PACKAGE WHERE PACKSTATUS='SHIPPED'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    String salesID;

                    while (rs.next()) {
                        packList.add(new Pack(rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4)));
                        Log.d("Success", rs.getString(1));
                        salesID = rs.getString(3);

                        query = " SELECT * FROM SALES WHERE salesID = '" + salesID + "'";
                        stmt = con.createStatement();
                        ResultSet rs2 = stmt.executeQuery(query);

                        if (rs2.next())
                            salesList.add(new Sales(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getDouble(6), rs2.getString(5)));
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

            if (packList.size()==0) {

                con.setVisibility(View.VISIBLE);
            }
        }
    }

}
