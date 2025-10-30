package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private TextView tvMainName;
    private TextView tvURL;
    private Button btnOpenFirst;
    private Button btnOpenUrl;
    private Button btnOpenGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMainName = findViewById(R.id.tv_mainName);
        tvURL = findViewById(R.id.tvURL);
        btnOpenFirst = findViewById(R.id.btn_open_first);
        btnOpenUrl = findViewById(R.id.btn_open_url);
        btnOpenGps = findViewById(R.id.btn_open_gps);


        tvMainName.setText("Mateusz Tomkiewicz");


        btnOpenFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explicitIntent = new Intent(MainActivity.this, SecondActivity.class);
                explicitIntent.putExtra("EXTRA_NAME", tvMainName.getText().toString());
                startActivity(explicitIntent);
            }
        });


        btnOpenUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlText = tvURL.getText().toString().trim();
                if (!urlText.startsWith("http://") && !urlText.startsWith("https://")) {
                    urlText = "http://" + urlText;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlText));
                startActivity(browserIntent);
            }
        });


        btnOpenGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gpsIntent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(gpsIntent);
            }
        });
    }
}
