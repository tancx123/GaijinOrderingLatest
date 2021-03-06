package com.example.orderandinventorysystem.ui.vendor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Customer;
import com.example.orderandinventorysystem.Model.Vendor;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.customer.CustomerMain;
import com.example.orderandinventorysystem.ui.customer.new_customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

public class add_new_vendor extends AppCompatActivity {

    EditText vendorName;
    EditText vendorIC;
    EditText vendorCompName;
    EditText vendorEmail;
    EditText vendorPhone;
    EditText vendorMobile;
    EditText vendorAddress;
    EditText postCode;
    EditText city;
    EditText state;

    String latestID2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vendor);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Vendor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        vendorName = findViewById(R.id.text_vendor_name_input);
        vendorIC = findViewById(R.id.text_vendor_ic_input);
        vendorCompName = findViewById(R.id.text_companyName_input);
        vendorEmail = findViewById(R.id.text_email_input);
        vendorPhone = findViewById(R.id.text_phone_input);
        vendorMobile = findViewById(R.id.text_mobile_input);
        vendorAddress = findViewById(R.id.text_vendor_address_input);
        postCode = findViewById(R.id.text_vendor_postCode_input);
        city = findViewById(R.id.text_vendor_city_input);
        state = findViewById(R.id.text_vendor_state_input);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save: {

                Toast toast = Toast.makeText(getApplicationContext(), "Please fill up the error field", Toast.LENGTH_SHORT);
                boolean nameValidate = false, ICValidate = false, emailValidate = false, phoneValidate = false, mobileValidate = false, postCodeValidate = false, cityValidate = false, stateValidate = false;

                if (vendorName.getText().toString().isEmpty() || vendorIC.getText().toString().isEmpty() || vendorCompName.getText().toString().isEmpty() || vendorEmail.getText().toString().isEmpty() || vendorPhone.getText().toString().isEmpty() || vendorMobile.getText().toString().isEmpty() || vendorAddress.getText().toString().isEmpty() || postCode.getText().toString().isEmpty() || city.getText().toString().isEmpty() || state.getText().toString().isEmpty()) {

                    if (vendorName.getText().toString().isEmpty())
                        vendorName.setError("Please enter this field");

                    if (vendorIC.getText().toString().isEmpty())
                        vendorIC.setError("Please enter this field");

                    if (vendorCompName.getText().toString().isEmpty())
                        vendorCompName.setError("Please enter this field");

                    if (vendorEmail.getText().toString().isEmpty())
                        vendorEmail.setError("Please enter this field");

                    if (vendorPhone.getText().toString().isEmpty())
                        vendorPhone.setError("Please enter this field");

                    if (vendorMobile.getText().toString().isEmpty())
                        vendorMobile.setError("Please enter this field");

                    if (vendorAddress.getText().toString().isEmpty())
                        vendorAddress.setError("Please enter this field");

                    if (postCode.getText().toString().isEmpty())
                        postCode.setError("Please enter this field");

                    if (city.getText().toString().isEmpty())
                        city.setError("Please enter this field");

                    if (state.getText().toString().isEmpty())
                        state.setError("Please enter this field");

                    toast.show();

                } else {

                    if (!vendorName.getText().toString().matches("[a-zA-Z ]+")) {
                        vendorName.setError("Only A-Z allow");
                        toast.show();
                    } else {
                        nameValidate = true;
                    }

                    if (!vendorIC.getText().toString().matches("^[0-9]*$")) {
                        vendorIC.setError("IC No. only contain 0-9");
                        toast.show();
                    } else if (!vendorIC.getText().toString().matches("^(\\d{12})$")) {
                        vendorIC.setError("IC No. have 12 digit");
                        toast.show();
                    } else {
                        ICValidate = true;
                    }

                    if (!vendorEmail.getText().toString().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
                        vendorEmail.setError("example@gmail.com");
                        toast.show();
                    } else {
                        emailValidate = true;
                    }

                    if (!vendorPhone.getText().toString().matches("^[0-9]*$")) {
                        vendorPhone.setError("Enter your company Phone No. without (-)");
                        toast.show();
                    } else if (!vendorPhone.getText().toString().matches("^(\\d{9}|\\d{10}|\\d{11}|\\d{12})$")) {
                        vendorPhone.setError("phone No. should be 9-12 digit");
                        toast.show();
                    } else {
                        phoneValidate = true;
                    }

                    if (!vendorMobile.getText().toString().matches("^[0-9]*$")) {
                        vendorMobile.setError("Enter your Mobile No. without (-)");
                        toast.show();
                    } else if (!vendorMobile.getText().toString().matches("^(\\d{10}|\\d{11}|\\d{12})$")) {
                        vendorMobile.setError("phone No. should be 10-12 digit");
                        toast.show();
                    } else {
                        mobileValidate = true;
                    }

                    if (!city.getText().toString().matches("[a-zA-Z ]+")) {
                        city.setError("Only A-Z allow");
                        toast.show();
                    } else {
                        cityValidate = true;
                    }

                    if (!state.getText().toString().matches("[a-zA-Z ]+")) {
                        state.setError("Only A-Z allow");
                        toast.show();
                    } else {
                        stateValidate = true;
                    }

                    if (!postCode.getText().toString().matches("^[0-9]*$")) {
                        postCode.setError("Only 0-9 allow");
                        toast.show();
                    } else if (!postCode.getText().toString().matches("^(\\d{5})$")) {
                        postCode.setError("post code have 5 digit");
                        toast.show();
                    } else {
                        postCodeValidate = true;
                    }

                }

                if (nameValidate == true && ICValidate == true && emailValidate == true && phoneValidate == true && mobileValidate == true && postCodeValidate == true && cityValidate == true && stateValidate == true) {
                    //constructor
                    Vendor ven = new Vendor("0", vendorName.getText().toString(), vendorIC.getText().toString(), vendorCompName.getText().toString(), vendorEmail.getText().toString(), vendorPhone.getText().toString(), vendorMobile.getText().toString(), vendorAddress.getText().toString(), state.getText().toString(), city.getText().toString(), postCode.getText().toString(), "Available");
                    AddVendor addVendor = new AddVendor(ven);
                    addVendor.execute("");

                    String str_result = "h";
                    try {
                        str_result = new RetrieveVendorID().execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.finish();

                    RetrieveVendorID retrieveVendorID = new RetrieveVendorID();
                    retrieveVendorID.execute("");
                    Intent intent = new Intent(this, VendorMain.class);
                    intent.putExtra("VendorID", latestID2);
                    startActivity(intent);
                    return true;
                }else {
                    toast.show();
                }
            }


            default:
                return super.onOptionsItemSelected(item);
        }
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

    public class RetrieveVendorID extends AsyncTask<String,String,String> {

        RetrieveVendorID() {
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

                    String query = " SELECT venID FROM VENDOR ORDER BY venID DESC LIMIT 1";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if(rs.next()){
                        latestID2 = rs.getString(1);
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
        }
    }

    public class AddVendor extends AsyncTask<String,String,String> {

        Vendor vendor;

        AddVendor(Vendor vendor) {

            this.vendor = vendor;
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
                    checkConnection = "No";
                } else {
                    String query = "SELECT * FROM VENDOR ORDER BY venID DESC LIMIT 1";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    String latestID;

                    if (rs.next()) {
                        latestID = rs.getString(1);
                        int numID = Integer.parseInt(latestID.substring(3,8)) + 1;
                        if (numID < 10)
                            latestID = "VE-0000" + Integer.toString(numID);
                        else if (numID < 100)
                            latestID = "VE-000" + Integer.toString(numID);
                        else if (numID < 1000)
                            latestID = "VE-00" + Integer.toString(numID);
                        else if (numID < 10000)
                            latestID = "VE-0" + Integer.toString(numID);
                        else if (numID < 100000)
                            latestID = "VE-" + Integer.toString(numID);

                        Log.d("ID", latestID);
                    }

                    else {
                        latestID = "VE-00001";
                        Log.d("ID", latestID);
                    }

                    query = "INSERT INTO VENDOR VALUES('" + latestID + "', '" + vendor.getVenName() + "', '" +
                            vendor.getVenIC() + "', '" + vendor.getCompanyName() + "', '" + vendor.getEmail() + "', '" +
                            vendor.getPhone() + "', '" + vendor.getMobile() + "', '" + vendor.getAddress() + "', '" +
                            vendor.getState() + "', '" + vendor.getCity() + "', '" + vendor.getPostcode() + "','Available')";

                    stmt = con.createStatement();
                    stmt.executeUpdate(query);

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

            Toast.makeText(add_new_vendor.this, "Vendor added.", Toast.LENGTH_LONG).show();
        }
    }


}
