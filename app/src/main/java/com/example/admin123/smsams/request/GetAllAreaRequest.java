package com.example.admin123.smsams.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetAllAreaRequest extends StringRequest {

    private static final String GET_AREA_LIST_URL = "http://192.168.1.7:8080/smsams-android-script/get_area.php";
    //private static final String GET_AREA_LIST_URL = "http://smsams.bsitcapstone.com/smsams-android-script/get_area_list.php";
    private Map<String, String> params;

    public GetAllAreaRequest(String user_id, Response.Listener<String>listener) {
        super(Method.POST, GET_AREA_LIST_URL, listener, null);

        params = new HashMap<>();
        params.put("user_id", user_id);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
