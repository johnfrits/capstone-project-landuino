package com.example.admin123.smsams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.admin123.smsams.R;
import com.example.admin123.smsams.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

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

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = nUsername.getText().toString();
                final String password = nPassword.getText().toString();
                final String repassword = retypePassword.getText().toString();

                final SweetAlertDialog pDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                if (isEmptyFields(username, password, repassword)) {
                    pDialog
                            .setTitleText("Please provide the input fields.")
                            .setConfirmText("Retry")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    pDialog.dismiss();
                                }
                            })
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                } else {
                    if (username.length() < 6 || password.length() < 6) {
                        pDialog
                                .setTitleText("Error!")
                                .setContentText("Username and Password have a minimum length of 6 characters.")
                                .setConfirmText("Retry")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    } else {
                        if (isMatchPassword(password, repassword)) {

                            Response.Listener<String> responseListener =  new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        final int sec = 2000;
                                        if (success) {
                                            pDialog
                                                    .setTitleText("Registration Successful!")
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                            final Handler handlerFinishActivity = new Handler();
                                            handlerFinishActivity.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }, sec);
                                        } else {
                                            pDialog
                                                    .setTitleText("Registration Failed!")
                                                    .setConfirmText("Retry")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            pDialog.dismiss();
                                                        }
                                                    })
                                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            RegisterRequest registerRequest = new RegisterRequest(username, repassword, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                            queue.add(registerRequest);
                        } else {
                            pDialog
                                    .setTitleText("Password Don't Match")
                                    .setConfirmText("Retry")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            pDialog.dismiss();
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }
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
