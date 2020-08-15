package com.example.orderandinventorysystem.ui.staff;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.HomeOne;
import com.example.orderandinventorysystem.HomeThree;
import com.example.orderandinventorysystem.HomeTwo;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.invoice.add_new_invoice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class loginStaff extends AppCompatActivity {
private Button button;
String name;
    EditText et, et1;
    boolean check=false, clerk=false, delivery=false, stockManager=false, admin=false, check2=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et = (EditText) findViewById(R.id.username);
        et1 = (EditText) findViewById(R.id.editTextTextPassword);
        et.setHintTextColor(Color.WHITE);
        et1.setHintTextColor(Color.WHITE);

        button = findViewById(R.id.Login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckLogin checkLogin = new CheckLogin(et.getText().toString(), et1.getText().toString());
                checkLogin.execute("");
            }
        });
    }

    public class CheckLogin extends AsyncTask<String,String,String> {

        String checkConnection = "";
        boolean isSuccess = false;
        String id, pass;

        CheckLogin(String id, String pass) {

            this.id=id;
            this.pass= pass;
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

                    check=false; clerk=false; delivery=false; stockManager=false; admin=false; check2=false;
                    String query = " SELECT * FROM STAFF WHERE STAFFID='" + id  + "' AND PASSWORD='" + pass + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        name = rs.getString(2);
                        if (rs.getString(5).equals("Clerk"))
                            clerk=true;
                        else if (rs.getString(5).equals("Stock Management Staff"))
                            stockManager=true;
                        else if (rs.getString(5).equals("Human Resource Staff"))
                            admin=true;
                        else if (rs.getString(5).equals("Delivery Staff"))
                            delivery =true;
                        else if (rs.getString(5).equals("Admin"))
                            check = true;
                        else if (rs.getString(5).equals("Admin2"))
                            check2 = true;
                    }


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

               if(clerk) {
                   Intent intent = new Intent(loginStaff.this, HomeOne.class);

                   startActivity(intent);

               } else if (stockManager) {
                   Intent intent = new Intent(loginStaff.this, HomeThree.class);

                   startActivity(intent);
               } else if (admin) {
                   Intent intent = new Intent(loginStaff.this, StaffManagementMainMenu.class);

                   startActivity(intent);
               }
               else if (delivery) {
                   Intent intent = new Intent(loginStaff.this, HomeTwo.class);

                   startActivity(intent);
               }
               else if (check) {
                   Intent intent = new Intent(loginStaff.this, StaffManagementMainMenu.class);

                   startActivity(intent);
               }

               else if (check2) {
                   Intent intent = new Intent(loginStaff.this, HomeOne.class);

                   startActivity(intent);
               } else {
                   Toast.makeText(loginStaff.this, "Invalid staff ID or password. Please try again.", Toast.LENGTH_LONG).show();
               }
        }
    }
}