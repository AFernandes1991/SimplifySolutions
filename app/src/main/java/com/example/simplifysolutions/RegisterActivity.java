package com.example.simplifysolutions;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity {

    TextView Label;
    EditText Email, RegUser, RegPassword;
    Button GetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Label = findViewById(R.id.etRegisterLabel);
        Email = findViewById(R.id.etEmail);
        RegUser = findViewById(R.id.etRegName);
        RegPassword = findViewById(R.id.etRegPassword);
        GetStarted = findViewById(R.id.btnRegister);
        GetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser reg = new registerUser();
                reg.execute("");
            }
        });
    }

    public class registerUser extends AsyncTask<String, String, String> {
        String email, username, password;
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email = Email.getText().toString();
            username = RegUser.getText().toString();
            password = RegPassword.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = ConnectionAdapter.connectionclass();
                String queryStmt = "Insert into AdminTest values ('" + username + "','" + password + "','" + email + "')";
                PreparedStatement preparedStatement = con.prepareStatement(queryStmt);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                isSuccess = true;

                return "Registration Successful";
            }
            catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (Exception e) {
                return "Error. Please check your connection";
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }
        }
    }
}
