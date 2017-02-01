package com.example.admin123.smsams.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetPlantRequest extends StringRequest {

    private static final String GET_PLANT_RECOM_REQUEST_URL = "http://192.168.1.8:8080/smsams-android-script/get_plant_recom.php";
    //private static final String SAVE_SOIL_LOCATION_REQUEST_URL = "http://smsams.bsitcapstone.com/smsams-android-script/save_soil_location_data.php";
    private Map<String, String> params;

    public GetPlantRequest(String soil_moisture,
                           Response.Listener<String> listener) {

        super(Method.POST, GET_PLANT_RECOM_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("soil_moisture", soil_moisture);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
