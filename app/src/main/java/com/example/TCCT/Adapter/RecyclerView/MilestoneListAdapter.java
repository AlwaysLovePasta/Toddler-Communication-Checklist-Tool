package com.example.TCCT.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TCCT.Activities.MilestoneDetail;
import com.example.TCCT.DataModel.Score;
import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.example.TCCT.Utils.TimeConverter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MilestoneListAdapter extends RecyclerView.Adapter<MilestoneListAdapter.ViewHolder> {

    private final int[][] totalScore = {
            {25, 25}, {25, 25}, {20, 30}, {20, 25},
            {20, 25}, {20, 25}, {20, 25}, {20, 25}, {30, 30}};
    private final int[] monthSpacing = {4, 8, 12, 16, 20, 24, 28, 32, 36};

    private final ArrayList<Score> scoreSet;
    private final Context context;

    private TimeConverter timeConverter;
    private int monthAge;

    public MilestoneListAdapter(Context context, ArrayList<Score> scoreSet) {
        this.context = context;
        this.scoreSet = scoreSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView month, listenScore , talkScore,  desc, level, detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            month = itemView.findViewById(R.id.month);
            listenScore = itemView.findViewById(R.id.listenScore);
            talkScore = itemView.findViewById(R.id.talkScore);
            desc = itemView.findViewById(R.id.desc);
            level = itemView.findViewById(R.id.level);
            detail = itemView.findViewById(R.id.detail);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_home_milestone, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n", "RecyclerView"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseData firebaseData = new FirebaseData(
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),
                FirebaseData.INIT_FIRESTORE);

        Score score = scoreSet.get(position);

        int scoreListen = score.getScoreSet().get(0);
        int scoreTalk = score.getScoreSet().get(1);

        holder.listenScore.setText(scoreListen + " 分");
        holder.talkScore.setText(scoreTalk + " 分");

        firebaseData.getMilestoneIndex(milestoneIndex -> {
            if (milestoneIndex.size() != 0){

                int index = milestoneIndex.get(position);
                holder.month.setText(String.format("%d～%d 個月", index*4, (index+1)*4));

                firebaseData.readToddlerData(context, (imgUri, basicData, detailData) -> {
                    timeConverter = new TimeConverter(basicData.get(1), "yyyy-MM-dd");
                    monthAge = Integer.parseInt(timeConverter.getMonths());

                    if (monthAge >= monthSpacing[index]){
                        if (scoreListen > totalScore[index][0]/2 && scoreListen > totalScore[index][1]/2){
                            holder.level.setText("發展正常");
                        } else {
                            holder.level.setText("疑似遲緩");
                        }
                    }else {
                        if (scoreListen > totalScore[index][0]/2 && scoreListen > totalScore[index][1]/2){
                            holder.level.setText("發展超前");
                        } else {
                            holder.level.setText("發展正常");
                        }
                    }
                });
            }
        });

        firebaseData.readToddlerData(context, (imgUri, basicData, detailData) -> {
            String name = basicData.get(0);
            holder.desc.setText(name + "在這個階段為");
        });

        holder.detail.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(context, MilestoneDetail.class);

            bundle.putInt("POSITION", holder.getAbsoluteAdapterPosition());
            intent.putExtras(bundle);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return scoreSet.size();
    }
}
