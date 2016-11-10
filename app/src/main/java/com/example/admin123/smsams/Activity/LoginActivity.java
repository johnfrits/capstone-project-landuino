package com.example.admin123.smsams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin123.smsams.R;


public class LoginActivity extends AppCompatActivity {

    TextView register_link;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_link = (TextView) findViewById(R.id.tv_register_link);
        btnLogin = (Button) findViewById(R.id.buttonSignIn);

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
                Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                LoginActivity.this.startActivity(i);
                finish();
            }
        });

    }
}
