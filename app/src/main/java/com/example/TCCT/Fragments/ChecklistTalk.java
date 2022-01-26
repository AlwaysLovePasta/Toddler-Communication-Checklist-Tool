package com.example.TCCT.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.TCCT.Activities.MainActivity;
import com.example.TCCT.R;
import com.example.TCCT.ViewModels.ChecklistViewModel;
import com.google.android.material.button.MaterialButton;

public class ChecklistTalk extends Fragment {

    private static final String INDEX = "index";
    private static Integer position;

    private ChecklistViewModel checklistViewModel;
    private ViewPager2 topicPager;
    private MaterialButton btnNext;
    private Integer lastTopic = 0;
    private Integer currentPage = 0;
    private Integer score = 0;
    private Integer scoreSum = 0;

    FragmentListener fragmentListener;

    public interface FragmentListener {
        void onResultCallback(Integer scoreListen, Integer scoreTalk);
    }

    public ChecklistTalk() {
        // Required empty public constructor
    }

    public static ChecklistTalk newInstance(Integer position) {
        ChecklistTalk fragment = new ChecklistTalk();
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
        return inflater.inflate(R.layout.fragment_checklist_talk, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentListener = (FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentCallback");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnNext = view.findViewById(R.id.btnNext);
        topicPager = view.findViewById(R.id.topicPager);

        checklistViewModel = new ViewModelProvider(requireActivity()).get(ChecklistViewModel.class);

        setTopicPager();
        btnNext.setOnClickListener(view1 -> toNextPage());
    }

    private void testDoneDialog(Dialog dialog){
        float width = requireActivity().getResources().getDimension(R.dimen._220sdp);

        dialog.setContentView(R.layout.dialog_test_done);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.rectangle_solid_corner_white));
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        MaterialButton btnPositive = dialog.findViewById(R.id.btnPositive);
        btnPositive.setOnClickListener(v -> {
            checklistViewModel.getListenScoreSum().observe(requireActivity(), integer -> fragmentListener.onResultCallback(integer, scoreSum));
            requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
            dialog.dismiss();
        });
    }

    private void toNextPage() {
        currentPage++;
        checklistViewModel.getTalkScore().observe(requireActivity(), integer -> score = integer);
        scoreSum += score;
        if (currentPage.equals(lastTopic)) {
            Dialog dialog = new Dialog(requireActivity());
            testDoneDialog(dialog);
            dialog.show();
        }
        if (currentPage.equals(lastTopic - 1)) {
            btnNext.setText("完成");
        }
        if (currentPage < lastTopic) {
            topicPager.setCurrentItem(currentPage);
        }
    }

    private void setTopicPager() {
        checklistViewModel.getTopicTalk().observe(requireActivity(), integer -> {
            TopicPagerAdapter topicPagerAdapter = new TopicPagerAdapter(requireActivity(), integer);
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
            return ChecklistTalkTopic.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return fragmentSize;
        }
    }
}