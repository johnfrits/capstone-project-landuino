package com.example.admin123.smsams.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.smsams.GPS_Service;
import com.example.admin123.smsams.R;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.skyfishjy.library.RippleBackground;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyzeSoilActivity extends AppCompatActivity {

    private Button btnAnalyze;
    private TextView textViewSoilData;
    private TextView textViewLocationData;
    private String locationData;
    private String soilData;
    private Intent i;
    private UsbManager usbManager;
    private UsbDevice device;
    private UsbSerialDevice serialPort;
    private AnimatorSet animatorSoil;
    private AnimatorSet animatorLocation;
    private UsbDeviceConnection connection;
    private Boolean clicked, foundedSoil, foundedLocation, prefclicked;
    private BroadcastReceiver broadcastReceiver;
    public final String ACTION_USB_PERMISSION = "com.example.admin123.smsams.USB_PERMISION";
    private ImageView foundSoil;
    private ImageView foundLocation;
    private RippleBackground rippleBackground;
    private Integer sec1;
    private Integer sec2;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_soil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();
    }

    private void init() {

        btnAnalyze = (Button) findViewById(R.id.btn_analyze);
        textViewSoilData = (TextView) findViewById(R.id.textViewSoilData);
        textViewLocationData = (TextView) findViewById(R.id.textViewLocationData);
        foundSoil = (ImageView) findViewById(R.id.foundSoil);
        foundLocation = (ImageView) findViewById(R.id.foundLocation);
        rippleBackground = (RippleBackground) findViewById(R.id.content);

        i = new Intent(getApplicationContext(), GPS_Service.class);
        final Boolean arduino_con = getIntent().getExtras().getBoolean("arduino_con");
        final Boolean loc_en = getIntent().getExtras().getBoolean("location_en");

        if (!arduino_con || !loc_en) {

            btnAnalyze.setEnabled(false);
            btnAnalyze.setBackgroundResource(R.color.disabled_analyze_btn);
            if (!arduino_con && loc_en) {
                Toast.makeText(getApplicationContext(), "Arduino Connection is not establish",
                        Toast.LENGTH_LONG).show();
            }
            if (!loc_en && arduino_con) {
                Toast.makeText(getApplicationContext(), "Location Is Disabled",
                        Toast.LENGTH_LONG).show();
            }
            if (!loc_en && !arduino_con) {
                Toast.makeText(getApplicationContext(), "\rGo to App Settings to\r\n\rConfigure Connection\r",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            runtime_permissions();
            enable_buttons();
        }
    }

    /*LOCATION DATA*/
    @Override
    protected void onResume() {
        super.onResume();

        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    textViewLocationData.setText("\n" + intent.getExtras().get("coordinates"));
                    locationData = textViewLocationData.getText().toString();
                    foundedLocation = true;

                    if (foundedLocation) {
                        locationIconAnimate();
                    }
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    private void filters() {

        usbManager = (UsbManager) getSystemService(USB_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiverArduino, filter);

    }


    private void foundSoil() {
        animatorSoil = new AnimatorSet();
        animatorSoil.setDuration(800);
        animatorSoil.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundSoil, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundSoil, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSoil.playTogether(animatorList);
        foundSoil.setVisibility(View.VISIBLE);
        animatorSoil.start();
    }

    private void foundLocation() {
        animatorLocation = new AnimatorSet();
        animatorLocation.setDuration(800);
        animatorLocation.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundLocation, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundLocation, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorLocation.playTogether(animatorList);
        foundLocation.setVisibility(View.VISIBLE);
        animatorLocation.start();
    }


    private void locationIconAnimate() {

        sec1 = 5000;

        final Handler handlerLocation = new Handler();
        handlerLocation.postDelayed(new Runnable() {
            @Override
            public void run() {
                foundLocation();
                btnAnalyze.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnAnalyze.setText(R.string.complete);
                        btnAnalyze.setBackgroundResource(R.drawable.ripple_anim_button_anaylse_complete);
                        clicked = true;
                    }
                }, 1000);
            }
        }, sec1);
    }

    private void soilIconAnimate() {

        sec2 = 5000;
        final Handler handlerSoil = new Handler();
        handlerSoil.postDelayed(new Runnable() {
            @Override
            public void run() {
                foundSoil();
            }
        }, sec2);
    }

    private void destroy() {
        rippleBackground.stopRippleAnimation();
        clicked = false;
        foundSoil.setVisibility(View.INVISIBLE);
        foundLocation.setVisibility(View.INVISIBLE);
        stopService(i);
        serialPort.close();
        textViewSoilData.setText(" ");
        textViewLocationData.setText(" ");
        btnAnalyze.setText(R.string.analyze);
        btnAnalyze.setBackgroundResource(R.drawable.ripple_anim_button_analyse);
    }

    private void enable_buttons() {

        clicked = false;
        prefclicked = false;
        btnAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters();
                if (!clicked && !prefclicked) {
                    rippleBackground.startRippleAnimation();
                    startService(i);
                    onClickStart();
                    soilIconAnimate();
                    btnAnalyze.setText("Analyzing");
                    btnAnalyze.setBackgroundResource(R.drawable.ripple_anim_button_analyse_red);
                    prefclicked = true;

                } else if (clicked && prefclicked) {
                    if (!soilData.isEmpty() && !locationData.isEmpty()) {
                        if (foundedLocation && foundedSoil) {
                            Toast.makeText(getApplicationContext(), "SOIL DATA: " + soilData + "LOCATION DATA: " + locationData,
                                    Toast.LENGTH_LONG).show();
                            prefclicked = false;
                            Intent i = new Intent(getApplicationContext(), SaveSoilLocationDataActivity.class);
                            i.putExtra("soilData", soilData);
                            i.putExtra("locationData", locationData);
                            startActivity(i);
                            destroy();
                        }
                    }
                }
            }
        });
    }

    private void runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else {
                runtime_permissions();
            }
        }
    }
    /*LOCATION DATA*/

    /*SOIL DATA*/

    public String removeAmpersandAtLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '@') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private String GetAverageSoilData(String fck) {

        Integer sum = 0;
        List<String> list = Splitter.on('@').trimResults().splitToList(fck);

        List<Integer> ints = Lists.transform(list, new Function<String, Integer>() {
            public Integer apply(String input) {
                return Integer.valueOf(input);
            }
        });

        for (Integer i = 0; i < ints.size(); i++) {
            sum += ints.get(i);
        }

        sum = sum / ints.size();

        return String.valueOf(sum);
    }

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                final String finalData = data;
                foundedSoil = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (textViewSoilData.getText().toString().length() > 4 || textViewSoilData.getText().toString().length() > 3) {
                            if (Integer.valueOf(GetAverageSoilData(removeAmpersandAtLast(textViewSoilData.getText().toString()))) > 0) {
                                soilData = GetAverageSoilData(removeAmpersandAtLast(textViewSoilData.getText().toString()));
                            }
                        }
                        textViewSoilData.append(finalData);
                    }
                });

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };


    private final BroadcastReceiver broadcastReceiverArduino = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    if (serialPort != null) {
                        if (serialPort.open()) {
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);
                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            }
        }
    };

    public void onClickStart() {
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 0x2341)  //Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }
                if (!keep)
                    break;
            }
        }
    }
   /*SOIL DATA*/
}
