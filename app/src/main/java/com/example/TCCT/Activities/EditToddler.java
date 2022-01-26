package com.example.TCCT.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.Dialogs.SpinnerDialog;
import com.example.TCCT.R;
import com.example.TCCT.Utils.ImageSelector;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class EditToddler extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String DIALOG_TITLE = "保存資料中，請稍候...";

    private ShapeableImageView toddlerImg;
    private View btnImg;
    private TextInputLayout nameInputLayout, dateInputLayout;
    private TextInputEditText edtDate;
    private MaterialAutoCompleteTextView menuGender, menuResident, menuRelationship, menuTime, menuLevel;

    private ImageSelector imageSelector;
    private SpinnerDialog spinnerDialog;
    private FirebaseData firebaseData;

    private String name, birth, gender, resident, relationship, time, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_toddler);
        imageSelector = new ImageSelector(getActivityResultRegistry());
        getLifecycle().addObserver(imageSelector);

        /* View Components*/
        TextView save = findViewById(R.id.save);
        View back = findViewById(R.id.back);
        toddlerImg = findViewById(R.id.toddlerImg);
        btnImg = findViewById(R.id.btnImg);
        nameInputLayout = findViewById(R.id.nameInputLayout);
        dateInputLayout = findViewById(R.id.dateInputLayout);
        edtDate = findViewById(R.id.edtDate);
        menuGender = findViewById(R.id.menuGender);
        menuResident = findViewById(R.id.menuResident);
        menuRelationship = findViewById(R.id.menuRelationship);
        menuTime = findViewById(R.id.menuTime);
        menuLevel = findViewById(R.id.menuLevel);
        /* View Components*/

        String userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        firebaseData = new FirebaseData(userUID, FirebaseData.INIT_BOTH);
        spinnerDialog = new SpinnerDialog(this, DIALOG_TITLE);

        retrieveData();
        getFieldData();

        btnImg.setOnClickListener(view -> imageSelector.selectImage(toddlerImg));
        save.setOnClickListener(view -> {
            spinnerDialog.show();
            updateData();
        });
        back.setOnClickListener(view -> finish());
    }

    private void getFieldData() {
        name = nameInputLayout.getEditText().getText().toString();
        edtDate.setOnClickListener(view -> showDatePickerDialog());
        setMenuGender();
        setMenuResident();
        setMenuRelationship();
        setMenuTime();
        setMenuLevel();
    }

    private void retrieveData() {
        firebaseData.readToddlerData(this, (imgUri, basicData, detailData) -> {

            Glide.with(this)
                    .load(imgUri)
                    .thumbnail(0.1f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(toddlerImg);

            nameInputLayout.getEditText().setText(basicData.get(0));

            dateInputLayout.getEditText().setText(basicData.get(1));
            birth = basicData.get(1);

            menuGender.setText(basicData.get(2), false);
            gender = basicData.get(2);

            menuResident.setText(basicData.get(3), false);
            resident = basicData.get(3);

            menuRelationship.setText(detailData.get(0), false);
            relationship = detailData.get(0);

            menuTime.setText(detailData.get(1), false);
            time = detailData.get(1);

            menuLevel.setText(detailData.get(2), false);
            level = detailData.get(2);
        });
    }

    private void updateData() {
        firebaseData.setSpinnerDialog(spinnerDialog);
        ArrayList<String> basicData = new ArrayList<>();
        ArrayList<String> detailData = new ArrayList<>();
        getFieldData();

        basicData.add(name);
        basicData.add(birth);
        basicData.add(gender);
        basicData.add(resident);

        detailData.add(relationship);
        detailData.add(time);
        detailData.add(level);

        if (imageSelector.getImageUri() != null){
            firebaseData.updateToddlerData(this, imageSelector.getImageUri(), basicData, detailData);
        } else {
            firebaseData.updateToddlerData(this, null, basicData, detailData);
        }
    }

    private void setMenuGender() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_down_menu, this.getResources().getStringArray(R.array.gender));
        menuGender.setAdapter(adapter);
        menuGender.setOnItemClickListener((adapterView, view, i, l) -> gender = adapterView.getItemAtPosition(i).toString());
    }

    private void setMenuResident() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_down_menu, this.getResources().getStringArray(R.array.residents));
        menuResident.setAdapter(adapter);
        menuResident.setOnItemClickListener((adapterView, view, i, l) -> resident = adapterView.getItemAtPosition(i).toString());
    }

    private void setMenuRelationship() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_down_menu, this.getResources().getStringArray(R.array.relationship));
        menuRelationship.setAdapter(adapter);
        menuRelationship.setOnItemClickListener((adapterView, view, i, l) -> relationship = adapterView.getItemAtPosition(i).toString());
    }

    private void setMenuTime() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_down_menu, this.getResources().getStringArray(R.array.time));
        menuTime.setAdapter(adapter);
        menuTime.setOnItemClickListener((adapterView, view, i, l) -> time = adapterView.getItemAtPosition(i).toString());
    }

    private void setMenuLevel() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_down_menu, this.getResources().getStringArray(R.array.level));
        menuLevel.setAdapter(adapter);
        menuLevel.setOnItemClickListener((adapterView, view, i, l) -> level = adapterView.getItemAtPosition(i).toString());
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(new Date().getTime()); //限制只有今天（含）之前的日期可以選擇
        datePicker.show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        birth = String.format("%d-%02d-%02d", year, month+1, dayOfMonth);
        Objects.requireNonNull(dateInputLayout.getEditText()).setText(birth);
    }
}