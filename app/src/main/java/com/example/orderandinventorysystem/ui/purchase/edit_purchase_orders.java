package com.example.orderandinventorysystem.ui.purchase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Customer;
import com.example.orderandinventorysystem.Model.ItemOrder;
import com.example.orderandinventorysystem.Model.Purchase;
import com.example.orderandinventorysystem.Model.Sales;
import com.example.orderandinventorysystem.Model.Vendor;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.customer.CustomerListAdapter;
import com.example.orderandinventorysystem.ui.sales.ItemOrderListAdapter;
import com.example.orderandinventorysystem.ui.sales.SalesOrderMainFragment;
import com.example.orderandinventorysystem.ui.sales.add_sales_line_item;
import com.example.orderandinventorysystem.ui.vendor.VendorListAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class edit_purchase_orders extends AppCompatActivity implements VendorListAdapter.ItemClickListener, ItemOrderListAdapter.ItemClickListener {

    RelativeLayout relativeLayout;
    private Button BtnAddLine;
    VendorListAdapter adapter;
    ItemOrderListAdapter adapter2;
    ArrayList<ItemOrder> ioList = new ArrayList<>();
    ArrayList<Vendor> vendorList;
    Purchase purchaseEdit;
    String venID;
    RecyclerView recyclerView, recyclerView2;
    EditText editText;
    TextView date;
    double total=0, subtotal=0, discountTotal=0;
    boolean checkCustName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_p_orders);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Purchase Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        purchaseEdit = (Purchase) intent.getSerializableExtra("PurchaseEdit");
        ioList = new ArrayList<>();
        BtnAddLine = findViewById(R.id.add_sales_line_item_btn);

        vendorList = new ArrayList<>();

        recyclerView = findViewById(R.id.custSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VendorListAdapter(this, vendorList);

        recyclerView2 = findViewById(R.id.itemLine);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new ItemOrderListAdapter(this, ioList);
        recyclerView2.setAdapter(adapter2);

        adapter.setClickListener(this);
        adapter2.setClickListener(this);

        relativeLayout = findViewById(R.id.itemLineLayout);
        relativeLayout.setVisibility(View.VISIBLE);
        date =  findViewById(R.id.text_sales_order_date_input);
        date.setText(purchaseEdit.getdDate());
        editText = findViewById(R.id.text_customer_name_input_sales);
        editText.setText(purchaseEdit.getVenName());
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean userChange = Math.abs(count - before) == 1;
                if (userChange) {
                    checkCustName = false;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        BtnAddLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddSalesLineItem();
            }
        });

        String str_result="h";
        try {
            str_result= new ShowCustList().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(View view, int position, String id, String name) {
        editText.getText().clear();
        checkCustName = true;
        venID = id;
        editText.setText(name);
        editText.clearFocus();
    }

    public void filter(String text) {
        ArrayList<Vendor> filteredList = new ArrayList<>();

        for (Vendor item : vendorList) {
            if (item.getVenName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);

        if (text.length() == 0 || filteredList.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
        }

        else {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void openAddSalesLineItem(){
        Intent intent = new Intent(this, add_sales_line_item.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_main_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save: {

                if(checkCustName && !ioList.isEmpty()){

                    Purchase purchase = new Purchase(purchaseEdit.getpID(), venID, editText.getText().toString(), date.getText().toString(), "Confirmed", total);
                    AddSales addSales = new AddSales(purchase, ioList);
                    addSales.execute("");
                    Intent intent = new Intent(this, PurchaseMain.class);
                    intent.putExtra("Purchase", purchase.getpID());
                    startActivity(intent);
                    setResult(2);
                    finish();


                }else{

                    if (!checkCustName)
                        editText.setError("Invalid vendor name !");
                    if (ioList.isEmpty())
                        Toast.makeText(getApplicationContext(),"You must add at least one item line !",Toast.LENGTH_SHORT).show();
                }
                //constructor
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                ItemOrder io = (ItemOrder) data.getSerializableExtra("itemOrder");
                relativeLayout = findViewById(R.id.itemLineLayout);
                relativeLayout.setVisibility(View.VISIBLE);
                boolean check = false;
                for (int i = 0 ; i < ioList.size() ; i++) {

                    if (ioList.get(i).getItemID().equals(io.getItemID())) {

                        ioList.get(i).setQuantity(ioList.get(i).getQuantity() + io.getQuantity());
                        ioList.get(i).setTotal(ioList.get(i).getQuantity() * ioList.get(i).getSellPrice());
                        check = true;
                        break;
                    }
                }
                if (check == false)
                    ioList.add(io);

                subtotal += io.getTotal();
                discountTotal +=  (io.getTotal() * io.getDiscount()/100);
                total = subtotal - discountTotal;
                TextView subtotalTV = findViewById(R.id.sales_order_sub_total);
                TextView totalTV = findViewById(R.id.sales_order_total);
                TextView discount = findViewById(R.id.discountTotal);
                discount.setText(String.format("- MYR%.2f", discountTotal));
                subtotalTV.setText(String.format("MYR%.2f", subtotal));
                totalTV.setText(String.format("MYR%.2f", total));
                recyclerView2.setLayoutManager(new LinearLayoutManager(this));
                adapter2 = new ItemOrderListAdapter(this, ioList);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onItemClick2(View view, int position, String id, String name) {

        final int itemPost = position;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Remove this item?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        subtotal -= ioList.get(itemPost).getTotal();
                        discountTotal -= (ioList.get(itemPost).getTotal() * ioList.get(itemPost).getDiscount()/100);
                        total = subtotal - discountTotal;

                        TextView subtotalTV = findViewById(R.id.sales_order_sub_total);
                        TextView totalTV = findViewById(R.id.sales_order_total);
                        TextView discount = findViewById(R.id.discountTotal);
                        discount.setText(String.format("- MYR%.2f", discountTotal));
                        subtotalTV.setText(String.format("MYR%.2f", subtotal));
                        totalTV.setText(String.format("MYR%.2f", total));
                        ioList.remove(itemPost);
                        recyclerView2.setAdapter(adapter2);
                        if (ioList.size() == 0)
                            relativeLayout.setVisibility(View.GONE);

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public class ShowCustList extends AsyncTask<String,String,String> {

        String latestID;
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
                        vendorList.add(new Vendor(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12)));
                        Log.d("Success", rs.getString(1));
                    }

                    query = " SELECT * FROM ITEMORDER WHERE ORDERID= '" + purchaseEdit.getpID() + "'";
                    stmt = con.createStatement();
                    rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        ioList.add(new ItemOrder(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getInt(6), rs.getDouble(7)));
                        subtotal += rs.getDouble(5);
                        discountTotal += (rs.getDouble(5) * rs.getDouble(7)/100);
                        total = subtotal - discountTotal;
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
            TextView subtotalTV = findViewById(R.id.sales_order_sub_total);
            TextView totalTV = findViewById(R.id.sales_order_total);
            TextView discount = findViewById(R.id.discountTotal);
            discount.setText(String.format("- MYR%.2f", discountTotal));
            subtotalTV.setText(String.format("MYR%.2f", subtotal));
            totalTV.setText(String.format("MYR%.2f", total));
        }
    }

    public class AddSales extends AsyncTask<String,String,String> {

        ArrayList<ItemOrder> ioList;
        Purchase sales;
        String checkConnection = "";
        boolean isSuccess = false;

        public Purchase getSales() {
            return sales;
        }

        public void setSales(Purchase purchase) {
            this.sales = purchase;
        }

        AddSales(Purchase purchase, ArrayList<ItemOrder> ioList) {
            this.sales = purchase;
            this.ioList = ioList;
        }

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
                    String query = "UPDATE PURCHASE SET VENID='" + sales.getVenID() + "', VENNAME='" +
                            sales.getVenName() + "', pDATE='" + sales.getdDate() + "', pSTATUS='" + sales.getpStatus() + "', amount='" +
                            sales.getpAmount() + "' WHERE PID = '" + sales.getpID()+"'";

                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    query = "DELETE FROM ITEMORDER WHERE ORDERID='" + sales.getpID() + "'";

                    stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    for (ItemOrder io : ioList) {


                        query = "INSERT INTO ITEMORDER VALUES ('" + sales.getpID() + "', '" + io.getItemID() + "', '" +
                                io.getItemName() + "', '" + io.getSellPrice() + "', '" + io.getTotal() + "', '" +
                                io.getQuantity() + "','" + io.getDiscount() + "')";

                        stmt = con.createStatement();
                        stmt.executeUpdate(query);
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
            Toast.makeText(edit_purchase_orders.this, "Purchase Order edited.", Toast.LENGTH_LONG).show();
        }
    }
}
