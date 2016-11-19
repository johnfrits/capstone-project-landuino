package com.example.admin123.smsams.activity;

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
import com.example.admin123.smsams.SessionManager;
import com.example.admin123.smsams.request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;


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

        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

                if (isEmptyFields(username_, password_)) {
                    builder.setMessage("Please provide all fields")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                final boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    final String userid_ = String.valueOf(jsonResponse.getInt("userid"));

                                    //create session
                                    session.createLoginSession(username_, userid_);

                                    Toast.makeText(getApplicationContext(), "Login Success",
                                            Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
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
