package com.example.admin123.smsams.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.admin123.smsams.R;
import com.example.admin123.smsams.request.GetAreaListRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AreaListActivity extends AppCompatActivity {

    private ListAdapter adapter;
    private String viewPublic = "False";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();
        setupList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem viewPublicList = menu.findItem(R.id.public_list_rad);

        viewPublicList.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (viewPublic.equals("False")) {
                    viewPublic = "True";
                    viewPublicList.setChecked(true);
                    setupList();
                } else {
                    viewPublic = "False";
                    viewPublicList.setChecked(false);
                    setupList();
                }

                return false;
            }
        });
        return true;
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

    private void setupList() {

        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new ListAdapter(this, createList());
        listView.setAdapter(adapter);
    }

    private List<String> createList() {

        final String user_id = getIntent().getExtras().getString("user_id");
        final boolean[] success = {false};
        final SweetAlertDialog pDialog = new SweetAlertDialog(AreaListActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Getting Area List...");
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
                        String areaName = obj.getString("area_name");
                        list.add(areaName);
                        success[0] = true;
                    }

                    if (success[0]) {

                        pDialog
                                .setTitleText("Area List Completed")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetAreaListRequest registerRequest = new GetAreaListRequest(user_id, viewPublic, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AreaListActivity.this);
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
