package com.example.sercices;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.Permission;
import java.security.Permissions;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static com.example.sercices.MyForegroundService.IS_SERVICE_ACTIVE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int REQUEST_CODE = 1001;

    String[] permissions = new String[2];
    private Button btnStop, btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;


        if (ContextCompat.checkSelfPermission(this, permissions[0])
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissions[1])
                        == PackageManager.PERMISSION_GRANTED) {


            getLastLocation();

        } else {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_CODE);
            }
        }


        long unix = 1579273850 * 1_000L;


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:MM:SS");

        Date dt = new Date(unix);
        btnStart.setText(dateFormat.format(dt));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            for (int results : grantResults) {
                if (results == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                }
            }
        }
    }

    void getLastLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null)
                            btnStart.setText(String.valueOf(location.getLatitude()));
                    }
                });
    }

    private void initListeners() {
        btnStop.setOnClickListener(this);
        btnStart.setOnClickListener(this);
    }

    private void initViews() {
        btnStop = findViewById(R.id.btnStop);
        btnStart = findViewById(R.id.btnStart);
    }

    private void actionService(boolean isActivated) {
        Intent intent = new Intent(this, MyForegroundService.class);
        intent.putExtra(IS_SERVICE_ACTIVE, isActivated);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                actionService(true);
                break;
            case R.id.btnStop:
                actionService(false);
                break;
        }
    }
}
