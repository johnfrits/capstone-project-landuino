package com.example.admin123.smsams.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.admin123.smsams.R;
import com.example.admin123.smsams.SessionManager;
import com.felhr.usbserial.UsbSerialDevice;
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
    Boolean arduino_con, location_enabled;
    String userid, firstname, lastname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //declare shits
        tv_arduino_connection = (TextView) findViewById(R.id.tv_arduincon);
        tv_location = (TextView) findViewById(R.id.tv_location);

        //shared preference
        session = new SessionManager(this.getApplicationContext());
        session.isLoggedin();
        session.checkLogin();

        //get pref firstname, lastname, userid
        HashMap<String, String> user = session.getUserDetails();
        userid = user.get(SessionManager.KEY_USERID);
        firstname = user.get(SessionManager.KEY_FIRSTNAME);
        lastname = user.get(SessionManager.KEY_LASTNAME);

        //create drawer
        new DrawerBuilder().withActivity(this).build();
        CreateDrawer(CreateAccountDrawer(), toolbar);
        //checking connection
        checkArduinoConnection();
        checkLocationEnabled();

    }

    void checkLocationEnabled() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            tv_location.setText(R.string.loc_enabled);
            location_enabled = true;
        } else {
            tv_location.setText(R.string.loc_disabled);
            location_enabled = false;
        }
    }

    void checkArduinoConnection() {

        usbManager = (UsbManager) getSystemService(USB_SERVICE);
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 0x2341) {
                    tv_arduino_connection.setText(R.string.arduino_connected);
                    arduino_con = true;
                } else {
                    tv_arduino_connection.setText(R.string.arduino_not_connected);
                    arduino_con = false;
                }
            }
        } else {
            tv_arduino_connection.setText(R.string.arduino_not_connected);
            arduino_con = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        result.deselect();
        checkArduinoConnection();
        checkLocationEnabled();
    }

    /*CREATE DRAWER*/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    private AccountHeader CreateAccountDrawer() {

        return new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.drawable.header1)
                .addProfiles(
                        new ProfileDrawerItem().withName(firstname + " " + lastname).withIcon(getResources()
                                .getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

    }

    private void CreateDrawer(AccountHeader header, Toolbar toolbar) {

        PrimaryDrawerItem p_item1 = new PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.analyze_soil).withIcon(FontAwesome.Icon.faw_bullseye);
        PrimaryDrawerItem p_item2 = new PrimaryDrawerItem().withIdentifier(2)
                .withName(R.string.area_list).withIcon(FontAwesome.Icon.faw_globe);
        //
        SecondaryDrawerItem s_item1 = new SecondaryDrawerItem().withIdentifier(3)
                .withName(R.string.drawer_item_app_settings).withIcon(FontAwesome.Icon.faw_cog);
        SecondaryDrawerItem s_item2 = new SecondaryDrawerItem().withIdentifier(4)
                .withName(R.string.drawer_item_account_settings).withIcon(FontAwesome.Icon.faw_cog);
        SecondaryDrawerItem s_item3 = new SecondaryDrawerItem().withIdentifier(5)
                .withName(R.string.logout).withIcon(FontAwesome.Icon.faw_sign_out);

        //Create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(header)
                .withToolbar(toolbar)
                .addDrawerItems(
                        p_item1,
                        p_item2,
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
                                i = new Intent(MainActivity.this, AnalyzeSoilActivity.class);
                                i.putExtra("arduino_con", arduino_con);
                                i.putExtra("location_en", location_enabled);
                                startActivity(i);
                                break;
                            case 2:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MainActivity.this, AreaListActivity.class);
                                startActivity(i);
                                break;
                            case 3:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MainActivity.this, AppSettingActivity.class);
                                startActivity(i);
                                break;
                            case 4:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MainActivity.this, AccountSettingActivity.class);
                                i.putExtra("user_id", userid);
                                startActivity(i);
                                break;
                            case 5:
                                onBackPressed();
                                session.logoutUser();
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
