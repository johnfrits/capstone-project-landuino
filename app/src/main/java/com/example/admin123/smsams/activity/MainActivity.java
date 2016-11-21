package com.example.admin123.smsams.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin123.smsams.R;
import com.example.admin123.smsams.SessionManager;
import com.felhr.usbserial.UsbSerialDevice;
import com.google.android.gms.vision.text.Text;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private Drawer result = null;
    TextView tv_soil_data, tv_arduino_connection, tv_location;
    SessionManager session;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    public final String ACTION_USB_PERMISSION = "com.example.admin123.smsams.USB_PERMISION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button btn_showlist = (Button) findViewById(R.id.btn_showlist);
        tv_arduino_connection = (TextView) findViewById(R.id.tv_arduincon);
        tv_location = (TextView) findViewById(R.id.tv_location);

        session = new SessionManager(this.getApplicationContext());
        session.isLoggedin();
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        String userid = user.get(SessionManager.KEY_USERID);
        String username = user.get(SessionManager.KEY_USERNAME);

        new DrawerBuilder().withActivity(this).build();
        CreateDrawer(CreateAccountDrawer(), toolbar);

        btn_showlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ListActivity.class);
                startActivity(i);
            }
        });

        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
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
        else{
            tv_arduino_connection.setText("NOT CONNECTED");
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            tv_location.setText("IT'S OK");
        }
        else{
            tv_location.setText("IM NOT OK");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        result.deselect();
    }

    //CREATE DRAWER
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    private AccountHeader CreateAccountDrawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.drawable.header1)
                .addProfiles(
                        new ProfileDrawerItem().withName("New User").withIcon(getResources()
                                .getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        return headerResult;

    }

    private void CreateDrawer(AccountHeader header, Toolbar toolbar) {

        PrimaryDrawerItem p_item1 = new PrimaryDrawerItem().withSetSelected(true).withIdentifier(1)
                .withName(R.string.map).withIcon(FontAwesome.Icon.faw_map);
        PrimaryDrawerItem p_item2 = new PrimaryDrawerItem().withIdentifier(2)
                .withName(R.string.analyze_soil).withIcon(FontAwesome.Icon.faw_bullseye);
        PrimaryDrawerItem p_item3 = new PrimaryDrawerItem().withIdentifier(3)
                .withName(R.string.drawer_item_statistic).withIcon(FontAwesome.Icon.faw_line_chart);
        //
        SecondaryDrawerItem s_item1 = new SecondaryDrawerItem().withIdentifier(4)
                .withName(R.string.drawer_item_app_settings).withIcon(FontAwesome.Icon.faw_cog);
        SecondaryDrawerItem s_item2 = new SecondaryDrawerItem().withIdentifier(5)
                .withName(R.string.drawer_item_account_settings).withIcon(FontAwesome.Icon.faw_cog);
        SecondaryDrawerItem s_item3 = new SecondaryDrawerItem().withIdentifier(6)
                .withName(R.string.logout).withIcon(FontAwesome.Icon.faw_sign_out);

        //Create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(header)
                .withToolbar(toolbar)
                .addDrawerItems(
                        p_item2,
                        p_item1,
                        p_item3,
                        new DividerDrawerItem(),
                        s_item1,
                        s_item2,
                        new DividerDrawerItem(),
                        s_item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        int clickedID = (int) drawerItem.getIdentifier();
                        Intent i;
                        switch (clickedID) {
                            case 1:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MainActivity.this, MapsActivity.class);
                                startActivity(i);
                                break;
                            case 2:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MainActivity.this, AnalyzeSoilActivity.class);
                                startActivity(i);
                                break;
                            case 3:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MainActivity.this, StatisticActivity.class);
                                startActivity(i);
                                break;
                            case 4:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MainActivity.this, AppSettingActivity.class);
                                startActivity(i);
                                break;
                            case 5:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MainActivity.this, AccountSettingActivity.class);
                                startActivity(i);
                                break;
                            case 6:
                                session.logoutUser();
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }
}
