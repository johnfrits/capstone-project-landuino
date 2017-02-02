package com.example.admin123.smsams.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.admin123.smsams.GetSensorValueDesc;
import com.example.admin123.smsams.R;
import com.example.admin123.smsams.request.GetAllAreaRequest;
import com.example.admin123.smsams.request.GetPlantRequest;
import com.example.admin123.smsams.request.SaveSoilDataRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SaveSoilLocationDataActivity extends AppCompatActivity {

    GetSensorValueDesc getSensorDesc;
    // SweetAlertDialog pDialog = new SweetAlertDialog(SaveSoilLocationDataActivity.this, SweetAlertDialog.WARNING_TYPE);
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_soil_location_data);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //declare
        final Button btnSave = (Button) findViewById(R.id.btn_save);
        final Button btnRunAnalyze = (Button) findViewById(R.id.btn_run_analyze);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final TextView tv_readable_soil_data = (TextView) findViewById(R.id.tv_readable_soil_data);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.colorbruhLinearLayout);
        getSensorDesc = new GetSensorValueDesc();

        final String user_id = getIntent().getExtras().getString("user_id");
        //get extra values
        final String soilData = getIntent().getExtras().getString("soilData");
        final String locationData = getIntent().getExtras().getString("locationData");




        //show readable data
        if (soilData.length() > 0) {
            tv_readable_soil_data.setText(getSensorDesc.ReturnSoilValueDescription(Integer.valueOf(soilData)));

            //set color background
            if (getSensorDesc.ReturnSoilValueDescription(Integer.valueOf(soilData)).equals("In Water")) {
                linearLayout.setBackgroundResource(R.color.in_water);
            }
            if (getSensorDesc.ReturnSoilValueDescription(Integer.valueOf(soilData)).equals("Humid Soil")) {
                linearLayout.setBackgroundResource(R.color.humid_soil);
            }
            if (getSensorDesc.ReturnSoilValueDescription(Integer.valueOf(soilData)).equals("Dry Soil")) {
                linearLayout.setBackgroundResource(R.color.dry_soil);
            }
        }

        final String soil_moisture = tv_readable_soil_data.getText().toString();
        ListView listView = (ListView) findViewById(R.id.plant_recom_list);

        // Initializing a new Plant Array
        final List<String> listPlant = new ArrayList<>();

        Response.Listener<String> responseListener1 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);
                        String plant_name = obj.getString("plant_name");
                        listPlant.add(plant_name);
                        arrayAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetPlantRequest registerRequest1 = new GetPlantRequest(soil_moisture, responseListener1);
        RequestQueue queue1 = Volley.newRequestQueue(SaveSoilLocationDataActivity.this);
        queue1.add(registerRequest1);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listPlant);
        listView.setAdapter(arrayAdapter);

        //Add Data to Spinner
        String[] arraySpinner = new String[]{
                "Sandy", "Silty", "Clay", "Peaty", "Loam"
        };
        final Spinner spinnerSoilType = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinnerSoilType.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
                        adapter1.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetAllAreaRequest registerRequest = new GetAllAreaRequest("2", responseListener);
        RequestQueue queue = Volley.newRequestQueue(SaveSoilLocationDataActivity.this);
        queue.add(registerRequest);

        final Spinner spinnerAreaType = (Spinner) findViewById(R.id.spinnerArea);
        adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerAreaType.setAdapter(adapter1);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
                final String selectedPrivacy = (String) radioButton.getText();


                if (!selectedPrivacy.isEmpty()) {

                    new SweetAlertDialog(SaveSoilLocationDataActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Save Data")
                            .setContentText("Soil and Location Data is ready to be save.")
                            .setConfirmText("Save")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(final SweetAlertDialog sDialog) {

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    sDialog
                                                            .setTitleText("Success")
                                                            .setContentText("Soil and Location Data Saved.")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    finish();
                                                                }
                                                            })
                                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                } else {
                                                    sDialog
                                                            .setTitleText("Failed")
                                                            .setContentText("Soil and Location Can't Be Save")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(null)
                                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                }
                                            } catch (JSONException e) {
                                                sDialog
                                                        .setTitleText("Something Went Wrong")
                                                        .setContentText("Please Try Again.")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(null)
                                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                                e.printStackTrace();
                                            }
                                        }
                                    };


                                    SaveSoilDataRequest saveSoilDataRequest = new SaveSoilDataRequest(
                                            spinnerSoilType.getSelectedItem().toString(), soil_moisture,
                                            soilData, spinnerAreaType.getSelectedItem().toString(),
                                            selectedPrivacy, locationData, user_id,
                                            responseListener);

                                    RequestQueue queue = Volley.newRequestQueue(SaveSoilLocationDataActivity.this);
                                    queue.add(saveSoilDataRequest);
                                }
                            }).show();
                } else {
                    new SweetAlertDialog(SaveSoilLocationDataActivity.this)
                            .setTitleText("Please Complete All Fields.")
                            .show();
                }
            }
        });

        btnRunAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
