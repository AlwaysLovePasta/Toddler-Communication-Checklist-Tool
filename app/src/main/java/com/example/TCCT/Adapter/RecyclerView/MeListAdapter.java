package com.example.TCCT.Adapter.RecyclerView;

import android.app.Activity;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TCCT.Activities.About;
import com.example.TCCT.Activities.AppIntroduce;
import com.example.TCCT.Activities.Feedback;
import com.example.TCCT.Activities.Glossary;
import com.example.TCCT.Activities.Reference;
import com.example.TCCT.Dialogs.SignOutDialog;
import com.example.TCCT.R;
import com.google.firebase.auth.FirebaseAuth;

public class MeListAdapter extends RecyclerView.Adapter<MeListAdapter.ViewHolder> {

    private final static int[] iconList = {
            R.drawable.ic_me_dictionary, R.drawable.ic_me_app,
            R.drawable.ic_me_team, R.drawable.ic_me_feedback,
            R.drawable.ic_me_reference, R.drawable.ic_me_logout};

    private final static String[] title = {"詞彙表參考", "App介紹", "關於我們", "意見回饋", "參考資料", "登出"};

    private final static int itemCount = 6;

    private Activity activity;
    private SignOutDialog signOutDialog;

    public MeListAdapter() {
    }

    public MeListAdapter(Activity activity) {
        this.activity = activity;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View icon, arrow, divider;
        TextView listTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            arrow = itemView.findViewById(R.id.arrow);
            divider = itemView.findViewById(R.id.divider);
            listTitle = itemView.findViewById(R.id.listTitle);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_me_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        signOutDialog = new SignOutDialog(activity);

        holder.icon.setBackground(ContextCompat.getDrawable(activity, iconList[position]));
        holder.listTitle.setText(title[position]);

        holder.arrow.setOnClickListener(view -> {

            switch (holder.getAbsoluteAdapterPosition()) {
                case 0:
                    activity.startActivity(new Intent(activity, Glossary.class));
                    break;
                case 1:
                    activity.startActivity(new Intent(activity, AppIntroduce.class));
                    break;
                case 2:
                    activity.startActivity(new Intent(activity, About.class));
                    break;
                case 3:
                    activity.startActivity(new Intent(activity, Feedback.class));
                    break;
                case 4:
                    activity.startActivity(new Intent(activity, Reference.class));
                    break;
                case 5:
                    signOutDialog.show();
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
