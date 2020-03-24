package com.example.simplifysolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class RateCalc extends AppCompatActivity {
    public static EditText p, d;
    EditText q;
    Spinner t;
    String res;
    Button calc;
    public  static TextView data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        getSupportActionBar().setTitle("Calculation");
        p = findViewById(R.id.etPickupLocation);
        d = findViewById(R.id.ettvDropLocation);
        t = findViewById(R.id.spnTypePackage);
        q = findViewById(R.id.etPackageQty);
        calc = findViewById(R.id.btnCalculator);
        data =  findViewById(R.id.fetcheddata);
         String pick = p.getText().toString();
         String drop = d.getText().toString();


        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData process = new fetchData();
                process.execute();
            }
        });

    }
}
