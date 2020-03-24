package com.example.simplifysolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class trial extends AppCompatActivity {
    TextView try1, try2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        try1 = findViewById(R.id.try1);
        try2 = findViewById(R.id.try2);

        try {
            Connection con = ConnectionAdapter.connectionclass();
            String queryStmt = "select Weight from cartDetails";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(queryStmt);

            if (resultSet.next()) {

                try1.setText(resultSet.getString("Weight").toString());
            }

        }
        catch (Exception e)
        {

        }


    }
}
