package com.cowicheck.cowinnotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cowicheck.cowinnotifier.Models.CenterSession;
import com.cowicheck.cowinnotifier.Models.Session;
import com.cowicheck.cowinnotifier.Models.UserData;
import com.cowicheck.cowinnotifier.Services.CheckSlotService;
import com.google.gson.Gson;

public class NotifyActivity extends AppCompatActivity {

    private TextView selectedText;
    private SharedPreferences sharedPreferences;
    private UserData getUserData;
    private Button startListening;
    private RadioGroup radioGroup;
    private String age = "18 +";
    private Typeface boldTypeFace, lightTypeFace, regularTypeFace;
    private Button redirectBtn, soundBtn;

    int dpToPixel(int val) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;

        return (int)(scale * val + 0.5f);
    }

    void setFoundSlots(SharedPreferences sharedPreferences) {
        LinearLayout linearLayout = findViewById(R.id.session_list);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.removeAllViews();

        String json = sharedPreferences.getString("placeData", "");
        Gson gson = new Gson();
        getUserData = gson.fromJson(json, UserData.class);

        if(getUserData.getFoundSessions() == null) {

        } else {
            for(int i = 0; i < getUserData.getFoundSessions().size();i++) {

                CenterSession centerSession = getUserData.getFoundSessions().get(i);

                if(centerSession.getSessions().size() != 0) {
                    LinearLayout newLinearLayout = new LinearLayout(getApplicationContext());
                    newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, dpToPixel(5));
                    newLinearLayout.setLayoutParams(params);
                    newLinearLayout.setBackgroundColor(Color.parseColor("#e9fbb1"));

                    TextView textView2 = new TextView(newLinearLayout.getContext());
                    textView2.setText(centerSession.getCenter());
                    textView2.setPadding(dpToPixel(10), dpToPixel(10), dpToPixel(10), dpToPixel(10));
                    textView2.setWidth(dpToPixel(120));
                    textView2.setTypeface(regularTypeFace);

                    newLinearLayout.addView(textView2);

                    for(int j = 0;j < centerSession.getSessions().size(); j++) {
                        Session session = centerSession.getSessions().get(j);

                        TextView textView3 = new TextView(newLinearLayout.getContext());
                        textView3.setText("Qty: " + session.getAvailable_capacity() + "\nAge: " + session.getMin_age_limit() + "\nDate: " + session.getDate() + "\nVaccine: " + session.getVaccine());
                        textView3.setPadding(dpToPixel(10), dpToPixel(10), dpToPixel(10), dpToPixel(10));
                        textView3.setWidth(dpToPixel(120));
                        textView3.setTypeface(regularTypeFace);
                        textView3.setBackgroundColor(Color.parseColor("#efffff"));
                        newLinearLayout.addView(textView3);
                    }

                    linearLayout.addView(newLinearLayout);
                }
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        selectedText = findViewById(R.id.selected_text);
        startListening = findViewById(R.id.notify_btn);
        radioGroup = findViewById(R.id.radio_grp);
        boldTypeFace = getResources().getFont(R.font.montserrat_bold);
        lightTypeFace = getResources().getFont(R.font.montserrat_light);
        regularTypeFace = getResources().getFont(R.font.montserrat_regular);
        redirectBtn = findViewById(R.id.redirect_btn);
        soundBtn = findViewById(R.id.sound_btn);

        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences("user-data", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("placeData", "");
        getUserData = gson.fromJson(json, UserData.class);

        redirectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://selfregistration.cowin.gov.in/"));
                startActivity(browserIntent);
            }
        });

        if(getUserData.getFoundSessions() != null) {
            setFoundSlots(sharedPreferences);
        }

        if(CheckSlotService.isRunning) {
            startListening.setText("Stop Notification Service");
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                LinearLayout linearLayout = findViewById(R.id.session_list);
                linearLayout.setVisibility(View.GONE);
                setFoundSlots(sharedPreferences);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                age = radioButton.getText().toString();
            }
        });

        startListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckSlotService.isRunning) {
                    startListening.setText("Turn on notifications");
                    Intent serviceIntent = new Intent(NotifyActivity.this, CheckSlotService.class);
                    stopService(serviceIntent);
                } else {
                    startListening.setText("Stop Notification Service");
                    Intent serviceIntent = new Intent(NotifyActivity.this, CheckSlotService.class);
                    serviceIntent.putExtra("type", getUserData.getPlaceType());
                    serviceIntent.putExtra("pin", getUserData.getPin());
                    serviceIntent.putExtra("district_id", getUserData.getDistrictId());
                    serviceIntent.putExtra("date", getUserData.getDate());
                    serviceIntent.putExtra("age", age);
                    if(getUserData.getPlaceType().equals("PIN")) {
                        serviceIntent.putExtra("inputExtra", "Searching in PIN: " + getUserData.getPin() + ", AGE: " + age);
                    } else {
                        serviceIntent.putExtra("inputExtra", "Searching in DISTRICT: " + getUserData.getDistrictName() + ", AGE: " + age);
                    }

                    startService(serviceIntent);
                }
            }
        });

        if(getUserData.getPlaceType().equals("PIN")) {
            selectedText.setText("Selected PIN: \n\n" + getUserData.getPin());
        } else {
            selectedText.setText("Selected District: \n" + getUserData.getDistrictName() + ", " + getUserData.getStateName());
        }
    }
}