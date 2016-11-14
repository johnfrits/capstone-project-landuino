package com.example.admin123.smsams.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.admin123.smsams.R;
import com.example.admin123.smsams.request.LoginRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;


public class LoginActivity extends AppCompatActivity {

    TextView register_link;
    Button btnLogin;
    AVLoadingIndicatorView loginAvi;
    EditText eTxtUsername, eTxtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        register_link = (TextView) findViewById(R.id.tv_register_link);
        btnLogin = (Button) findViewById(R.id.buttonSignIn);
//        loginAvi = (AVLoadingIndicatorView) findViewById(R.id.loginavi);
        eTxtUsername = (EditText) findViewById(R.id.eTxt_username);
        eTxtPassword = (EditText) findViewById(R.id.eTxt_password);


        register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginResponseListener(eTxtUsername, eTxtPassword);
//              Intent i = new Intent(LoginActivity.this, MapsActivity.class);
//              LoginActivity.this.startActivity(i);
//              finish();
            }
        });
    }

    void LoginResponseListener(EditText username, EditText password) {


        final String username_ = username.getText().toString();
        final String password_ = password.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                        LoginActivity.this.startActivity(i);
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Login Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        LoginRequest loginRequest = new LoginRequest(username_, password_, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);

    }
}
