package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class SecondActivity extends AppCompatActivity {

    private TextView tvSecondName;
    private TextView tvSecondInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tvSecondName = findViewById(R.id.tvSecondName);
        tvSecondInfo = findViewById(R.id.tvSecondInfo);

        String name = getIntent().getStringExtra("EXTRA_NAME");
        if (name == null || name.isEmpty()) {
            name = "Brak imienia";
        }
        tvSecondName.setText(name);
        tvSecondInfo.setText("Kliknij przycisk back by wrócić do poprzedniego ekranu");
    }
}
