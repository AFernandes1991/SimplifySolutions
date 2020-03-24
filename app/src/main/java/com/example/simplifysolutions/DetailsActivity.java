package com.example.simplifysolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

    }


    public class displayDetails extends AsyncTask<String, String, String> {
        ListView lvDetails;
        ArrayList<ResultSet> details;
        ArrayAdapter arrayAdapter;
        Boolean isSuccess = false;




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           lvDetails = findViewById(R.id.lvDetails);
            details = new ArrayList<ResultSet>();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = ConnectionAdapter.connectionclass();
            if(con != null){
                isSuccess = false;
            }
            else{
                String queryStmt = "select * from cartDetails";
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(queryStmt);
                while (resultSet.next()) {
                    try{
//                        details.add(resultSet.getString("name"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
//                    StringBuffer sb = new StringBuffer();
//                    sb.append("User ID: " + resultSet.getString(0) + "\n");
//                    sb.append("First Name: " + resultSet.getString(1) + "\n");
//                    sb.append("Last Name: " + resultSet.getString(2) + "\n");
//                    sb.append("Password: " + resultSet.getString(3) + "\n");
//                    sb.append("Date & Time: " + resultSet.getString(4) + "\n");
//                    sb.append("Rating: " + resultSet.getString(5) + "\n");
//                    details.add(sb);
                }
                isSuccess = true;
            }
                return "";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (Exception e) {
                return "Error. Please check your connection";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (isSuccess) {
                    //finish();
//                    arrayAdapter = new ArrayAdapter(details, DetailsActivity.this)
                    lvDetails.setAdapter(arrayAdapter);
                }
            } catch (Exception e) {
            }
        }
    }
}


