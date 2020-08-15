package com.example.orderandinventorysystem.ui.bill;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Bill;
import com.example.orderandinventorysystem.Model.Invoice;
import com.example.orderandinventorysystem.Model.Payment;
import com.example.orderandinventorysystem.Model.Purchase;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.payment.PaymentMain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class BillMain extends AppCompatActivity{

    Bill bill;
    Purchase purchase;
    String intentInvoiceID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_main);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bill Receipt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        intentInvoiceID = intent.getStringExtra("Bill");
        PaymentInfo paymentInfo = new PaymentInfo(intentInvoiceID);
        paymentInfo.execute("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    public class PaymentInfo extends AsyncTask<String,String,String> {

        String invoiceID;

        PaymentInfo(String invoiceID) {
            this.invoiceID = invoiceID;
        }

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
                    checkConnection = "Please check your internet connection.";
                } else {

                    String query = " SELECT * FROM BILL WHERE pID ='" + invoiceID + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {

                        bill = new Bill(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getDouble(5));
                    }

                    query = " SELECT * FROM Purchase WHERE pID ='" + invoiceID + "'";
                    stmt = con.createStatement();
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {

                        purchase = new Purchase(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getDouble(6));
                    }

                    Log.d("Success", "Done");
                    checkConnection = "Done";
                    isSuccess = true;

                }
            } catch (Exception ex) {
                Log.d("Error", ex.toString());
                isSuccess = false;
                checkConnection = "Exceptions" + ex;
            }

            return checkConnection;
        }

        @Override
        protected void onPostExecute(String s) {

            TextView sales_order_id = findViewById(R.id.p_Order);
            sales_order_id.setText(bill.getBillID());

            TextView custName = findViewById(R.id.vendor);
            custName.setText(purchase.getVenName());

            TextView sales_order_price = findViewById(R.id.sales_order_price);
            sales_order_price.setText(String.format("MYR%.2f", purchase.getpAmount()));

            TextView sales_order_date = findViewById(R.id.sales_order_date);
            sales_order_date.setText(bill.getBillDate());

            TextView invoice_id = findViewById(R.id.invoice_id);
            invoice_id.setText(purchase.getpID());

            TextView invoice_date = findViewById(R.id.invoice_date);
            invoice_date.setText(purchase.getdDate());

            TextView item_total_price = findViewById(R.id.item_total_price);
            item_total_price.setText(String.format("MYR%.2f", purchase.getpAmount()));

        }
    }
}
