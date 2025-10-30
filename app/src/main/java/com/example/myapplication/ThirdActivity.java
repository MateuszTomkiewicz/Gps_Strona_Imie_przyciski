package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ThirdActivity extends AppCompatActivity {

    private TextView tvGpsStatus;
    private TextView tvCoordinates;
    private Button btnStartGps;
    private Button btnStopGps;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private final String PROVIDER = LocationManager.GPS_PROVIDER;


    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        tvGpsStatus = findViewById(R.id.tvGpsStatus);
        tvCoordinates = findViewById(R.id.tvCoordinates);
        btnStartGps = findViewById(R.id.btnStartGps);
        btnStopGps = findViewById(R.id.btnStopGps);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double lat = location.getLatitude();
                double lon = location.getLongitude();
                tvCoordinates.setText(String.format("Szerokość: %.6f, Długość: %.6f", lat, lon));
            }

            @Override public void onProviderDisabled(String provider) {}
            @Override public void onProviderEnabled(String provider) {}
            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
        };


        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fine = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarse = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                    if (fine || coarse) {
                        startGpsListening();
                    } else {
                        tvGpsStatus.setText("Brak uprawnień do lokalizacji");
                    }
                }
        );


        btnStartGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnStartGps.setEnabled(false);
                btnStopGps.setEnabled(true);

                if (hasLocationPermission()) {
                    startGpsListening();
                } else {
                    requestPermissionLauncher.launch(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    });
                }
            }
        });

        btnStopGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopGpsListening();
                btnStartGps.setEnabled(true);
                btnStopGps.setEnabled(false);
            }
        });

    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void startGpsListening() {
        try {

            if (hasLocationPermission()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(PROVIDER, 2000, 1, locationListener);
                tvGpsStatus.setText("Status GPS: AKTYWNY (nasłuchiwanie)");
            } else {
                tvGpsStatus.setText("Brak uprawnień - nie można uruchomić GPS");
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvGpsStatus.setText("Błąd przy uruchamianiu GPS: " + e.getMessage());
        }
    }

    private void stopGpsListening() {
        try {
            locationManager.removeUpdates(locationListener);
            tvGpsStatus.setText("Status GPS: ZATRZYMANY");
        } catch (Exception e) {
            e.printStackTrace();
            tvGpsStatus.setText("Błąd przy zatrzymaniu GPS: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            locationManager.removeUpdates(locationListener);
        } catch (Exception ignored) {}
    }
}
