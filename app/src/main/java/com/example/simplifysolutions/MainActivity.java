package com.example.simplifysolutions;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    EditText Name, Password;
    Button Login, Register;
    private int counter = 5;
    SignInButton signin;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=0;


    String name, email, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin= findViewById(R.id.sign_in_button);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleReg greg = new googleReg();
                greg.execute();

                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Name = findViewById(R.id.etUsername);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btnLogin);
        Register = findViewById(R.id.btnRegister);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
//                validate(Name.getText().toString(), Password.getText().toString());

            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            name = personName;
            email =  personEmail;
            id = personId;
//            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public class CheckLogin extends AsyncTask<String, String, String> {
        String u, p;
        String msg = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            u = Name.getText().toString();
            p = Password.getText().toString();
        }


        @Override
        protected String doInBackground(String... params) {
            if (u.trim().equals("") || p.trim().equals(""))
                msg = "Please enter Username and Password";
            else {
                try {
                    Connection con = ConnectionAdapter.connectionclass();        // Connect to database
                    if (con == null) {
                        msg = "Check Your Internet Access!";
                    } else {
                        String query = "select * from AdminTest where Username= '" + u + "' and Password = '" + p + "' ";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            msg = "Accept to continue";
                            isSuccess = true;
                            con.close();
                        } else {
                            msg = "Invalid Credentials!";
                            isSuccess = false;
                        }
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    msg = ex.getMessage();
                }
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {

                terms();
            }
        }
    }


    public void terms()
    {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Terms and Conditions")
                .setMessage("\n" +
                        "SIMPLIFY SUPPLY CHAIN SOLUTIONS INC. TERMS AND CONDITIONS OF CARRIAGE (“Terms\n" +
                        "and Conditions”)\n" +
                        "When ordering SIMPLIFY SUPPLY CHAIN SOLUTIONS INC’s services you, as “Shipper”, are agreeing, on your\n" +
                        "behalf and on behalf of the receiver of the Shipment (“Receiver”) and anyone else with an interest in the Shipment\n" +
                        "that these Terms and Conditions shall apply.\n" +
                        "“Shipment” means all documents or parcels that travel under one waybill and which may be carried by any means\n" +
                        "SIMPLIFY SUPPLY CHAIN SOLUTIONS INC. chooses, including air, road or any other carrier. A “waybill” shall\n" +
                        "include any Shipment identifier or document produced by SIMPLIFY SUPPLY CHAIN SOLUTIONS INC. or Shipper\n" +
                        "automated systems such as a label, barcode, waybill or consignment note as well as any electronic version thereof.\n" +
                        "Every Shipment is transported on a limited liability basis as provided herein. If Shipper requires greater protection,\n" +
                        "then insurance may be arranged at an additional cost. (Please see below for further information). “SIMPLIFY SCS”\n" +
                        "means any member of the SIMPLIFY SUPPLY CHAIN SOLUTIONS INC. Network.\n" +
                        "-\n" +
                        "1. Customs Clearance:\n" +
                        "SIMPLIFY SCS may perform any of the following activities on Shipper’s or Receiver’s behalf in order to provide its\n" +
                        "services: (1) complete any documents, amend product or service codes, and pay any duties, taxes or penalties\n" +
                        "required under applicable laws and regulations (“Customs Duties”), (2) act as Shipper’s forwarding agent for customs\n" +
                        "and export control purposes and as Receiver solely for the purpose of designating a customs broker to perform\n" +
                        "customs clearance and entry and (3) redirect the Shipment to Receiver’s customs broker or other address upon\n" +
                        "request by any person who SIMPLIFY SCS believes in its reasonable opinion to be authorized.\n" +
                        "2. Unacceptable Shipments:\n" +
                        "A Shipment is deemed unacceptable if:\n" +
                        " no customs declaration is made when required by applicable customs regulations,\n" +
                        " it contains counterfeit goods, animals, bullion, currency, gem stones; weapons, explosives and ammunition;\n" +
                        "human remains; illegal items, such as ivory and narcotics,\n" +
                        " it is classified as hazardous material, dangerous goods, prohibited or restricted articles by IATA (International Air\n" +
                        "Transport Association), ICAO (International Civil Aviation Organization), ADR (European Road Transport\n" +
                        "Regulation on dangerous goods) or other relevant organization (“Dangerous Goods”),\n" +
                        " its address is incorrect or not properly marked or its packaging is defective or inadequate to ensure safe\n" +
                        "transportation with ordinary care in handling,\n" +
                        " it contains any other item which SIMPLIFY SCS decides cannot be carried safely or legally.\n" +
                        "\n" +
                        "3. Deliveries and Undeliverable:\n" +
                        "Shipments cannot be delivered to PO boxes or postal codes. Shipments are delivered to the Receiver’s address\n" +
                        "given by Shipper but not necessarily to the named Receiver personally. Shipments to addresses with a central\n" +
                        "receiving area will be delivered to that area.\n" +
                        "SIMPLIFY SCS may notify Receiver of an upcoming delivery or a missed delivery. Receiver may be offered\n" +
                        "alternative delivery options such as delivery on another day, no signature required, redirection or collection at a\n" +
                        "SIMPLIFY SCS Service Point. Shipper may exclude some delivery options on request.\n" +
                        "If the Shipment is deemed to be unacceptable as described in Section 2, or it has been undervalued for customs\n" +
                        "purposes, or Receiver cannot be reasonably identified or located, or Receiver refuses delivery or to pay Customs\n" +
                        "Duties or other Shipment charges, SIMPLIFY SCS shall use reasonable efforts to return the Shipment to Shipper at\n" +
                        "Shipper’s cost, failing which the Shipment may be released, disposed of or sold without incurring any liability\n" +
                        "whatsoever to Shipper or anyone else, with the proceeds applied against Customs Duties, Shipment charges and\n" +
                        "related administrative costs with the balance of the proceeds of a sale to be returned to Shipper. SIMPLIFY SCS shall\n" +
                        "have the right to destroy any Shipment which any law prevents SIMPLIFY SCS from returning to Shipper as well as\n" +
                        "any Shipment of Dangerous Goods\n" +
                        "4. Inspection:\n" +
                        "SIMPLIFY SCS has the right to open and inspect a Shipment without notice for safety, security, customs or other\n" +
                        "regulatory reasons.\n" +
                        "5. Shipment Charges and Fees:\n" +
                        "SIMPLIFY SCS’s Shipment charges are calculated according to the higher of actual or volumetric weight per piece\n" +
                        "and any piece may be re-weighed and re-measured by SIMPLIFY SCS to confirm this calculation.\n" +
                        "Shipper, or the Receiver when SIMPLIFY SCS acts on Receiver’s behalf, shall pay or reimburse SIMPLIFY SCS for\n" +
                        "all Shipment or other charges due, or Customs Duties owed for services provided by SIMPLIFY SCS or incurred by\n" +
                        "SIMPLIFY SCS on Shipper’s or Receiver’s behalf. Payment of Customs Duties may be requested prior to delivery.\n" +
                        "If SIMPLIFY SCS uses its credit with the Customs Authorities or advances any Customs Duties on behalf of a\n" +
                        "Receiver who does not have an account with SIMPLIFY SCS, SIMPLIFY SCS shall be entitled to assess a fee.\n" +
                        "\n" +
                        "6. SIMPLIFY SUPPLY CHAIN SOLUTIONS INC’s Liability:\n" +
                        "6.1 SIMPLIFY SCS’s liability in respect of any one Shipment transported by air (including ancillary road transport or\n" +
                        "stops en route) is limited by the Montreal Convention or the Warsaw Convention as applicable, or in the absence of\n" +
                        "such Convention, to the lower of (i) the current market or declared value, or (ii) 19 Special Drawing Rights per\n" +
                        "kilogram (approximately $US 26.00 per kilogram). Such limits shall also apply to all other forms of transportation,\n" +
                        "except where Shipments are carried only by road, when the limits below apply.\n" +
                        "For cross border Shipments transported by road, SIMPLIFY SCS’s liability is or shall be deemed to be limited by the\n" +
                        "Convention for the International Carriage of Goods by Road (CMR) to the lower of (i) current market value or\n" +
                        "declared value, or (ii) 8.33 Special Drawing Rights per kilogram (approximately $US 14.00 per kilogram). Such limits\n" +
                        "will also apply to national road transportation in the absence of any mandatory or lower liability limits in the applicable\n" +
                        "national transport law.\n" +
                        "If Shipper regards these limits as insufficient it must make a special declaration of value and request insurance as\n" +
                        "described in Section 8 or make its own insurance arrangements.\n" +
                        "SIMPLIFY SCS’s liability is strictly limited to direct loss and damage to a Shipment only and to the per kilogram limits\n" +
                        "in this Section 6. All other types of loss or damage are excluded (including but not limited to lost profits, income,\n" +
                        "interest, future business), whether such loss or damage is special or indirect, and even if the risk of such loss or\n" +
                        "damage was brought to SIMPLIFY SCS’s attention.\n" +
                        "6.2 SIMPLIFY SCS will make every reasonable effort to deliver the Shipment according to SIMPLIFY SCS’s regular\n" +
                        "delivery schedules, but these schedules are not binding and do not form part of the contract. SIMPLIFY SCS is not\n" +
                        "liable for any damages or loss caused by delay, but for certain Shipments, Shipper may be able to claim limited delay\n" +
                        "compensation under the Money Back Guarantee terms and conditions.\n" +
                        "7. Claims:\n" +
                        "All claims must be submitted in writing to SIMPLIFY SCS within thirty (30) days from the date that SIMPLIFY SCS\n" +
                        "accepted the Shipment, failing which SIMPLIFY SCS shall have no liability whatsoever. Claims are limited to one\n" +
                        "claim per Shipment, settlement of which will be full and final settlement for all loss or damage in connection therewith.\n" +
                        "8. Shipment Insurance:\n" +
                        "SIMPLIFY SCS may be able to arrange insurance covering the value in respect of loss of or damage to the Shipment,\n" +
                        "provided that the Shipper so instructs SIMPLIFY SCS in writing, including by completing the insurance section on the\n" +
                        "front of the waybill or by SIMPLIFY SCS’s automated systems and pays the applicable premium. Shipment insurance\n" +
                        "does not cover indirect loss or damage, or loss or damage caused by delays.\n" +
                        "\n" +
                        "9. Circumstances Beyond SIMPLIFY SCS’s Control:\n" +
                        "SIMPLIFY SCS is not liable for any loss or damage arising out of circumstances beyond SIMPLIFY SCS’s control.\n" +
                        "These include but are not limited to electrical or magnetic damage to, or erasure of, electronic or photographic\n" +
                        "images, data or recordings; any defect or characteristic related to the nature of the Shipment, even if known to\n" +
                        "SIMPLIFY SCS; any act or omission by a person not employed or contracted by SIMPLIFY SCS - e.g. Shipper,\n" +
                        "Receiver, third party, customs or other government official; “Force Majeure” - e.g. earthquake, cyclone, storm, flood,\n" +
                        "fog, war, plane crash, embargo, riot, civil commotion, or industrial action.\n" +
                        "10. Shippers Warranties and Indemnities:\n" +
                        "Shipper shall indemnify and hold SIMPLIFY SCS harmless for any loss or damage arising out of Shipper’s failure to\n" +
                        "comply with the following warranties and representations:\n" +
                        " all information provided by Shipper or its representatives is complete and accurate;\n" +
                        " the Shipment is acceptable for transport under Section 2 above;\n" +
                        " the Shipment was prepared in secure premises by reliable persons and was protected against unauthorized\n" +
                        "interference during preparation, storage and any transportation to SIMPLIFY SCS;\n" +
                        " Shipper has complied with all applicable customs, import, export, data protection laws, sanctions, embargos and\n" +
                        "other laws and regulations; and\n" +
                        " Shipper has obtained all necessary consents in relation to personal data provided to SIMPLIFY SCS including\n" +
                        "Receiver’s data as may be required for transport, customs clearance and delivery, such as e-mail address and\n" +
                        "mobile phone number.\n" +
                        "11. Routing:\n" +
                        "Shipper agrees to all routing and diversion, including the possibility that the Shipment may be carried via intermediate\n" +
                        "stopping places.\n" +
                        "12. Governing Law:\n" +
                        "Any dispute arising under or in any way connected with these Terms and Conditions shall be subject, for the benefit\n" +
                        "of SIMPLIFY SCS, to the non-exclusive jurisdiction of the courts of, and governed by the law of the country of origin\n" +
                        "of the Shipment and Shipper irrevocably submits to such jurisdiction, unless contrary to applicable law\n" +
                        "13. Severability:\n" +
                        "The invalidity or unenforceability of any provision shall not affect any other part of these Terms and Conditions.\n" +
                        " ")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, NewDash.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);

            Intent intent= new Intent(MainActivity.this,secondActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }


    public class googleReg extends AsyncTask<String, String, String> {
        String NAME, EMAIL, ID;
        Boolean isSuccess = false, temp=false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            EMAIL = email;
            NAME = name;
            ID = id;

          //  Toast.makeText(MainActivity.this, EMAIL, Toast.LENGTH_SHORT).show();


            try{
                Connection con = ConnectionAdapter.connectionclass();
                String query1 = "select * from AdminTest where Email= '" +EMAIL+ "' and GFlag = 'Y' ";
                Statement stmt1 = con.createStatement();
                ResultSet rs = stmt1.executeQuery(query1);
                if(rs.next())
                {
                   // Toast.makeText(MainActivity.this, "innnn", Toast.LENGTH_SHORT).show();
                    temp=true;
                }
            }
            catch (SQLException e)
            {

            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {


                if(temp != true)
                {
                    Connection con1 = ConnectionAdapter.connectionclass();
                    String queryStmt = "Insert into AdminTest (Username, Email, GoogleID , GFlag) values ('" + NAME + "','" + EMAIL + "','" + ID + "','Y')";
                    PreparedStatement preparedStatement = con1.prepareStatement(queryStmt);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    isSuccess = true;
                }
                else{
                    isSuccess = true;
                }

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
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Intent intent = new Intent(MainActivity.this, NewDash.class);
                startActivity(intent);
                //finish();
            }
        }
    }
}


