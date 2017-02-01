package com.example.admin123.smsams.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetContentRequest extends StringRequest {

    private static final String GET_AREA_INFO = "http://192.168.1.8:8080/smsams-android-script/get_soil_content.php";
    //private static final String GET_AREA_LIST_URL = "http://smsams.bsitcapstone.com/smsams-android-script/get_area_list.php";
    private Map<String, String> params;

    public GetContentRequest(String content_name, Response.Listener<String> listener) {
        super(Method.POST, GET_AREA_INFO, listener, null);

        params = new HashMap<>();
        params.put("content_name", content_name);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
