package com.example.TCCT.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.example.TCCT.Activities.Login;
import com.example.TCCT.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class SignOutDialog {

    private Dialog dialog;
    private final Activity activity;

    public SignOutDialog(Activity activity) {
        this.activity = activity;
    }

    public void show() {

        float width = activity.getResources().getDimension(R.dimen._220sdp);

        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_sign_out);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.rectangle_solid_corner_white));
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        dialog.show();

        MaterialButton btnPositive = dialog.findViewById(R.id.btnPositive);
        btnPositive.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(activity, Login.class);
            activity.startActivity(intent);
            FirebaseAuth.getInstance().signOut();
            Log.e("FIRESTORE", "User signed out");
            activity.finish();
        });

        MaterialButton btnNegative = dialog.findViewById(R.id.btnNegative);
        btnNegative.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }
}
