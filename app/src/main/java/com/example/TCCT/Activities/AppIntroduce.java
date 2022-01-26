package com.example.TCCT.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.TCCT.Adapter.RecyclerView.FeatureListAdapter;
import com.example.TCCT.R;

public class AppIntroduce extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_introduce);

        View viewBack = findViewById(R.id.viewBack);
        RecyclerView featureList = findViewById(R.id.featureList);

        viewBack.setOnClickListener(view -> finish());
        setFeatureList(featureList);
    }

    private void setFeatureList(RecyclerView featureList) {
        FeatureListAdapter featureListAdapter = new FeatureListAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        featureList.setLayoutManager(layoutManager);
        featureList.setAdapter(featureListAdapter);
    }
}