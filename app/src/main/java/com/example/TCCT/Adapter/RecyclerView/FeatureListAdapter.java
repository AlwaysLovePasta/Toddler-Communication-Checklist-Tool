package com.example.TCCT.Adapter.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TCCT.R;


public class FeatureListAdapter extends RecyclerView.Adapter<FeatureListAdapter.ViewHolder> {

    static final int itemCount = 5;
    static final int[] icon = {
            R.drawable.ic_feature_test, R.drawable.ic_feature_grow,
            R.drawable.ic_feature_milestone, R.drawable.ic_feature_chatbot, R.drawable.ic_feature_others};

    private Context context;

    public FeatureListAdapter() {
    }

    public FeatureListAdapter(Context context) {
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView featureTitle, featureDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            featureTitle = itemView.findViewById(R.id.featureTitle);
            featureDesc = itemView.findViewById(R.id.featureDesc);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_feature_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] title = context.getResources().getStringArray(R.array.featureTitle);
        String[] desc = context.getResources().getStringArray(R.array.featureDesc);

        holder.featureTitle.setText(title[position]);
        holder.featureTitle.setCompoundDrawablesWithIntrinsicBounds(icon[position], 0, 0, 0);
        holder.featureDesc.setText(desc[position]);
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
