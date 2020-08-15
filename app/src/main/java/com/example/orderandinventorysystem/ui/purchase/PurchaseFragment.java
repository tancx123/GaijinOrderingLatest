package com.example.orderandinventorysystem.ui.purchase;

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
import com.example.orderandinventorysystem.Model.Purchase;
import com.example.orderandinventorysystem.Model.Sales;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.sales.SalesListAdapter;
import com.example.orderandinventorysystem.ui.sales.SalesOrderMainFragment;
import com.example.orderandinventorysystem.ui.sales.add_sales_orders;
import com.example.orderandinventorysystem.ui.sales.sales_avail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PurchaseFragment extends Fragment implements PurchaseListAdapter.ItemClickListener{

    PurchaseListAdapter padapter;
    ArrayList<Purchase> pList;
    RecyclerView recyclerView;
    View root;
    TextView con;
    private boolean shouldRefreshOnResume = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_purchase, container, false);
        pList = new ArrayList<>();

        ShowPList showPList = new ShowPList();
        showPList.execute("");
        con = root.findViewById(R.id.connection);
        recyclerView = root.findViewById(R.id.purchase_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        padapter = new PurchaseListAdapter(getContext(), pList);
        padapter.setClickListener(this);
        recyclerView.setAdapter(padapter);

        FloatingActionButton fab = root.findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), add_new_purchase.class));
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check should we need to refresh the fragment
        if(shouldRefreshOnResume){
            con.setVisibility(View.INVISIBLE);
            pList = new ArrayList<>();
            ShowPList showPList = new ShowPList();
            showPList.execute("");
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            padapter = new PurchaseListAdapter(getContext(), pList);
            padapter.setClickListener(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), PurchaseMain.class);
        intent.putExtra("Purchase", pList.get(position).getpID());
        startActivity(intent);
    }

    public class ShowPList extends AsyncTask<String,String,String> {

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

                    String query = " SELECT * FROM Purchase WHERE NOT PStatus='Removed' ";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        pList.add(new Purchase(rs.getString(1),rs.getString(2),
                                rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6)
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
            recyclerView.setAdapter(padapter);

            if (pList.size()==0) {

                con.setVisibility(View.VISIBLE);
            }
        }
    }
}