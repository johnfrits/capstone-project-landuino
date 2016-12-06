package com.example.admin123.smsams.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin123.smsams.GetSensorValueDesc;
import com.example.admin123.smsams.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SaveSoilLocationDataActivity extends AppCompatActivity {

    GetSensorValueDesc getSensorDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_soil_location_data);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //declare
        final Button btnSave = (Button) findViewById(R.id.btn_save);
        final Button btnRunAnalyze = (Button) findViewById(R.id.btn_run_analyze);
        final Intent i = new Intent(getApplicationContext(), AnalyzeSoilActivity.class);
        TextView tv_readable_soil_data = (TextView) findViewById(R.id.tv_readable_soil_data);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.colorbruhLinearLayout);
        getSensorDesc = new GetSensorValueDesc();

        //get extra values
        String soilData = getIntent().getExtras().getString("soilData");
        String locationData = getIntent().getExtras().getString("locationData");

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

        //Add Data to Spinner
        String[] arraySpinner = new String[]{
                "Sandy", "Silty", "Clay", "Peaty", "Loam"
        };
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog pDialog = new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
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
