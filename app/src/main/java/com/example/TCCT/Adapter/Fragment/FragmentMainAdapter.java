package com.example.TCCT.Adapter.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.TCCT.Fragments.Chatbot;
import com.example.TCCT.Fragments.Home;
import com.example.TCCT.Fragments.Settings;

public class FragmentMainAdapter extends FragmentStateAdapter {

    final int fragmentSize = 3;

    public FragmentMainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return Home.newInstance(position);
            case 1: return Chatbot.newInstance(position);
            case 2: return Settings.newInstance(position);
            default: return Home.newInstance(0);
        }
    }

    @Override
    public int getItemCount() {
        return fragmentSize;
    }
}
