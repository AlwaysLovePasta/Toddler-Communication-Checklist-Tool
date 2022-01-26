package com.example.TCCT.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.TCCT.R;

public class Reference extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);

        View viewBack = findViewById(R.id.viewBack);

        viewBack.setOnClickListener(view -> finish());
    }
}