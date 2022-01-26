package com.example.TCCT.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.TCCT.R;
import com.example.TCCT.ViewModels.ChecklistViewModel;
import com.google.android.material.button.MaterialButton;

public class ChecklistListen extends Fragment {

    private static final String INDEX = "index";
    private static Integer position;

    private ChecklistViewModel checklistViewModel;
    private ViewPager2 topicPager;
    private Integer lastTopic = 0;
    private Integer currentPage = 0;
    private Integer score = 0;
    private Integer scoreSum = 0;

    public ChecklistListen() {
        // Required empty public constructor
    }

    public static ChecklistListen newInstance(Integer position) {
        ChecklistListen fragment = new ChecklistListen();
        Bundle args = new Bundle();
        args.putInt(INDEX, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist_listen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnNext = view.findViewById(R.id.btnNext);
        topicPager = view.findViewById(R.id.topicPager);

        checklistViewModel = new ViewModelProvider(requireActivity()).get(ChecklistViewModel.class);

        setTopicPager();
        btnNext.setOnClickListener(view1 -> toNextPage(view));
    }

    private void toNextPage(View view) {
        currentPage++;
        checklistViewModel.getListenScore().observe(requireActivity(), integer -> score = integer);
        scoreSum += score;
        if (currentPage.equals(lastTopic)){
            //To Talk Topic Page
            checklistViewModel.setListenScoreSum(scoreSum);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_checklistListen_to_checklistTalk);
        } else {
            topicPager.setCurrentItem(currentPage);
        }
    }

    private void setTopicPager() {
        checklistViewModel.getTopicListen().observe(requireActivity(), integer -> {
            TopicPagerAdapter topicPagerAdapter = new TopicPagerAdapter(requireActivity(),integer);
            topicPager.setAdapter(topicPagerAdapter);
            topicPager.setUserInputEnabled(false);
            lastTopic = integer;
        });
    }

    private static class TopicPagerAdapter extends FragmentStateAdapter {

        int fragmentSize;

        public TopicPagerAdapter(@NonNull FragmentActivity fragmentActivity, Integer fragmentSize) {
            super(fragmentActivity);
            this.fragmentSize = fragmentSize;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return ChecklistListenTopic.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return fragmentSize;
        }
    }
}