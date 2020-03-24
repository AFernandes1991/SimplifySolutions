package com.example.simplifysolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;

import com.braintreepayments.cardform.view.CardForm;

public class PaymentActivity extends AppCompatActivity {
    Button btnBuy;
    CardForm cfBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        btnBuy = findViewById(R.id.btnBuy);
        cfBuy = findViewById(R.id.card_form);

        cfBuy.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(PaymentActivity.this);
        cfBuy.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }

}
