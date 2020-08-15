package com.example.orderandinventorysystem.ui.pack;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.sales.SalesOrderMainFragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class add_shipment extends AppCompatActivity {

    String intentShipID, intentPackID;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipment);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Shipment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        intentShipID = intent.getStringExtra("ShipID");
        intentPackID = intent.getStringExtra("PackID");
        editText = findViewById(R.id.text_carrier_input);
        TextView tx = findViewById(R.id.text_shipment_date_input);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tx.setText(currentDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save: {

                if (!editText.getText().toString().isEmpty()) {

                    AddShipment addShipment = new AddShipment(editText.getText().toString());
                    addShipment.execute("");

                    setResult(4);
                    finish();
                }

                else {
                    editText.setError("Please enter the carrier name !");
                }

                return true;
            }

            default:
                return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    public class AddShipment extends AsyncTask<String,String,String> {

        String carrier;

        AddShipment(String carrier) {

            this.carrier = carrier;
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

                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                    String query = " INSERT INTO SHIPMENT VALUES('" + intentShipID + "','" + intentPackID + "','" + currentDate + "','" + carrier + "')" ;
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    query = " UPDATE PACKAGE SET PACKSTATUS='SHIPPED' WHERE PACKID='" + intentPackID + "'" ;
                    stmt = con.createStatement();
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
        protected void onPostExecute(String s) {

            Toast.makeText(add_shipment.this, "Shipment added", Toast.LENGTH_LONG).show();
        }
    }
}
