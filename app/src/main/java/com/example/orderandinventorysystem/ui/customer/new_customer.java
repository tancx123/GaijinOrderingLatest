package com.example.orderandinventorysystem.ui.customer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Customer;
import com.example.orderandinventorysystem.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

public class new_customer extends AppCompatActivity {

    RadioGroup custType;
    RadioGroup custGender;
    RadioButton radioCustType;
    RadioButton radioGender;
    EditText custName;
    EditText companyName;
    EditText custEmail;
    EditText companyPhone;
    EditText custMobile;
    EditText custAddress;
    EditText custIC;

    String latestID2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        custType = findViewById(R.id.group_custType);
        custGender = findViewById(R.id.group_gender);

        radioCustType = findViewById(R.id.radioBtn_Business);
        radioGender = findViewById(R.id.radioBtn_Male);

        custType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioBtn_Business:
                        radioCustType = findViewById(R.id.radioBtn_Business);
                        break;

                    case R.id.radioBtn_Individual:
                        radioCustType = findViewById(R.id.radioBtn_Individual);
                        break;
                }
            }
        });

        custGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtn_Male:
                        radioGender = findViewById(R.id.radioBtn_Male);
                        break;

                    case R.id.radioBtn_Female:
                        radioGender = findViewById(R.id.radioBtn_Female);
                        break;
                }
            }
        });

        custName = findViewById(R.id.text_custName_input);
        companyName = findViewById(R.id.text_company_name_input);
        custEmail = findViewById(R.id.text_email_input);
        companyPhone = findViewById(R.id.text_phone_input);
        custMobile = findViewById(R.id.text_custMobile_input);
        custAddress = findViewById(R.id.text_address_input);
        custIC = findViewById(R.id.text_custIc_input);


        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save: {

                Toast toast = Toast.makeText(getApplicationContext(), "Please fill up the error field", Toast.LENGTH_SHORT);

                if(custName.getText().toString().isEmpty() || custIC.getText().toString().isEmpty() || custEmail.getText().toString().isEmpty() || companyPhone.getText().toString().isEmpty() || custMobile.getText().toString().isEmpty() || companyName.getText().toString().isEmpty() || custAddress.getText().toString().isEmpty()){

                    if(custName.getText().toString().isEmpty())
                        custName.setError("Please enter this field");

                    if(custIC.getText().toString().isEmpty())
                        custIC.setError("Please enter this field");

                    if(custEmail.getText().toString().isEmpty())
                        custEmail.setError("Please enter this field");

                    if(companyPhone.getText().toString().isEmpty())
                        companyPhone.setError("Please enter this field");

                    if(custMobile.getText().toString().isEmpty())
                        custMobile.setError("Please enter this field");

                    if(companyName.getText().toString().isEmpty())
                        companyName.setError("Please enter this field");

                    if(custAddress.getText().toString().isEmpty())
                        custAddress.setError("Please enter this field");

                    toast.show();

                }else if(!custName.getText().toString().matches("[a-zA-Z ]+")) {
                    custName.setError("Only A-Z allow");
                        toast.show();

                } else if(!custIC.getText().toString().matches("^[0-9]*$")) {
                    custIC.setError("Enter your IC without (-)");
                        toast.show();

                } else if(!custIC.getText().toString().matches("^(\\d{12})$") ) {
                    custIC.setError("IC No. have 12 digit");
                        toast.show();

                } else if(!custEmail.getText().toString().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$") ) {
                    custEmail.setError("example@gmail.com");
                        toast.show();

                } else if(!companyPhone.getText().toString().matches("^[0-9]*$")) {
                    companyPhone.setError("Enter your company Phone No. without (-)");
                    toast.show();

                } else if(!companyPhone.getText().toString().matches("^(\\d{9}|\\d{10}|\\d{11}|\\d{12})$") ) {
                        companyPhone.setError("phone No. should be 9-12 digit");
                        toast.show();
                } else if(!custMobile.getText().toString().matches("^[0-9]*$")) {
                        custMobile.setError("Enter your Mobile No. without (-)");
                        toast.show();
                } else if(!custMobile.getText().toString().matches("^(\\d{10}|\\d{11}|\\d{12})$") ) {
                        custMobile.setError("phone No. should be 10-12 digit");
                        toast.show();
                } else {

                    //constructor
                    Customer cust = new Customer("0", custName.getText().toString(), custIC.getText().toString(), custEmail.getText().toString(), companyPhone.getText().toString(), custMobile.getText().toString(), companyName.getText().toString(), radioGender.getText().toString(), radioCustType.getText().toString(), custAddress.getText().toString());
                    AddCust addCust = new AddCust(cust);
                    addCust.execute("");

                    String str_result = "h";
                    try {
                        str_result = new RetrieveCustID().execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.finish();

                    RetrieveCustID retrieveCustID = new RetrieveCustID();
                    retrieveCustID.execute("");
                    Intent intent = new Intent(this, CustomerMain.class);
                    intent.putExtra("CustomerID", latestID2);
                    startActivity(intent);
                    return true;
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

    public class RetrieveCustID extends AsyncTask<String,String,String> {

        RetrieveCustID() {
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

                    String query = " SELECT custID FROM CUSTOMER ORDER BY custID DESC LIMIT 1";
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

    public class AddCust extends AsyncTask<String,String,String> {

        Customer customer;

        AddCust(Customer customer) {

            this.customer = customer;
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
                    String query = "SELECT * FROM CUSTOMER ORDER BY CUSTID DESC LIMIT 1";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    String latestID;

                    if (rs.next()) {
                        latestID = rs.getString(1);
                        int numID = Integer.parseInt(latestID.substring(3,8)) + 1;
                        if (numID < 10)
                            latestID = "CU-0000" + Integer.toString(numID);
                        else if (numID < 100)
                            latestID = "CU-000" + Integer.toString(numID);
                        else if (numID < 1000)
                            latestID = "CU-00" + Integer.toString(numID);
                        else if (numID < 10000)
                            latestID = "CU-0" + Integer.toString(numID);
                        else if (numID < 100000)
                            latestID = "CU-" + Integer.toString(numID);

                        Log.d("ID", latestID);
                    }

                    else {
                        latestID = "CU-00001";
                        Log.d("ID", latestID);
                    }

                    query = "INSERT INTO CUSTOMER VALUES('" + latestID + "', '" + customer.getCustName() + "', '" +
                            customer.getIcNo() + "', '" + customer.getEmail() + "', '" + customer.getPhone() + "', '" +
                            customer.getMobile() + "', '" + customer.getCompanyName() + "', '" + customer.getGender() + "', '" +
                            customer.getCustType() + "', '" + customer.getAddress() + "')";

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

        }
    }
}
