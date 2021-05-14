package com.cowicheck.cowinnotifier.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cowicheck.cowinnotifier.API.Centers;
import com.cowicheck.cowinnotifier.API.StateDistrict;
import com.cowicheck.cowinnotifier.Models.Center;
import com.cowicheck.cowinnotifier.Models.CenterList;
import com.cowicheck.cowinnotifier.Models.CenterSession;
import com.cowicheck.cowinnotifier.Models.Session;
import com.cowicheck.cowinnotifier.Models.UserData;
import com.cowicheck.cowinnotifier.NotifyActivity;
import com.cowicheck.cowinnotifier.R;
import com.cowicheck.cowinnotifier.Retrofit.RetrofitClientInstance;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CheckSlotService extends Service {

    public static final String CHANNEL_ID = "CheckSlotServiceChannel";
    public static final String CHANNEL_ID_2 = "SlotFoundServiceChannel";
    public static boolean isRunning = false;
    public static boolean slotFound= false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ArrayList<CenterSession> foundSessions = new ArrayList<>();
    private int total_slots = 0;
    private int total_centers = 0;

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void createNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID_2,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_SHORT);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.i("info", "Check Message");
        }
    };

    void checkSlots(CenterList centerList, String age) {
        Center[] centers = centerList.getCenters();

        for(int i = 0;i < centers.length; i++) {
            Center center = centers[i];
            CenterSession centerSession = new CenterSession(new ArrayList<Session>(), center.getName());
            Session[] sessions = center.getSessions();

            boolean tempCenterValid = false;

            for(int j = 0;j < sessions.length; j++) {
                Session session = sessions[j];

                if(session.getAvailable_capacity() > 0) {
                    if(age.equals("45 +") && session.getMin_age_limit() == 45) {
                        slotFound = true;
                        tempCenterValid = true;
                        total_slots += 1;
                        Log.i("info", "SLOT FOUND!");
                        centerSession.sessions.add(session);
                    }
                    else if(age.equals("18 +") && session.getMin_age_limit() == 18) {
                        slotFound = true;
                        tempCenterValid = true;
                        total_slots += 1;
                        Log.i("info", "SLOT FOUND!");
                        centerSession.sessions.add(session);
                    } else if(age.equals("Any Age")) {
                        slotFound = true;
                        tempCenterValid = true;
                        total_slots += 1;
                        Log.i("info", "SLOT FOUND!");
                        centerSession.sessions.add(session);
                    }
                }
            }

            if(tempCenterValid) total_centers++;
            foundSessions.add(centerSession);
        }
        Log.i("info", "No Slots Found. Fetching Again");
        return;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, NotifyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Cowin Notifier is finding slots")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launcher_cowin_notifier)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
                Centers centerService = retrofit.create(Centers.class);

                //Background work here
                if(!isRunning) {
                    Log.i("info", "starting session");
                    isRunning = true;

                    while(!slotFound && isRunning) {
                        if(intent.getStringExtra("type").equals("PIN")) {

                            centerService.getCentersByPin(intent.getIntExtra("pin", 0), "" + LocalDate.now().getDayOfMonth() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getYear()).enqueue(new Callback<CenterList>() {
                                @Override
                                public void onResponse(Call<CenterList> call, Response<CenterList> response) {
                                    Log.i("info", response.code() + "");
                                    if(!response.isSuccessful()) {

                                    } else {
                                        checkSlots(response.body(), intent.getStringExtra("age"));
                                    }
                                }

                                @Override
                                public void onFailure(Call<CenterList> call, Throwable t) {

                                }
                            });
                        } else {

                            centerService.getCentersByDistrict(intent.getLongExtra("district_id", 1), "" + LocalDate.now().getDayOfMonth() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getYear()).clone().enqueue(new Callback<CenterList>() {
                                @Override
                                public void onResponse(Call<CenterList> call, Response<CenterList> response) {
                                    Log.i("info", response.code() + "");
                                    if(!response.isSuccessful()) {

                                    } else {
                                        checkSlots(response.body(), intent.getStringExtra("age"));
                                    }
                                }

                                @Override
                                public void onFailure(Call<CenterList> call, Throwable t) {

                                }
                            });
                        }
                        try {
                            Thread.sleep(10000);
                        }
                        catch (InterruptedException e) {

                        }
                    }
                    Log.i("INFO", "OUT OF LOOP");
                    if(slotFound) {
                        slotFound = false;

                        SharedPreferences sharedPreferences = getSharedPreferences("user-data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString("placeData", "");
                        UserData getUserData = gson.fromJson(json, UserData.class);

                        UserData newUserData = new UserData(getUserData.getPlaceType(), getUserData.getDistrictName(), getUserData.getStateName(), getUserData.getDistrictId(), getUserData.getPin(), getUserData.getDate(), foundSessions);

                        String json2 = gson.toJson(newUserData);
                        myEdit.putString("placeData", json2);
                        myEdit.commit();

                        createNotificationChannel2();

                        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        String notificationDesc = "";

                        if(getUserData.getPlaceType().equals("PIN")) {
                            notificationDesc += "A total of " + total_slots + " vaccination slots has been found near you for the PIN: " + intent.getIntExtra("pin", 0);
                        } else {
                            notificationDesc += "A total of " + total_slots + " vaccination slots has been found near you for the DISTRICT: " + getUserData.getDistrictName();
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID_2)
                                .setSmallIcon(R.mipmap.ic_launcher_cowin_notifier2)
                                .setContentTitle(total_centers + " centers found for age: " + intent.getStringExtra("age"))
                                .setContentText(notificationDesc)
                                .setContentIntent(pendingIntent)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setSound(soundUri);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(2, builder.build());
                    }
                }
            }
        });

        return START_NOT_STICKY;
    }
}
