package com.cowicheck.cowinnotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cowicheck.cowinnotifier.API.StateDistrict;
import com.cowicheck.cowinnotifier.Adapters.StateAdapter;
import com.cowicheck.cowinnotifier.Models.State;
import com.cowicheck.cowinnotifier.Models.StateList;
import com.cowicheck.cowinnotifier.Retrofit.RetrofitClientInstance;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private Button addPlace, changeDate, closeButton;
    private TextView selectedDate;
    private RadioGroup radioGroup;
    private TextInputLayout textInputLayout;
    private State[] stateList;
    private RelativeLayout relativeLayout;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        addPlace = findViewById(R.id.add_place);
        changeDate = findViewById(R.id.date_change);
        selectedDate = findViewById(R.id.date_text);
        closeButton = findViewById(R.id.close_button);
        radioGroup = findViewById(R.id.radio_grp);
        textInputLayout = findViewById(R.id.pin_input);
        relativeLayout = findViewById(R.id.main_layout);
        spinner = findViewById(R.id.state_menu);

        selectedDate.setText("Date: " + LocalDate.now().getDayOfMonth() + " " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear());

        DatePickerDialog datePickerDialog = new DatePickerDialog(DashboardActivity.this);

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = findViewById(R.id.update_place);

                if(linearLayout.getVisibility() == View.GONE) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.GONE);
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

                linearLayout.setVisibility(View.GONE);
            }
        });

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate.setText("Date: " + dayOfMonth + " " + Month.of(month+1) + " " + year);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //fetch district list and populate the district spinner
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
                } else {
                    textInputLayout.setVisibility(View.GONE);

                    //fetch the state and district lists
                    StateDistrict service = RetrofitClientInstance.getRetrofitInstance().create(StateDistrict.class);
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
                                spinner.setAdapter(stateAdapter);
                                spinner.setSelection(0);
                            }
                        }

                        @Override
                        public void onFailure(Call<StateList> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
}