package com.example.TCCT.Adapter.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.TCCT.Fragments.MilestoneDetailListen;
import com.example.TCCT.Fragments.MilestoneDetailTalk;

public class MilestoneDetailAdapter extends FragmentStateAdapter {

    final static int fragmentSize = 2;

    public MilestoneDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return MilestoneDetailListen.newInstance(position);
            case 1: return MilestoneDetailTalk.newInstance(position);
            default: return MilestoneDetailListen.newInstance(0);
        }
    }

    @Override
    public int getItemCount() {
        return fragmentSize;
    }
}
