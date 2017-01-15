package com.example.admin123.smsams.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.admin123.smsams.R;
import com.example.admin123.smsams.request.GetContentListRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InfoViewActivity extends AppCompatActivity {

    private ListContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Area Soil Read");
        setupActionBar();
        setupList();
    }

    private void setupList() {

        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new ListContentAdapter(this, createList());
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (adapter != null) {
            adapter.saveStates(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (adapter != null) {
            adapter.restoreStates(savedInstanceState);
        }
    }

    private List<String> createList() {

        final String area_name = getIntent().getExtras().getString("area_name");
        final String privacy = getIntent().getExtras().getString("privacy");
        final boolean[] success = {false};
        final SweetAlertDialog pDialog = new SweetAlertDialog(InfoViewActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Getting Soil List Read...");
        pDialog.setCancelable(true);
        pDialog.show();


        final List<String> list = new ArrayList<>();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);
                        String name = obj.getString("name");
                        list.add(name);
                        success[0] = true;
                    }

                    if (success[0] && !list.isEmpty()) {

                        pDialog
                                .setTitleText("Soil List Read Completed")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    } else {
                        pDialog
                                .setTitleText("Soil List Read Empty")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetContentListRequest registerRequest = new GetContentListRequest(area_name, privacy, responseListener);
        RequestQueue queue = Volley.newRequestQueue(InfoViewActivity.this);
        queue.add(registerRequest);


        return list;
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
