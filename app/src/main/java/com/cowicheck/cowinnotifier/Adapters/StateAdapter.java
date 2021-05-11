package com.cowicheck.cowinnotifier.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cowicheck.cowinnotifier.Models.State;

public class StateAdapter extends ArrayAdapter<State> {

    private Context context;
    private State[] values;

    public StateAdapter(@NonNull Context context, int resource, @NonNull State[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.values= objects;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Nullable
    @Override
    public State getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return values[position].getState_id();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values[position].getState_name());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values[position].getState_name());

        return label;
    }


}
