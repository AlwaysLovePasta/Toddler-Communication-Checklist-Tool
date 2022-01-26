package com.example.TCCT.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.TCCT.R;
import com.example.TCCT.ViewModels.ChecklistViewModel;

public class ChecklistTalkTopic extends Fragment {

    private final static String INDEX = "index";
    private Integer position;

    private final static String[] chineseNumber = {"", "", "", "", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    private int monthIndex = 0;
    private int optionScore = 0;

    private ChecklistViewModel checklistViewModel;

    public ChecklistTalkTopic() {
        // Required empty public constructor
    }

    public static ChecklistTalkTopic newInstance(Integer position) {
        ChecklistTalkTopic fragment = new ChecklistTalkTopic();
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
        return inflater.inflate(R.layout.fragment_checklist_talk_topic, container, false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTopicContent(view);
        getScore(view);
    }

    @SuppressLint("DefaultLocale")
    private void setTopicContent(View view) {
        TextView topicNumber = view.findViewById(R.id.topicNumber);
        TextView topicDescription = view.findViewById(R.id.topicDescription);
        checklistViewModel = new ViewModelProvider(requireActivity()).get(ChecklistViewModel.class);
        checklistViewModel.getMonthIndex().observe(requireActivity(), integer -> monthIndex = integer);
        checklistViewModel.getTopicListen().observe(requireActivity(), integer -> topicNumber.setText(String.format("第%s題", chineseNumber[integer + position])));
        String resName = String.format("topic%d_talk%d", monthIndex, position + 1);
        int stringID = getResources().getIdentifier(resName, "string", requireActivity().getPackageName());
        topicDescription.setText(stringID);
    }

    @SuppressLint("NonConstantResourceId")
    private void getScore(View view){
        RadioGroup optionGroup = view.findViewById(R.id.optionGroup);
        optionGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.option01:
                    optionScore = 5;
                    break;
                case R.id.option02:
                    optionScore = 4;
                    break;
                case R.id.option03:
                    optionScore = 3;
                    break;
                case R.id.option04:
                    optionScore = 2;
                    break;
                case R.id.option05:
                    optionScore = 1;
                    break;
                case -1:
                    optionScore = 0;
                    break;
            }
            checklistViewModel.setTalkScore(optionScore);
        });
        optionGroup.check(-1);
    }
}