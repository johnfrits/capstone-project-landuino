package com.example.admin123.smsams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.admin123.smsams.R;
import com.example.admin123.smsams.SessionManager;
import com.example.admin123.smsams.request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends AppCompatActivity {

    TextView register_link;
    Button btnLogin;
    EditText eTxtUsername, eTxtPassword;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        register_link = (TextView) findViewById(R.id.tv_register_link);
        btnLogin = (Button) findViewById(R.id.buttonSignIn);
        eTxtUsername = (EditText) findViewById(R.id.eTxt_username);
        eTxtPassword = (EditText) findViewById(R.id.eTxt_password);


        session = new SessionManager(this);
        session.isLoggedin();

        /*Set On Clicks*/
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

                final String username_ = eTxtUsername.getText().toString();
                final String password_ = eTxtPassword.getText().toString();

                final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                if (isEmptyFields(username_, password_)) {
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
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                final boolean success = jsonResponse.getBoolean("success");
                                final int sec = 2000;

                                if (success) {
                                    final String userid_ = String.valueOf(jsonResponse.getInt("userid"));
                                    final String firstname = jsonResponse.getString("fname");
                                    final String lastname = jsonResponse.getString("lname");
                                    //create session
                                    session.createLoginSession(firstname, lastname, userid_);
                                    //show this
                                    pDialog
                                            .setTitleText("Login Success!")
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                    final Handler handlerFinishActivity = new Handler();
                                    handlerFinishActivity.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            i.putExtra("user_id", userid_);
                                            startActivity(i);
                                            finish();
                                        }
                                    }, sec);
                                } else {
                                    pDialog
                                            .setTitleText("Login Failed!")
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

                    LoginRequest loginRequest = new LoginRequest(username_, password_, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            }
        });
    }

    static boolean isEmptyFields(String n1, String pw1) {
        final boolean result;

        if (n1.isEmpty() || pw1.isEmpty())
            result = true;
        else
            result = false;

        return result;
    }
}
