package com.example.TCCT.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.TCCT.R;

public class MonthMenu extends AppCompatActivity {

    RecyclerView monthMenu;
    MonthMenuAdapter monthMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        monthMenu = findViewById(R.id.monthMenu);
        setMonthMenu(monthMenu);
    }

    private void setMonthMenu(RecyclerView monthMenu) {
        monthMenuAdapter = new MonthMenuAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        monthMenu.setLayoutManager(linearLayoutManager);
        monthMenu.setAdapter(monthMenuAdapter);
    }

    private static class MonthMenuAdapter extends RecyclerView.Adapter<MonthMenuAdapter.ViewHolder>{

        static final int[] icons = {
                R.drawable.ic_pack_baby_crib, R.drawable.ic_pack_baby_pacifier, R.drawable.ic_pack_baby_diape,
                R.drawable.ic_pack_baby_rattle, R.drawable.ic_pack_baby_feeding_bottle, R.drawable.ic_pack_baby_toy,
                R.drawable.ic_pack_baby_dress, R.drawable.ic_pack_baby_safety_pin, R.drawable.ic_pack_baby_stroller };
        static final int[] numbers = {10, 10, 10, 9, 9, 9, 9, 9, 12};
        static final int itemCount = 9;

        Context context;
        Activity activity;

        public MonthMenuAdapter(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            View icon, arrow, divider;
            TextView month, number;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.icon);
                arrow = itemView.findViewById(R.id.arrow);
                divider = itemView.findViewById(R.id.divider);
                month= itemView.findViewById(R.id.month);
                number = itemView.findViewById(R.id.number);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_checklist_month, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.icon.setBackground(ContextCompat.getDrawable(context, icons[position]));
            holder.month.setText(String.format("%d～%d 個月", position*4, (position+1)*4));
            holder.number.setText(String.format("共 %d 題", numbers[position]));

            if (position == itemCount - 1){
                holder.divider.setVisibility(View.INVISIBLE);
            }

            holder.arrow.setOnClickListener(view -> {
                Intent intent = new Intent(activity, Checklist.class);
                Bundle bundle = new Bundle();
                bundle.putInt("INDEX", holder.getAbsoluteAdapterPosition());
                intent.putExtras(bundle);
                activity.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }
    }
}