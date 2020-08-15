package com.example.orderandinventorysystem.ui.invoice;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.orderandinventorysystem.ConnectionPhpMyAdmin;
import com.example.orderandinventorysystem.Model.Invoice;
import com.example.orderandinventorysystem.Model.Sales;
import com.example.orderandinventorysystem.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class edit_new_invoice extends AppCompatActivity {

    Invoice invoiceEdit;
    EditText dueDate;
    String invoiceLatestID;
    Calendar myCalendar = Calendar.getInstance();
    Date today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_invoice);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Invoice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        invoiceEdit = (Invoice) intent.getSerializableExtra("InvoiceEdit");
        TextView tx1 = findViewById(R.id.text_customer_name_input);
        tx1.setText(invoiceEdit.getInvCustName());
        TextView text_order_num_input = findViewById(R.id.text_order_num_input);
        text_order_num_input.setText(invoiceEdit.getSalesID());
        TextView text_invoice_date_input = findViewById(R.id.text_invoice_date_input);
        text_invoice_date_input.setText(invoiceEdit.getSalesID());
        dueDate = findViewById(R.id.text_due_date_input);
        dueDate.setText(invoiceEdit.getInvDueDate());

        myCalendar.set(Calendar.HOUR_OF_DAY, 0);
        myCalendar.set(Calendar.MINUTE, 0);
        myCalendar.set(Calendar.SECOND, 0);
        myCalendar.set(Calendar.MILLISECOND, 0);

        today = myCalendar.getTime();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                Date dateEnter = myCalendar.getTime();

                if(dateEnter.before(today)){
                    dueDate.setError("You cannot choose a past date");
                    Toast toast = Toast.makeText(getApplicationContext(),"You cannot choose a past date, please reselect a valid date", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else{
                    updateLabel();
                }

            }
        };

        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(edit_new_invoice.this,date,myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dueDate.setText(sdf.format(myCalendar.getTime()));
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

                if(!dueDate.getText().toString().isEmpty()){

                    AddInvoice addInvoice = new AddInvoice(dueDate.getText().toString());
                    addInvoice.execute("");
                    setResult(5, getIntent().putExtra("Invoice", invoiceEdit.getInvID()));
                    finish();

                }else{
                    dueDate.setError("Please enter a valid date !");
                }
                //constructor
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    public class AddInvoice extends AsyncTask<String,String,String> {

        String dueDate;
        String checkConnection = "";
        boolean isSuccess = false;

        AddInvoice(String dueDate) {
            this.dueDate = dueDate;
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

                    String query = "UPDATE INVOICE SET INVDUEDATE='" + dueDate + "' WHERE SALESID='" + invoiceEdit.getSalesID() + "'";
                    Log.d("HAHA", invoiceEdit.getSalesID());
                    Statement stmt = con.createStatement();
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
            Toast.makeText(edit_new_invoice.this, "Invoice edited", Toast.LENGTH_LONG).show();
        }
    }

}
