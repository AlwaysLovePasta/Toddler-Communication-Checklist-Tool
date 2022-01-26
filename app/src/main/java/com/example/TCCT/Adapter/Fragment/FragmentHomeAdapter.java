package com.example.TCCT.Adapter.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.TCCT.Fragments.HomeChart;
import com.example.TCCT.Fragments.HomeMilestone;

public class FragmentHomeAdapter extends FragmentStateAdapter {

    private static final int fragmentSize = 2;

    public FragmentHomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return HomeChart.newInstance(position);
            case 1: return HomeMilestone.newInstance(position);
            default: return HomeChart.newInstance(0);
        }
    }

    @Override
    public int getItemCount() {
        return fragmentSize;
    }
}
