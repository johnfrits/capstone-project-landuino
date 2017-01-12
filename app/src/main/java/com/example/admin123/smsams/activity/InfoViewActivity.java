package com.example.admin123.smsams.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.admin123.smsams.R;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoViewActivity extends AppCompatActivity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_view);

        final String area_name = getIntent().getExtras().getString("area_name");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(area_name);

        lv = (ListView) findViewById(R.id.list_view1);
        ArrayList<HashMap<String, String>> feedList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("date", "1/7");
        map.put("description", "gift her");
        map.put("price", "23");
        map.put("discount", "25");
        feedList.add(map);

        map = new HashMap<>();
        map.put("date", "1/8");
        map.put("description", "nice phone");
        map.put("price", "67");
        map.put("discount", "50");
        feedList.add(map);

        map = new HashMap<>();
        map.put("date", "1/6");
        map.put("description", "hello");
        map.put("price", "33");
        map.put("discount", "50");
        feedList.add(map);


        map = new HashMap<>();
        map.put("date", "1/3");
        map.put("description", "yo");
        map.put("price", "123");
        map.put("discount", "33");
        feedList.add(map);


        map = new HashMap<>();
        map.put("date", "1/2");
        map.put("description", "nice phone");
        map.put("price", "67");
        map.put("discount", "50");
        feedList.add(map);


        map = new HashMap<>();
        map.put("date", "23/12");
        map.put("description", "nice car");
        map.put("price", "6700");
        map.put("discount", "50");
        feedList.add(map);


        map = new HashMap<>();
        map.put("date", "4/3");
        map.put("description", "nice phone");
        map.put("price", "678");
        map.put("discount", "70");
        feedList.add(map);

        map = new HashMap<>();
        map.put("date", "1/12");
        map.put("description", "Yummy burger");
        map.put("price", "12");
        map.put("discount", "10");
        feedList.add(map);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, feedList, R.layout.view_item, new String[]{"date", "description", "price", "discount"}, new int[]{R.id.textViewDate, R.id.textViewDescription, R.id.textViewDiscount, R.id.textViewPrice});
        lv.setAdapter(simpleAdapter);


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
