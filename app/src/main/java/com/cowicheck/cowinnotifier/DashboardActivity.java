package com.cowicheck.cowinnotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cowicheck.cowinnotifier.API.Centers;
import com.cowicheck.cowinnotifier.API.StateDistrict;
import com.cowicheck.cowinnotifier.Adapters.DistrictAdapter;
import com.cowicheck.cowinnotifier.Adapters.StateAdapter;
import com.cowicheck.cowinnotifier.Models.Center;
import com.cowicheck.cowinnotifier.Models.CenterList;
import com.cowicheck.cowinnotifier.Models.District;
import com.cowicheck.cowinnotifier.Models.DistrictList;
import com.cowicheck.cowinnotifier.Models.Session;
import com.cowicheck.cowinnotifier.Models.State;
import com.cowicheck.cowinnotifier.Models.StateList;
import com.cowicheck.cowinnotifier.Models.UserData;
import com.cowicheck.cowinnotifier.Retrofit.RetrofitClientInstance;
import com.cowicheck.cowinnotifier.Services.CheckSlotService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;

public class DashboardActivity extends AppCompatActivity {

    private Button addPlace, changeDate, closeButton, updateButton, viewSlotsButton, notifyButton;
    private TextView selectedDate, stateText, districtText, infoText, slotInfo, loadingText;
    private RadioGroup radioGroup;
    private TextInputLayout textInputLayout;
    private State[] stateList;
    private District[] districtList;
    private RelativeLayout relativeLayout;
    private Spinner stateSpinner, districtSpinner;
    private Retrofit retrofit;
    private StateDistrict service;
    private Centers centerService;
    private TextInputEditText pinInput;
    private String dateSelected, tempDate;
    private String districtName = "", stateName = "";
    private long districtId = 0;
    private SharedPreferences sharedPreferences;
    private UserData getUserData;
    private Integer pinVal;
    private Typeface boldTypeFace, lightTypeFace, regularTypeFace;
    private String type;
    private int eighteenPlus = 0;
    private int fourtyFivePlus = 0;
    private FloatingActionButton fab;

    boolean isNumeric(String s) {
        try {
          int value = Integer.parseInt(s);
          return true;
        } catch (NumberFormatException e) {

        }
        return false;
    }

    void hideUpdatePlace() {
        LinearLayout linearLayout = findViewById(R.id.update_place);

        if(linearLayout.getVisibility() == GONE) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(GONE);
        }
    }

    void updateInfo(String type, String pin, String date, String district, String state, UserData userData, boolean update) {
        if(type.equals("PIN")) {
            infoText.setTypeface(regularTypeFace);
            infoText.setText("PIN: " + pin + "\n\n" + "DATE: " + date);
        } else {
            infoText.setTypeface(regularTypeFace);
            infoText.setText("DISTRICT: " + district + "\n\nSTATE: " + state +  "\n\nDATE: " + date);
        }
        hideUpdatePlace();
        LinearLayout linearLayout = findViewById(R.id.after_place_added);
        linearLayout.setVisibility(View.VISIBLE);
        addPlace.setText("Edit Place");

        if(update) {
            sharedPreferences = getSharedPreferences("user-data", Context.MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(userData);
            myEdit.putString("placeData", json);
            myEdit.commit();
        }

        updateSlots();
    }

    int dpToPixel(int val) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;

        return (int)(scale * val + 0.5f);
    }

    String makeDateToAPI(String date) {
        String resultDate = "";

        String[] dateArray = date.split(" ");

        resultDate += dateArray[0] + "-";
        resultDate += Month.valueOf(dateArray[1]).getValue() + "-";
        resultDate += dateArray[2];

        return resultDate;
    }

    void setSlotsTable(CenterList centerList) {
        Center[] centers = centerList.getCenters();
        eighteenPlus = 0;
        fourtyFivePlus = 0;

        loadingText.setVisibility(GONE);

        if(centers == null) {
            slotInfo.setText("No centers found !");
        } else {
            Log.i("info", "Response Length: " + centers.length);

            LinearLayout linearLayout = findViewById(R.id.slot_table_header);
            linearLayout.removeAllViews();
            LinearLayout scrollTable = findViewById(R.id.scroll_table);
            scrollTable.removeAllViews();

            TextView textView = new TextView(linearLayout.getContext());
            textView.setText("Centers");
            textView.setPadding(dpToPixel(10), dpToPixel(10), dpToPixel(10), dpToPixel(10));
            textView.setWidth(dpToPixel(120));
            textView.setTypeface(regularTypeFace);
            textView.setBackgroundColor(Color.parseColor("#b1c4fb"));

            linearLayout.addView(textView);

            String[] dateParts = dateSelected.split(" ");
            String monthString;

            if((Month.valueOf(dateParts[1]).getValue() + "").length() == 1) {
                monthString = "0" + Month.valueOf(dateParts[1]).getValue();
            } else {
                monthString = Month.valueOf(dateParts[1]).getValue() + "";
            }

            LocalDate date = LocalDate.parse(dateParts[2] + "-" + monthString + "-" + dateParts[0]);
            ArrayList<String> dateArray = new ArrayList<>();

            for(int i = 0;i < 7; i++) {
                TextView textView2 = new TextView(linearLayout.getContext());
                textView2.setText(date.getDayOfMonth() + "-" + date.getMonthValue() + "-" + date.getYear());

                String tempString;

                if((date.getMonthValue() + "").length() == 1) {
                    tempString = "0" + date.getMonthValue();
                } else {
                    tempString = date.getMonthValue() + "";
                }

                dateArray.add(date.getDayOfMonth() + "-" + tempString + "-" + date.getYear());
                textView2.setPadding(dpToPixel(10), dpToPixel(10), dpToPixel(10), dpToPixel(10));
                textView2.setWidth(dpToPixel(120));
                textView2.setTypeface(regularTypeFace);
                textView2.setBackgroundColor(Color.parseColor("#F5FDCD"));

                linearLayout.addView(textView2);
                date = date.plusDays(1);
            }



            for(int i = 0;i < centers.length; i++) {
                Center center = centers[i];
                Session[] sessions = center.getSessions();
                String centerName = center.getName();

                Map<String, Session[]> map = new TreeMap<>();

                for(int t = 0;t < 7; t++) {
                    map.put(dateArray.get(t), null);
                }

                for(int j = 0;j < sessions.length; j++) {
                    if(map.get(sessions[j].getDate()) != null) {
                        Session[] tempSessionList = {map.get(sessions[j].getDate())[0], sessions[j]};
                        map.put(sessions[j].getDate(), tempSessionList);
                    } else {
                        Session[] tempSessionList = {sessions[j]};
                        map.put(sessions[j].getDate(), tempSessionList);
                    }
                }

                LinearLayout newLinearLayout = new LinearLayout(linearLayout.getContext());
                newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                scrollTable.addView(newLinearLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, dpToPixel(5));
                newLinearLayout.setLayoutParams(params);
                newLinearLayout.setBackgroundColor(Color.parseColor("#e9fbb1"));

                LinearLayout tempLayout = new LinearLayout(newLinearLayout.getContext());
                tempLayout.setOrientation(LinearLayout.VERTICAL);
                TextView textView2 = new TextView(newLinearLayout.getContext());
                textView2.setText(centerName + "\nPIN: " + center.getPincode());
                textView2.setPadding(dpToPixel(10), dpToPixel(10), dpToPixel(10), dpToPixel(10));
                textView2.setWidth(dpToPixel(120));
                textView2.setTypeface(regularTypeFace);

                tempLayout.addView(textView2);
                newLinearLayout.addView(tempLayout);

                LinearLayout tempLinearLayout = new LinearLayout(newLinearLayout.getContext());
                tempLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                tempLinearLayout.setBackgroundColor(Color.parseColor("#efffff"));

                for(Map.Entry<String, Session[]> entry: map.entrySet()) {
                    TextView textView3 = new TextView(newLinearLayout.getContext());

                    String text = "";

                    if(entry.getValue() == null) {
                        text += "NA";

                        textView3.setText(text);
                        textView3.setPadding(dpToPixel(10), dpToPixel(10), dpToPixel(10), dpToPixel(10));
                        textView3.setWidth(dpToPixel(120));
                        textView3.setTypeface(regularTypeFace);

                        tempLinearLayout.addView(textView3);
                    } else {

                        boolean eighteenSlot = false;

                        for(int l = 0;l < entry.getValue().length; l++) {

                            if(entry.getValue()[l].getMin_age_limit() == 45) {
                                if(entry.getValue()[l].getAvailable_capacity() > 0) {
                                    fourtyFivePlus++;
                                }
                            } else {
                                if(entry.getValue()[l].getAvailable_capacity() > 0) {
                                    eighteenPlus++;
                                    eighteenSlot = true;
                                }
                            }

                            if(l == 1) text += "\n";
                            text += "Qty: " + entry.getValue()[l].getAvailable_capacity() + "\nAge: " + entry.getValue()[l].getMin_age_limit() + "+\n" + entry.getValue()[l].getVaccine() + "\nDate: " + entry.getValue()[l].getDate() + "\n";
                        }

                        textView3.setText(text);
                        textView3.setPadding(dpToPixel(10), dpToPixel(10), dpToPixel(10), dpToPixel(10));
                        textView3.setWidth(dpToPixel(120));
                        textView3.setTypeface(regularTypeFace);
                        if(eighteenSlot) textView3.setTextColor(Color.parseColor("blue"));

                        tempLinearLayout.addView(textView3);
                    }
                }

                newLinearLayout.addView(tempLinearLayout);
            }

            slotInfo.setText("Available 18+ Slots: " + eighteenPlus + " and 45+ Slots: " + fourtyFivePlus);
        }
    }

    void updateSlots() {

        LinearLayout linearLayoutSlots = findViewById(R.id.slots);
        linearLayoutSlots.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);

        if(type.equals("PIN")) {
            Call<CenterList> call = centerService.getCentersByPin(pinVal, makeDateToAPI(dateSelected));

            call.enqueue(new Callback<CenterList>() {
                @Override
                public void onResponse(Call<CenterList> call, Response<CenterList> response) {
                    Log.i("info", response.code() + "");
                    if(!response.isSuccessful()) {
                        Snackbar.make(relativeLayout, "Error in fetching slots information.", Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        setSlotsTable(response.body());
                    }
                }

                @Override
                public void onFailure(Call<CenterList> call, Throwable t) {
                    Snackbar.make(relativeLayout, "Network Error. Please turn on your internet.", Snackbar.LENGTH_LONG)
                            .show();
                }
            });
        } else {
            Call<CenterList> call = centerService.getCentersByDistrict(districtId, makeDateToAPI(dateSelected));

            call.enqueue(new Callback<CenterList>() {
                @Override
                public void onResponse(Call<CenterList> call, Response<CenterList> response) {
                    Log.i("info", response.code() + "");
                    if(!response.isSuccessful()) {
                        Snackbar.make(relativeLayout, "Error in fetching slots information.", Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        setSlotsTable(response.body());
                    }
                }

                @Override
                public void onFailure(Call<CenterList> call, Throwable t) {
                    Snackbar.make(relativeLayout, "Network Error. Please turn on your internet.", Snackbar.LENGTH_LONG)
                            .show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPreferences = getSharedPreferences("user-data", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("placeData", "");
        getUserData = gson.fromJson(json, UserData.class);

        infoText = findViewById(R.id.info);
        viewSlotsButton = findViewById(R.id.view_slots_btn);
        notifyButton = findViewById(R.id.notify_btn);
        pinInput = findViewById(R.id.pin_edit);
        addPlace = findViewById(R.id.add_place);
        changeDate = findViewById(R.id.date_change);
        selectedDate = findViewById(R.id.date_text);
        closeButton = findViewById(R.id.close_button);
        radioGroup = findViewById(R.id.radio_grp);
        textInputLayout = findViewById(R.id.pin_input);
        relativeLayout = findViewById(R.id.main_layout);
        stateSpinner = findViewById(R.id.state_menu);
        districtSpinner = findViewById(R.id.district_menu);
        stateText = findViewById(R.id.state_text);
        districtText = findViewById(R.id.district_text);
        updateButton = findViewById(R.id.update_button);
        boldTypeFace = getResources().getFont(R.font.montserrat_bold);
        lightTypeFace = getResources().getFont(R.font.montserrat_light);
        regularTypeFace = getResources().getFont(R.font.montserrat_regular);
        slotInfo = findViewById(R.id.slot_info);
        loadingText = findViewById(R.id.loading_text);
        fab = findViewById(R.id.help_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(DashboardActivity.this)
                        .setTitle("Developer Info")
                        .setMessage("Sukrit Kapil\nBITS Pilani Hyderabad Campus\nBlog: https://sukritkapil2.github.io/\n\nMade with ❤ in Java")
                        .show();
            }
        });

        int nightModeFlags =
                getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                setTheme(android.R.style.Theme_Material_Light_DarkActionBar);
                Log.i("info", "NIGHT MODE YES");
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                Log.i("info", "NIGHT MODE NO");
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                Log.i("info", "NIGHT MODE UNDEFINED");
                break;
        }

        retrofit = RetrofitClientInstance.getRetrofitInstance();
        service = retrofit.create(StateDistrict.class);
        centerService = retrofit.create(Centers.class);

        selectedDate.setText("Date: " + LocalDate.now().getDayOfMonth() + " " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear());
        dateSelected = LocalDate.now().getDayOfMonth() + " " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear();

        if(getUserData != null) {
            type = getUserData.getPlaceType();
            pinVal = getUserData.getPin();
            districtId = getUserData.getDistrictId();
            districtName = getUserData.getDistrictName();
            stateName = getUserData.getStateName();
            dateSelected  = getUserData.getDate();

            hideUpdatePlace();
            LinearLayout linearLayout = findViewById(R.id.after_place_added);
            linearLayout.setVisibility(View.VISIBLE);
            addPlace.setText("Edit Place");
            updateInfo(type, pinVal.toString(), dateSelected, districtName, stateName, getUserData, false);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(DashboardActivity.this);

        if(CheckSlotService.isRunning) {
            addPlace.setText("Listening to Updates");
            notifyButton.setVisibility(GONE);
            View view = findViewById(R.id.empty_view);
            view.setVisibility(GONE);
            addPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, NotifyActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            View view = findViewById(R.id.empty_view);
            addPlace.setText("Edit Place");
            view.setVisibility(View.VISIBLE);
            notifyButton.setVisibility(View.VISIBLE);
            addPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideUpdatePlace();
                    LinearLayout slots = findViewById(R.id.slots);
                    slots.setVisibility(GONE);
                }
            });
        }

        viewSlotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSlots();
            }
        });

        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, NotifyActivity.class);
                startActivity(intent);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());

                if(selectedRadioButton.getText().equals("PIN")) {
                    String pinEntered = pinInput.getText().toString();

                    if(pinEntered.length() == 0) {
                        Snackbar.make(relativeLayout, "Please enter a PIN.", Snackbar.LENGTH_LONG)
                                .show();
                    } else if(pinEntered.length() != 6) {
                        Snackbar.make(relativeLayout, "PIN should be of 6 digits.", Snackbar.LENGTH_LONG)
                                .show();
                    } else if(!isNumeric(pinEntered)) {
                        Snackbar.make(relativeLayout, "PIN should be numeric.", Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        pinVal = Integer.parseInt(pinEntered);
                        if(tempDate != null) dateSelected = tempDate;
                        else dateSelected = LocalDate.now().getDayOfMonth() + " " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear();
                        type = "PIN";
                        UserData userData;
                        if(getUserData != null) userData = new UserData("PIN", "", "", 0, Integer.parseInt(pinEntered), dateSelected, getUserData.getFoundSessions());
                        else userData = new UserData("PIN", "", "", 0, Integer.parseInt(pinEntered), dateSelected, null);
                        updateInfo("PIN", pinEntered, dateSelected, "", "", userData, true);
                    }
                } else {
                    if(stateName.equals("") || districtName.equals("")) {
                        Snackbar.make(relativeLayout, "Please select a state and district from the dropdown", Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        if(tempDate != null) dateSelected = tempDate;
                        else dateSelected = LocalDate.now().getDayOfMonth() + " " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear();
                        type = "DISTRICT";
                        UserData userData;
                        if(getUserData != null) userData = new UserData("DISTRICT", districtName, stateName, districtId, 0, dateSelected, getUserData.getFoundSessions());
                        else userData = new UserData("DISTRICT", districtName, stateName, districtId, 0, dateSelected, null);
                        updateInfo("DISTRICT", "", dateSelected, districtName, stateName, userData, true);
                    }
                }
            }
        });

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = findViewById(R.id.update_place);

                linearLayout.setVisibility(GONE);
            }
        });

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tempDate = dayOfMonth + " " + Month.of(month+1) + " " + year;
                selectedDate.setText("Date: " + tempDate);
            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //fetch district list and populate the district spinner
                stateName = stateList[position].getState_name();
                Call<DistrictList> call = service.getDistricts(id);

                call.enqueue(new Callback<DistrictList>() {
                    @Override
                    public void onResponse(Call<DistrictList> call, Response<DistrictList> response) {
                        if(!response.isSuccessful()) {
                            Snackbar.make(relativeLayout, "Error in fetching district list. Please retry after some time.", Snackbar.LENGTH_LONG)
                                    .show();
                        } else {
                            districtList = response.body().getDistricts();

                            DistrictAdapter districtAdapter = new DistrictAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, districtList);
                            districtSpinner.setAdapter(districtAdapter);
                            districtSpinner.setSelection(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<DistrictList> call, Throwable t) {
                        Snackbar.make(relativeLayout, "Network Error. Please turn on your internet.", Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtName = districtList[position].getDistrict_name();
                districtId = districtList[position].getDistrict_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);

                if(radioButton.getText().equals("PIN")) {
                    textInputLayout.setVisibility(View.VISIBLE);
                    stateText.setVisibility(GONE);
                    districtText.setVisibility(GONE);
                    stateSpinner.setVisibility(GONE);
                    districtSpinner.setVisibility(GONE);
                } else {
                    textInputLayout.setVisibility(GONE);
                    stateText.setVisibility(View.VISIBLE);
                    stateSpinner.setVisibility(View.VISIBLE);
                    districtText.setVisibility(View.VISIBLE);
                    districtSpinner.setVisibility(View.VISIBLE);

                    //fetch the state and district lists
                    Call<StateList> call = service.getStates();

                    call.enqueue(new Callback<StateList>() {
                        @Override
                        public void onResponse(Call<StateList> call, Response<StateList> response) {
                            if(!response.isSuccessful()) {
                                Snackbar.make(relativeLayout, "Error in fetching state list. Please retry after some time.", Snackbar.LENGTH_LONG)
                                        .show();
                            } else {
                                stateList = response.body().getStates();

                                StateAdapter stateAdapter = new StateAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, stateList);
                                stateSpinner.setAdapter(stateAdapter);
                                stateSpinner.setSelection(0);
                            }
                        }

                        @Override
                        public void onFailure(Call<StateList> call, Throwable t) {
                            Snackbar.make(relativeLayout, "Network Error. Please turn on your internet.", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CheckSlotService.isRunning) {
            addPlace.setText("Listening to Updates");
            View view = findViewById(R.id.empty_view);
            notifyButton.setVisibility(GONE);
            view.setVisibility(GONE);
            addPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, NotifyActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            addPlace.setText("Edit Place");
            notifyButton.setVisibility(View.VISIBLE);
            View view = findViewById(R.id.empty_view);
            view.setVisibility(View.VISIBLE);
            addPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideUpdatePlace();
                    LinearLayout slots = findViewById(R.id.slots);
                    slots.setVisibility(GONE);
                }
            });
        }
    }
}