package com.example.TCCT.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.TCCT.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;

public class SpinnerDialog {

    private Dialog dialog;
    private final Context context;
    private static String title;

    public SpinnerDialog(Context context, String title) {
        this.context = context;
        this.title = title;
    }

    public void show() {
        float boxWidth = context.getResources().getDimension(R.dimen._200sdp);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_loading);
        TextView progressTitle = dialog.findViewById(R.id.progressTitle);
        progressTitle.setText(title);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rectangle_solid_corner_white));
        dialog.getWindow().setLayout((int) boxWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        Sprite sprite = new ChasingDots();
        sprite.setColor(ContextCompat.getColor(context, R.color.lightBlue_200));
        progressBar.setIndeterminateDrawable(sprite);

        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
