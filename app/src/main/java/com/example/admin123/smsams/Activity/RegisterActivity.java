package com.example.admin123.smsams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.admin123.smsams.R;
import com.example.admin123.smsams.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText nUsername = (EditText) findViewById(R.id.eTxt_username_register);
        final EditText nPassword = (EditText) findViewById(R.id.eTxt_password_register);
        final EditText retypePassword = (EditText) findViewById(R.id.eTxt_retype_password);
        final Button bRegister = (Button) findViewById(R.id.buttonRegister);
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = nUsername.getText().toString();
                final String password = nPassword.getText().toString();
                final String repassword = retypePassword.getText().toString();

                if (isEmptyFields(username, password, repassword)) {
                    builder.setMessage("Please provide all fields")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                } else {
                    if (isMatchPassword(password, repassword)) {

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        Intent intent = new Intent(RegisterActivity.this,
                                                LoginActivity.class);
                                        Toast.makeText(getApplicationContext(), "Register Completed",
                                                Toast.LENGTH_SHORT).show();
                                        RegisterActivity.this.startActivity(intent);
                                        finish();
                                    } else {
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

                        RegisterRequest registerRequest = new RegisterRequest(username, password, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                        queue.add(registerRequest);
                    } else {
                        builder.setMessage("Password Don't Match")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                }
            }
        });
    }

    static boolean isMatchPassword(String pw1, String pw2) {

        final boolean result;

        if (pw1.equals(pw2))
            result = true;
        else
            result = false;

        return result;
    }

    static boolean isEmptyFields(String n1, String pw1, String pw2) {
        final boolean result;

        if (n1.isEmpty() || pw1.isEmpty() || pw2.isEmpty())
            result = true;
        else
            result = false;

        return result;
    }
}
