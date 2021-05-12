package com.cowicheck.cowinnotifier.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cowicheck.cowinnotifier.Models.District;

import org.jetbrains.annotations.NotNull;

public class DistrictAdapter extends ArrayAdapter<District> {

    private Context context;
    private District[] values;

    public DistrictAdapter(@NonNull Context context, int resource, @NonNull District[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Nullable
    @Override
    public District getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return values[position].getDistrict_id();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values[position].getDistrict_name());

        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values[position].getDistrict_name());

        return label;
    }
}
