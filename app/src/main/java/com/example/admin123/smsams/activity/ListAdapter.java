package com.example.admin123.smsams.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.admin123.smsams.R;

import java.util.List;

/**
 * Created by Chau Thai on 4/12/16.
 */
public class ListAdapter extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper;
    private Context context;

    public ListAdapter(Context context, List<String> objects) {
        super(context, R.layout.row_list, objects);
        mInflater = LayoutInflater.from(context);
        binderHelper = new ViewBinderHelper();
        this.context = context;
        // uncomment if you want to open only one row at a time
        // binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_list, parent, false);

            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            holder.statView = convertView.findViewById(R.id.statistic_layout);
            holder.mapView = convertView.findViewById(R.id.map_layout);
            holder.infoView = convertView.findViewById(R.id.info_layout);
            holder.swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String item = getItem(position);
        if (item != null) {
            binderHelper.bind(holder.swipeLayout, item);

            holder.textView.setText(item);
            holder.statView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, StatisticActivity.class);
                    context.startActivity(i);
                }
            });
            holder.mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, MapsActivity.class);
                    context.startActivity(i);
                }
            });
            holder.infoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, InfoViewActivity.class);
                    context.startActivity(i);
                }
            });
        }

        return convertView;
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    private class ViewHolder {
        TextView textView;
        View statView;
        View mapView;
        View infoView;
        SwipeRevealLayout swipeLayout;
    }
}
