package com.example.admin123.smsams.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.admin123.smsams.R;
import com.example.admin123.smsams.request.AccountSettingRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AccountSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        final EditText fname = (EditText) findViewById(R.id.et_fname);
        final EditText lname = (EditText) findViewById(R.id.et_lname);
        final EditText old_pw = (EditText) findViewById(R.id.et_oldpw);
        final EditText new_pw = (EditText) findViewById(R.id.et_newpw);
        final EditText re_pw = (EditText) findViewById(R.id.et_repassword);
        final Button btn_save = (Button) findViewById(R.id.btn_save_account_settings);
        final String user_id = getIntent().getExtras().getString("user_id");


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(AccountSettingActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
                String firstname = "", lastname = "", oldPassword = "", newPassword = "", rePassword;
                boolean isUpdated = false;
                String editProfile = "false", changePass = "false";

                if (!fname.getText().toString().equals("") && !lname.getText().toString().equals("")) {

                    firstname = fname.getText().toString();
                    lastname = lname.getText().toString();
                    isUpdated = true;
                    editProfile = "true";
                } else if (!old_pw.getText().toString().equals("") && !new_pw.getText().toString().equals("") && !re_pw.getText().toString().equals("")) {

                    oldPassword = old_pw.getText().toString();
                    newPassword = new_pw.getText().toString();
                    rePassword = re_pw.getText().toString();

                    if (isMatchPassword(newPassword, rePassword)) {
                        isUpdated = true;
                        changePass = "true";
                    } else {
                        pDialog
                                .setTitleText("Password Don't Match")
                                .setConfirmText("Retry")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismiss();
                                    }
                                })
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                } else {

                    pDialog
                            .setTitleText("Please Provide Input Fields")
                            .setConfirmText("Retry")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    pDialog.dismiss();
                                }
                            })
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }


                if (isUpdated) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    pDialog
                                            .setTitleText("Update Successful!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    fname.setText("");
                                                    lname.setText("");
                                                    old_pw.setText("");
                                                    new_pw.setText("");
                                                    re_pw.setText("");
                                                    pDialog.dismiss();
                                                }
                                            })
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                } else {
                                    pDialog
                                            .setTitleText("Update Failed!")
                                            .setConfirmText("Retry")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    pDialog.dismiss();
                                                }
                                            })
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    AccountSettingRequest accountSettingRequest = new AccountSettingRequest(firstname, lastname, newPassword,
                            oldPassword, user_id, changePass, editProfile, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(AccountSettingActivity.this);
                    queue.add(accountSettingRequest);
                }

            }

        });
    }

    static boolean isMatchPassword(String pw1, String pw2) {

        final boolean result;

        if (pw1.equals(pw2))
            result = true;
        else
            result = false;

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
