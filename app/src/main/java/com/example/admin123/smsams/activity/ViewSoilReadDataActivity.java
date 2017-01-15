package com.example.admin123.smsams.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.admin123.smsams.R;
import com.example.admin123.smsams.request.GetContentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewSoilReadDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_soil_read_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        final String content_name = getIntent().getExtras().getString("content_name");
        getSupportActionBar().setTitle(content_name);


        final TextView type = (TextView) findViewById(R.id.tv_type);
        final TextView soil_moisture = (TextView) findViewById(R.id.tv_soil_moisture);
        final TextView moisture_value = (TextView) findViewById(R.id.tv_moisture_value);
        final TextView latitude = (TextView) findViewById(R.id.tv_latitude);
        final TextView longitude = (TextView) findViewById(R.id.tv_longitude);
        final TextView status = (TextView) findViewById(R.id.tv_status);
        final TextView dta = (TextView) findViewById(R.id.tv_dta);

        final boolean[] success = {false};
        final SweetAlertDialog pDialog = new SweetAlertDialog(ViewSoilReadDataActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Getting content...");
        pDialog.setCancelable(true);
        pDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);
                        type.setText(obj.getString("type"));
                        soil_moisture.setText(obj.getString("soil_moisture"));
                        moisture_value.setText(obj.getString("moisture_value"));
                        latitude.setText(obj.getString("latitude"));
                        longitude.setText(obj.getString("longitude"));
                        status.setText(obj.getString("status"));
                        dta.setText(obj.getString("date_time_access"));
                        success[0] = true;
                    }

                    if (success[0]) {
                        pDialog.dismiss();
                    } else {
                        pDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetContentRequest registerRequest = new GetContentRequest(content_name, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ViewSoilReadDataActivity.this);
        queue.add(registerRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
