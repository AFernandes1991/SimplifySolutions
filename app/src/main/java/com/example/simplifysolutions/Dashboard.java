package com.example.simplifysolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void create(View view) {
    Intent intent = new Intent(this, CreateShipment.class);
    startActivity(intent);
}
    public void calculation(View view) {
        Intent intent = new Intent(this, RateCalc.class);
        startActivity(intent);
    }
}
