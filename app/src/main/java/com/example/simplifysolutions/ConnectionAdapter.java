package com.example.simplifysolutions;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionAdapter {
    @SuppressLint("NewApi")
    public static Connection connectionclass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ConnectionURL = "jdbc:jtds:sqlserver://mysqlserverscs.database.windows.net:1433;DatabaseName=NewSimplySolutionsDatabase;user=azureuser@mysqlserverscs;password=Windows1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}
