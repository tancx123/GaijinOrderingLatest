package com.example.orderandinventorysystem.ui.purchase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Bill;
import com.example.orderandinventorysystem.Model.Payment;
import com.example.orderandinventorysystem.R;
import com.example.orderandinventorysystem.ui.bill.BillMain;
import com.example.orderandinventorysystem.ui.payment.PaymentMain;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Purchase_Payments extends Fragment {

    View root;
    Bill bill;
    String intentInvoiceID;
    ImageButton img;
    boolean payCheck=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_invoice_payment, container, false);
        Intent intent = getActivity().getIntent();
        intentInvoiceID = intent.getStringExtra("Purchase");
        PayDetails payDetails = new PayDetails(intentInvoiceID);
        payDetails.execute("");

        img = root.findViewById(R.id.payment_button);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BillMain.class);
                intent.putExtra("Bill", intentInvoiceID);
                startActivityForResult(intent, 3);
            }
        });
        return root;
    }

    public class PayDetails extends AsyncTask<String,String,String> {

        String intentInvoiceID;

        PayDetails(String intentInvoiceID) {
            this.intentInvoiceID = intentInvoiceID;
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

                    String query = " SELECT * FROM BILL WHERE pID ='" + intentInvoiceID + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {

                        bill = new Bill(rs.getString(1), rs.getString(2),
                                rs.getString(3), rs.getString(4),
                                rs.getDouble(5));
                        payCheck = true;

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

            if (payCheck) {

                TextView click = root.findViewById(R.id.click);
                click.setVisibility(View.VISIBLE);

                TextView payment_id = root.findViewById(R.id.payment_id);
                payment_id.setText(bill.getBillID());

                TextView payment_amount = root.findViewById(R.id.payment_amount);
                payment_amount.setText(String.format("MYR%.2f", bill.getBillAmount()));

                TextView payment_date = root.findViewById(R.id.payment_date);
                payment_date.setText(bill.getBillDate());

                TextView payment_method = root.findViewById(R.id.payment_method);
                payment_method.setText("PAID");

                TableRow tb1 = root.findViewById(R.id.payRow1);
                tb1.setVisibility(View.VISIBLE);
                tb1 = root.findViewById(R.id.payRow2);
                tb1.setVisibility(View.VISIBLE);

                img.setVisibility(View.VISIBLE);
            }

        }
    }

}
