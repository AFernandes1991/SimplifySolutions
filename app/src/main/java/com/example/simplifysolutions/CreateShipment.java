package com.example.simplifysolutions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplifysolutions.config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateShipment extends AppCompatActivity {
    EditText address, pincode,weight,length, breadth, height, ordValue, ordQty;
    Button add, chkout;
    Spinner spntype;
    String[] type = new String[]{"Parcel", "Box", "Skid"};//adapter for spinner

    //Paypal
    private static final int PAYPAL_REQUEST_CODE=7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPaynow;
    EditText edtAmount;
    String amout="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shipment);
        getSupportActionBar().setTitle("Create Shipment");
        address=findViewById(R.id.etPickupAddress);
        pincode=findViewById(R.id.etPin);
        weight=findViewById(R.id.etWeight);
        length=findViewById(R.id.etLength);
        breadth=findViewById(R.id.etBreadth);
        height=findViewById(R.id.etHeight);
        ordValue=findViewById(R.id.etOrdValue);
        ordQty=findViewById(R.id.etOrdQty);
        add = findViewById(R.id.btnAddToCart);
        chkout = findViewById(R.id.btnCheckout);
        spntype = findViewById(R.id.spnType);
        final ArrayAdapter<String> aryShift = new ArrayAdapter<>(this, R.layout.simple_list_item_1, type);
        spntype.setAdapter(aryShift);
        add = findViewById(R.id.btnAddToCart);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createShipment cs = new createShipment();
                cs.execute();
            }
        });
        chkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CreateShipment.this, PaymentDetails.class);
//                startActivity(intent);

                processPayment();

            }
        });

        //Paypal
        Intent intent= new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


    }

    public class createShipment extends AsyncTask<String, String, String> {
        String add, pin, type, w, l, b, h, oval,oqty;
        String msg = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            add = address.getText().toString();
            pin = pincode.getText().toString();
            w = weight.getText().toString();
            l = length.getText().toString();
            b = breadth.getText().toString();
            h = height.getText().toString();
            oval = ordValue.getText().toString();
            oqty = ordQty.getText().toString();
            type = spntype.getSelectedItem().toString();
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = ConnectionAdapter.connectionclass();
                String query = "Insert into cartDetails (Address, Pincode,PackageType, Weight, Length, Breadth, Height,OrderValue, OrderQty) values ('" + add + "','" + pin + "','" + type + "','" + w + "','" + l + "','" + b + "','" + h + "','" + oval + "','" + oqty + "')";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                isSuccess = true;

                return "Successful";
            }
            catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (Exception e) {
                return "Error. Please check your connection";
            }
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(CreateShipment.this, r, Toast.LENGTH_SHORT).show();

            if (isSuccess) {


            }
        }
    }


    private void processPayment() {
        amout="199.99";
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(String.valueOf(amout)),"USD","Donate for EDMtdev",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent =new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amout)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
}
