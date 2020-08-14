package com.example.orderandinventorysystem.ui.pack;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Item;
import com.example.orderandinventorysystem.Model.ItemOrder;
import com.example.orderandinventorysystem.Model.Pack;
import com.example.orderandinventorysystem.Model.Sales;
import com.example.orderandinventorysystem.Model.Shipment;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.sales.ItemOrderListAdapter;
import com.example.orderandinventorysystem.ui.sales.SalesOrderMainFragment;
import com.example.orderandinventorysystem.ui.sales.edit_sales_orders;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PackageMain extends AppCompatActivity {

    Menu menu1;
    String latestShipID;
    int totalQuantity;
    Pack pack;
    ArrayList <Item> itemList;
    ArrayList <ItemOrder> ioList;
    Shipment shipment;
    String intentPackageID;
    boolean shipCheck=false;
    RecyclerView recyclerView;
    ItemPackListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.package_main);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Package");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        intentPackageID = intent.getStringExtra("Package");
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.execute("");

        ioList = new ArrayList<>();
        itemList = new ArrayList<>();
        recyclerView = findViewById(R.id.itemLine_2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemPackListAdapter(this, ioList, itemList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.package_main_menu, menu);
        menu1 = menu;
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 4) {
            setResult(3);
            finish();
            Intent intent = new Intent(this, PackageMain.class);
            intent.putExtra("Package", intentPackageID);
            startActivityForResult(intent, 3);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addShipment: {
                Intent intent = new Intent(this, add_shipment.class);
                intent.putExtra("ShipID", latestShipID);
                intent.putExtra("PackID", intentPackageID);
                startActivityForResult(intent, 10);
                return true;
            }

            case R.id.delivered: {
                MarkDelivered markDelivered = new MarkDelivered();
                markDelivered.execute("");
                setResult(3);
                finish();
                Intent intent = new Intent(this, PackageMain.class);
                intent.putExtra("Package", intentPackageID);
                startActivityForResult(intent, 3);
                return true;
            }

            case R.id.delete: {

                if (shipCheck) {
                    DeleteShipment deleteShipment = new DeleteShipment();
                    deleteShipment.execute("");
                    setResult(3);
                    finish();
                    Intent intent = new Intent(this, PackageMain.class);
                    intent.putExtra("Package", intentPackageID);
                    startActivityForResult(intent, 3);
                }

                else {

                    DeletePack deletePack = new DeletePack();
                    deletePack.execute("");
                    setResult(3);
                    finish();

                }


                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(3);
        this.finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public class PackageInfo extends AsyncTask<String,String,String> {

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

                    Log.d("HAHA", intentPackageID);
                    String query = " SELECT * FROM PACKAGE WHERE packID ='" + intentPackageID + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {

                        pack = new Pack(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getString(4));
                    }

                    query = " SELECT * FROM ITEMORDER WHERE orderID ='" + pack.getSalesID() + "'";
                    stmt = con.createStatement();
                    rs = stmt.executeQuery(query);
                    while (rs.next()) {

                        ioList.add(new ItemOrder(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getDouble(4),
                                rs.getDouble(5), rs.getInt(6), rs.getDouble(7)));
                        totalQuantity += rs.getInt(6);

                        query = " SELECT * FROM ITEM WHERE itemID ='" + rs.getString(2) + "'";
                        stmt = con.createStatement();
                        ResultSet rs2 = stmt.executeQuery(query);

                        if (rs2.next()) {
                            itemList.add(new Item(rs2.getString(1), rs2.getString(2), rs2.getString(3), rs2.getString(4)
                                    , rs2.getInt(5), rs2.getInt(6), rs2.getDouble(7), rs2.getDouble(8), rs2.getString(9)));

                        }

                    }

                    query = " SELECT * FROM SHIPMENT WHERE packID ='" + intentPackageID + "'";
                    stmt = con.createStatement();
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {

                        shipment = new Shipment(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getString(4));
                        shipCheck=true;
                    }

                    query = "SELECT * FROM SHIPMENT ORDER BY SHIPID DESC LIMIT 1";
                    stmt = con.createStatement();
                    rs = stmt.executeQuery(query);
                    String latestID;

                    if (rs.next()) {
                        latestID = rs.getString(1);
                        int numID = Integer.parseInt(latestID.substring(3,8)) + 1;
                        if (numID < 10)
                            latestID = "SH-0000" + Integer.toString(numID);
                        else if (numID < 100)
                            latestID = "SH-000" + Integer.toString(numID);
                        else if (numID < 1000)
                            latestID = "SH-00" + Integer.toString(numID);
                        else if (numID < 10000)
                            latestID = "SH-0" + Integer.toString(numID);
                        else if (numID < 100000)
                            latestID = "SH-" + Integer.toString(numID);

                    }

                    else {
                        latestID = "SH-00001";
                    }

                    latestShipID = latestID;

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

            TextView packID = findViewById(R.id.package_name);
            packID.setText(pack.getPackID());

            TextView packStatus = findViewById(R.id.package_status);
            packStatus.setText(pack.getPackStatus());

            TextView salesDetails = findViewById(R.id.salesDetails);
            salesDetails.setText("Sales order created: " + pack.getSalesID());

            TextView packDetails = findViewById(R.id.packDetails);
            packDetails.setText("Package created: " + pack.getPackID());

            TextView package_date = findViewById(R.id.package_date);
            package_date.setText(pack.getPackDate());

            TextView quantity = findViewById(R.id.quantity);
            quantity.setText(Integer.toString(totalQuantity));

            if (shipCheck) {

                MenuItem menuItem = menu1.findItem(R.id.addShipment);
                menuItem.setVisible(false);
                MenuItem menuItem2 = menu1.findItem(R.id.delete);
                menuItem2.setTitle("Delete Shipment");
                MenuItem menuItem3 = menu1.findItem(R.id.delivered);
                menuItem3.setVisible(true);

                TextView shipDetails = findViewById(R.id.shipDetails);
                shipDetails.setText("Shipped through " + shipment.getCarrier());
                TextView ship_date = findViewById(R.id.ship_date);
                ship_date.setText(shipment.getShipDate());
                TableRow carrierRow = findViewById(R.id.carrierRow);
                carrierRow.setVisibility(View.VISIBLE);
                TableRow shipDateRow = findViewById(R.id.shipDateRow);
                shipDateRow.setVisibility(View.VISIBLE);
                TextView ship_id = findViewById(R.id.ship_id);
                ship_id.setText(shipment.getShipID());
                ship_id.setVisibility(View.VISIBLE);
                TextView shipIDText = findViewById(R.id.shipIDText);
                shipIDText.setVisibility(View.VISIBLE);

            }

            if (pack.getPackStatus().equals("DELIVERED")) {

                TableRow tbd = findViewById(R.id.deliveredRow);
                tbd.setVisibility(View.VISIBLE);
                MenuItem menui = menu1.findItem(R.id.delivered);
                menui.setVisible(false);
                MenuItem menuItem = menu1.findItem(R.id.addShipment);
                menuItem.setVisible(false);
                MenuItem menuItem2 = menu1.findItem(R.id.delete);
                menuItem2.setVisible(false);
            }

            recyclerView.setAdapter(adapter);
        }
    }

    public class DeletePack extends AsyncTask<String,String,String> {

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

                    String query = " DELETE FROM PACKAGE WHERE PACKID ='" + intentPackageID + "'";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    for (int i=0; i < ioList.size(); i++) {

                        query = "UPDATE ITEM SET ITEMQUANTITYPHY= ITEMQUANTITYPHY + '" + ioList.get(i).getQuantity() + "' WHERE ITEMID='" + ioList.get(i).getItemID() + "'";
                        stmt = con.createStatement();
                        stmt.executeUpdate(query);
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

    public class DeleteShipment extends AsyncTask<String,String,String> {

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

                    String query = " DELETE FROM Shipment WHERE PACKID ='" + intentPackageID + "'";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    query = " UPDATE PACKAGE SET PACKSTATUS='PACKED' WHERE PACKID ='" + intentPackageID + "'";
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

        }
    }

    public class MarkDelivered extends AsyncTask<String,String,String> {

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

                    String query = " UPDATE PACKAGE SET PACKSTATUS='DELIVERED' WHERE PACKID ='" + intentPackageID + "'";
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
        protected void onPostExecute(String s) {

        }
    }
}
