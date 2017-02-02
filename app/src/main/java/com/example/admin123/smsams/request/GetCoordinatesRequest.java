package com.example.admin123.smsams.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetCoordinatesRequest extends StringRequest {

    private static final String GET_COORDINATES_URL = "http://smsams.bsitcapstone.com/smsams-android-script/get_soil_coordinates.php";
    //private static final String GET_COORDINATES_URL = "http://smsams.bsitcapstone.com/smsams-android-script/get_soil_coordinates.php";
    private Map<String, String> params;

    public GetCoordinatesRequest(String get_coordinates, Response.Listener<String> listener) {
        super(Method.POST, GET_COORDINATES_URL, listener, null);

        params = new HashMap<>();
        params.put("get_coordinates", get_coordinates);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
