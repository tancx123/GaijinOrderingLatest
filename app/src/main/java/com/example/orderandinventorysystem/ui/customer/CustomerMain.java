package com.example.orderandinventorysystem.ui.customer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Customer;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.invoice.InvoiceMainFragment;
import com.example.orderandinventorysystem.ui.sales.SalesOrderMainFragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerMain extends AppCompatActivity {

    Customer cust;
    String custID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main);
        Intent intent = getIntent();
        String intentCustID = intent.getStringExtra("CustomerID");
        custID = intentCustID;

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RetrieveCust retrieveCust = new RetrieveCust(custID);
        retrieveCust.execute("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.customer_main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3) {

            Intent intent = new Intent(this, CustomerMain.class);
            intent.putExtra("CustomerID", custID);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case R.id.delete: {
                DeleteCust deleteCust = new DeleteCust(custID);
                deleteCust.execute("");
                this.finish();

                return true;
            }
            case R.id.edit_sales: {
                Intent intent = new Intent(this, edit_customer.class);
                intent.putExtra("CustomerID", custID);
                startActivityForResult(intent, 17);

                return true;
            }


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public class RetrieveCust extends AsyncTask<String,String,String> {

        String id;

        RetrieveCust(String id) {

            this.id = id;
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

                    String query = " SELECT * FROM CUSTOMER WHERE custID ='" + custID + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if(rs.next()){
                        cust = new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10));
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

            //Log.d("hahaha", item.getItemName());

            getSupportActionBar().setTitle(cust.getCustName());

            TextView compName = (TextView)findViewById(R.id.comp_name);
            compName.setText(cust.getCompanyName());
            TextView custType = (TextView)findViewById(R.id.customer_type);
            custType.setText(cust.getCustType() + " Customer");
            TextView gender = (TextView)findViewById(R.id.gender);
            gender.setText(cust.getGender());
            TextView icNo = (TextView)findViewById(R.id.ic_number);
            icNo.setText(cust.getIcNo());
            TextView phone = (TextView)findViewById(R.id.phone);
            phone.setText(cust.getPhone());
            TextView mobile = (TextView)findViewById(R.id.mobile);
            mobile.setText(cust.getMobile());
            TextView address = (TextView)findViewById(R.id.address);
            address.setText(cust.getAddress());
            TextView email = (TextView)findViewById(R.id.email);
            email.setText(cust.getEmail());

        }
    }

    public class DeleteCust extends AsyncTask<String,String,String> {
        String id;

        DeleteCust(String id) {

            this.id = id;
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

                    String query = " DELETE FROM CUSTOMER WHERE custID ='" + custID + "'";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
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
        protected void onPostExecute(String s) {}
    }
}
