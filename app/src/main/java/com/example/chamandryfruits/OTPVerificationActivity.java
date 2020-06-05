package com.example.chamandryfruits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Random;

public class OTPVerificationActivity extends AppCompatActivity  {

    private TextView phoneNo;
    private EditText otp;
    private Button verify;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_pverification);

        phoneNo = findViewById(R.id.phone_no);
        otp = findViewById(R.id.otp);
        verify = findViewById(R.id.verify);

        number = getIntent().getStringExtra("mobileNo");
        phoneNo.setText("Verification code ha been sent to +92 " + number);

        Random random = new Random();
        int otpNumber = random.nextInt(999999 - 111111) + 111111;

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryActivity.codOrderConfirmed = true;
                finish();
                    /*
                    if(otp.getText().toString().equals("123456")){
                        DeliveryActivity.codOrderConfirmed = true;
                        finish();
                    }else {
                        Toast.makeText(OTPVerificationActivity.this, "Incorrect verification code!", Toast.LENGTH_SHORT).show();
                    }

                     */

            }
        });

        /*
        String username = "923045316190";
        String password = "jf6rFV@vi4u6ELC";
        String ComapnyName = "Chaman Dry Fruits";
        String message = "Dear Customer your verification code is: " + otpNumber;
        String smsApi = "https://sendpk.com/api/sms.php?username=" + username + "&password=" + password + "&mobile=" + username + "&sender=" + ComapnyName + "ComapnyName&message=" + message;


        try {

            URL url = new URL(smsApi);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();;
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String urlString = "";
            String current;

            while ((current = in.readLine()) != null) {
                urlString += current;
            }

            final String finalUrlString = urlString;


            //System.out.println(urlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


         */
    }

}
