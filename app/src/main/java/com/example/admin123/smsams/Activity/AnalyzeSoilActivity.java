package com.example.admin123.smsams.activity;

import android.Manifest;
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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin123.smsams.GPS_Service;
import com.example.admin123.smsams.R;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.skyfishjy.library.RippleBackground;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeSoilActivity extends AppCompatActivity {

    Button btnAnalyze;
    TextView textViewSoilData;
    TextView textViewLocationData;
    String soilData;
    String locationData;
    public final String ACTION_USB_PERMISSION = "com.example.admin123.smsams.USB_PERMISION";
    Intent i;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    boolean clicked;
    private BroadcastReceiver broadcastReceiver;

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
        textViewSoilData = (TextView) findViewById(R.id.textView3);
        textViewLocationData = (TextView) findViewById(R.id.textView4);
        i = new Intent(getApplicationContext(), GPS_Service.class);
        final Boolean arduino_con = getIntent().getExtras().getBoolean("arduino_con");
        final Boolean loc_en = getIntent().getExtras().getBoolean("location_en");

        if (!arduino_con || !loc_en) {
            btnAnalyze.setEnabled(false);
            btnAnalyze.setBackgroundResource(R.color.disabled_analyze_btn);

            if (!arduino_con && loc_en) {
                Toast.makeText(getApplicationContext(), "Arduino Connection is not establish",
                        Toast.LENGTH_LONG).show();

            } else if (!loc_en && arduino_con) {
                Toast.makeText(getApplicationContext(), "Location Is Disabled",
                        Toast.LENGTH_LONG).show();

            } else {
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

    private void enable_buttons() {

        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);
        clicked = false;

        btnAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //filter
                filters();

                if (!clicked) {
                    rippleBackground.startRippleAnimation();
                    startService(i);
                    onClickStart();
                    btnAnalyze.setText("Stop");
                    btnAnalyze.setBackgroundResource(R.drawable.ripple_anim_button_analyse_red);
                    clicked = true;
                } else if (clicked) {
                    Toast.makeText(getApplicationContext(), soilData + " " + locationData,
                            Toast.LENGTH_SHORT).show();
                    rippleBackground.stopRippleAnimation();
                    btnAnalyze.setText(R.string.analyze);
                    btnAnalyze.setBackgroundResource(R.drawable.ripple_anim_button_analyse);
                    clicked = false;
                    onClickStop();
                    stopService(i);
                }
            }
        });
    }

    private void runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                data.concat("/n");
                tvAppend(textViewSoilData, data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    public void onClickStop() {
        textViewSoilData.setEnabled(false);
        serialPort.close();
        textViewSoilData.setText(soilData);
    }

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

    private final BroadcastReceiver broadcastReceiverArduino = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
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


    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
                soilData = ftv.getText().toString();
            }
        });


    }
   /*SOIL DATA*/

}
