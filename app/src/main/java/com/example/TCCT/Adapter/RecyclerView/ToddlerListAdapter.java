package com.example.TCCT.Adapter.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.TCCT.Activities.MainActivity;
import com.example.TCCT.DataModel.Toddler;
import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class ToddlerListAdapter extends RecyclerView.Adapter<ToddlerListAdapter.ViewHolder> {

    private ArrayList<Toddler> toddlerList;
    private Context context;
    private Activity activity;
    Integer layoutIndex;

    public ToddlerListAdapter() {
    }

    public ToddlerListAdapter(Context context, ArrayList<Toddler> toddlerList) {
        this.context = context;
        this.toddlerList = toddlerList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        ShapeableImageView toddlerImg;
        TextView toddlerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            toddlerImg = itemView.findViewById(R.id.toddlerImg);
            toddlerName = itemView.findViewById(R.id.toddlerName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_toddler_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseData firebaseData = new FirebaseData(
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),
                FirebaseData.INIT_FIRESTORE);
        Toddler toddler = toddlerList.get(position);

        /* Set View Components */
        Glide.with(context)
                .load(toddler.getImgUri())
                .thumbnail(0.1f)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.toddlerImg);

        holder.toddlerName.setText(toddler.getBasicData().get(0));

        firebaseData.getCurrentToddler(currentPosition -> {
            holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_200));
            layoutIndex = currentPosition;
            //偵測當前點擊的item位置
            holder.layout.setOnClickListener(view -> {
                layoutIndex = holder.getAbsoluteAdapterPosition();
                firebaseData.updateCurrentToddler(holder.getAbsoluteAdapterPosition());
                notifyDataSetChanged();
                refreshActivity();
            });
            if (layoutIndex == position) {
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_200));
            } else {
                holder.layout.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        /* Set View Components */
    }

    public void refreshActivity(){
        Intent intent = activity.getIntent();
        activity.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(intent);
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return toddlerList.size();
    }
}
