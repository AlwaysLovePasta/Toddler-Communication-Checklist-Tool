package com.example.TCCT.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class Feedback extends AppCompatActivity {

    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        
        View back = findViewById(R.id.back);
        MaterialButton btnSubmit = findViewById(R.id.btnSubmit);

        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        back.setOnClickListener(view -> finish());
        btnSubmit.setOnClickListener(view -> submitFeedback());
    }

    private void submitFeedback() {
        EditText edtFeedback = findViewById(R.id.edtFeedback);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        String feedbackContent = edtFeedback.getText().toString();
        String feedbackRating = String.valueOf(ratingBar.getRating());

        FirebaseData firebaseData = new FirebaseData(userUID, FirebaseData.INIT_FIRESTORE);
        firebaseData.addFeedback(this, feedbackContent, feedbackRating);

        edtFeedback.getText().clear();
    }
}