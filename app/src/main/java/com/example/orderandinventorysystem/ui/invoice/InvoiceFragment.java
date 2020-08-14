package com.example.orderandinventorysystem.ui.invoice;

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
import com.example.orderandinventorysystem.Model.Invoice;
import com.example.orderandinventorysystem.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class InvoiceFragment extends Fragment implements InvoiceListAdapter.ItemClickListener{

    InvoiceListAdapter adapter;
    ArrayList<Invoice> invList;
    RecyclerView recyclerView;
    View root;
    TextView con;
    private boolean shouldRefreshOnResume = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_invoice, container, false);
        invList = new ArrayList<>();

        ShowInvList showInvList = new ShowInvList();
        showInvList.execute("");
        recyclerView = root.findViewById(R.id.inv_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InvoiceListAdapter(getContext(), invList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), InvoiceMainFragment.class);
        intent.putExtra("Invoice", invList.get(position).getInvID());
        startActivityForResult(intent, 16);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        if(shouldRefreshOnResume){
            invList = new ArrayList<>();
            ShowInvList showInvList = new ShowInvList();
            showInvList.execute("");
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new InvoiceListAdapter(getContext(), invList);
            adapter.setClickListener(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    public class ShowInvList extends AsyncTask<String,String,String> {

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

                    String query = " SELECT * FROM INVOICE ";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        invList.add(new Invoice(rs.getString(1),rs.getString(2),
                                rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6),
                                rs.getString(7)
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

            if (checkConnection.equals("No")) {

                con.setVisibility(View.VISIBLE);
            }
        }
    }
}