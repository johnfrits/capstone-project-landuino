package com.example.admin123.smsams.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AccountSettingRequest extends StringRequest {

    private static final String ACCOUNT_SETTING_URL = "http://192.168.1.5:8080/smsams/account_setting.php";
    private Map<String, String> params;

    public AccountSettingRequest(String fname, String lname,
                                 String new_password, String old_password,
                                 String userid, String changePassword, String editProfile,
                                 Response.Listener<String> listener) {

        super(Method.POST, ACCOUNT_SETTING_URL, listener, null);

        params = new HashMap<>();
        params.put("fname", fname);
        params.put("lname", lname);
        params.put("new_password", new_password);
        params.put("old_password", old_password);
        params.put("user_id", userid);
        params.put("changePassword", changePassword);
        params.put("editProfile", editProfile);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
