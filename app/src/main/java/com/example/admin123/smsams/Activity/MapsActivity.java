package com.example.admin123.smsams.activity;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.admin123.smsams.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //create drawer
        new DrawerBuilder().withActivity(this).build();
        CreateDrawer(CreateAccountDrawer(), toolbar);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(7.1118466, 125.4913899);
        mMap.addMarker(new MarkerOptions().position(sydney).title("BRUH"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        final Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(7.1118466, 125.4913899))
                .radius(10)
                .strokeColor(ContextCompat.getColor(this, R.color.primary))
                .fillColor(ContextCompat.getColor(this, R.color.colorAccent)));


        /*ValueAnimator vAnimator = new ValueAnimator();
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.REVERSE);
        vAnimator.setIntValues(0, 100);
        vAnimator.setDuration(500);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                // Log.e("", "" + animatedFraction);
                circle.setRadius(animatedFraction * 10);
            }
        });
        vAnimator.start();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        result.deselect();
    }

    @Override
    protected void onPause() {
        super.onPause();

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
                        new ProfileDrawerItem().withName("Johnfrits Rejaba").withIcon(getResources()
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
                        p_item1,
                        p_item2,
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
                            case 2:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MapsActivity.this, AnalyzeSoilActivity.class);
                                startActivity(i);
                                break;
                            case 3:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MapsActivity.this, StatisticActivity.class);
                                startActivity(i);
                                break;
                            case 4:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MapsActivity.this, AppSettingActivity.class);
                                startActivity(i);
                                break;
                            case 5:
                                drawerItem.withSetSelected(false);
                                i = new Intent(MapsActivity.this, AccountSettingActivity.class);
                                startActivity(i);
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }
}
