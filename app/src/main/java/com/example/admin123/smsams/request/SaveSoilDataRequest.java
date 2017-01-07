package com.example.admin123.smsams.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SaveSoilDataRequest extends StringRequest {

    private static final String SAVE_SOIL_LOCATION_REQUEST_URL = "http://192.168.1.7:8080/smsams-android-script/save_soil_location_data.php";
    //private static final String SAVE_SOIL_LOCATION_REQUEST_URL = "http://smsams.bsitcapstone.com/smsams-android-script/save_soil_location_data.php";
    private Map<String, String> params;

    public SaveSoilDataRequest(String soil_type, String moisture_value,
                               String area_name, String soil_privacy, String coordinates,
                               Response.Listener<String> listener) {

        super(Method.POST, SAVE_SOIL_LOCATION_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("soil_type", soil_type);
        params.put("soil_moisture_value", moisture_value);
        params.put("soil_area_name", area_name);
        params.put("soil_privacy", soil_privacy);
        params.put("soil_coordinates", coordinates);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
