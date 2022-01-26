package com.example.TCCT.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.Dialogs.SpinnerDialog;
import com.example.TCCT.Fragments.AddToddlerStep04;
import com.example.TCCT.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class AddToddler extends AppCompatActivity implements AddToddlerStep04.FragmentListener {

    private FirebaseData firebaseData;
    private SpinnerDialog spinnerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toddler);

        firebaseData = new FirebaseData(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), FirebaseData.INIT_BOTH);
        spinnerDialog = new SpinnerDialog(this, "建立資料中...");
        firebaseData.setSpinnerDialog(spinnerDialog);
    }

    @Override
    public void onResultCallback(Uri imgUri, ArrayList<String> basicData, ArrayList<String> detailData) {
        spinnerDialog.show();
        firebaseData.createDataField(imgUri, basicData, detailData);
        firebaseData.addToddler(this);
    }
}