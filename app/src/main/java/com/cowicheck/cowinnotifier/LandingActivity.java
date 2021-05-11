package com.cowicheck.cowinnotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {

    private Button enterButton;
    private SharedPreferences sharedPreferences;
    private Boolean firstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        enterButton = findViewById(R.id.enter_button);

        sharedPreferences = getSharedPreferences("enter-info", Context.MODE_PRIVATE);
        firstTime = sharedPreferences.getBoolean("first-time", true);

        if(!firstTime) {
            Intent intent = new Intent(LandingActivity.this, DashboardActivity.class);
            startActivity(intent);
        }

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putBoolean("first-time", false);
                myEdit.commit();

                Intent intent = new Intent(LandingActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }
}