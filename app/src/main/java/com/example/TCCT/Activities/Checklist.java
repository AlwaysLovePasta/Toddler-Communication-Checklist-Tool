package com.example.TCCT.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.Fragments.ChecklistTalk;
import com.example.TCCT.R;
import com.example.TCCT.ViewModels.ChecklistViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Checklist extends AppCompatActivity implements ChecklistTalk.FragmentListener {

    private final int[][] topicNumber = {{5, 5}, {5, 5}, {4, 6}, {4, 5}, {4 ,5}, {4, 5}, {4 ,5}, {4, 5}, {6 ,6}};

    private FirebaseData firebaseData;
    private ChecklistViewModel checklistViewModel;
    private static int monthIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        firebaseData = new FirebaseData(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseData.INIT_FIRESTORE);
        checklistViewModel= new ViewModelProvider(this).get(ChecklistViewModel.class);
        setTopicContent();
    }

    private void setTopicContent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        monthIndex = bundle.getInt("INDEX");
        checklistViewModel.setMonthIndex(monthIndex);
        checklistViewModel.setTopicListen(topicNumber[monthIndex][0]);
        checklistViewModel.setTopicTalk(topicNumber[monthIndex][1]);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onResultCallback(Integer scoreListen, Integer scoreTalk) {
        ArrayList<Integer> scoreSet = new ArrayList<>();
        scoreSet.add(scoreListen);
        scoreSet.add(scoreTalk);
        firebaseData.updateScore(Checklist.this, monthIndex, scoreSet);
    }
}