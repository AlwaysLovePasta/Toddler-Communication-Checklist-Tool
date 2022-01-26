package com.example.TCCT.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.example.TCCT.Utils.TimeConverter;
import com.example.TCCT.ViewModels.MilestoneDetailViewModel;
import com.google.firebase.auth.FirebaseAuth;

import at.grabner.circleprogress.CircleProgressView;

public class MilestoneDetailTalk extends Fragment {

    private static final String INDEX = "index";
    private static Integer position;
    private final int[] totalScoreList = {25, 25, 30, 25, 25, 25, 25, 25, 30};
    private final int[] monthSpacing = {4, 8, 12, 16, 20, 24, 28, 32, 36};

    private TimeConverter timeConverter;
    private FirebaseData firebaseData;

    private CircleProgressView progressView;
    private TextView score, totalScore, description;

    private Integer monthAge;

    public MilestoneDetailTalk() {
        // Required empty public constructor
    }

    public static MilestoneDetailTalk newInstance(Integer position) {
        MilestoneDetailTalk fragment = new MilestoneDetailTalk();
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
        return inflater.inflate(R.layout.fragment_milestone_detail_talk, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressView = view.findViewById(R.id.progressView);
        score = view.findViewById(R.id.score);
        totalScore = view.findViewById(R.id.totalScore);
        description = view.findViewById(R.id.description);

        firebaseData = new FirebaseData(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseData.INIT_FIRESTORE);

        getMilestoneIndex();
    }

    private void getMilestoneIndex() {
        MilestoneDetailViewModel viewModel = new ViewModelProvider(requireActivity()).get(MilestoneDetailViewModel.class);
        viewModel.getMilestoneIndex().observe(requireActivity(), integer -> {
            Log.e("Detail Fragment Page", "Index => " + integer);
            setScore(integer);
        });
    }

    private void setProgressView(String valueFormat) {
        progressView.setValueAnimated(0, Float.parseFloat(valueFormat), 2000);
    }

    @SuppressLint("DefaultLocale")
    private void setScore(Integer milestoneIndex) {
        firebaseData.readMilestoneScore(requireContext(), milestoneIndex, scoreSet -> {
            score.setText(String.valueOf(scoreSet.get(1)));
            String percentage = String.format("%.0f", (((Number) scoreSet.get(1)).floatValue() / totalScoreList[milestoneIndex]) * 100);
            setProgressView(percentage);
            setDescription(milestoneIndex, percentage);
        });
        totalScore.setText(String.valueOf(totalScoreList[milestoneIndex]));
    }

    @SuppressLint("SetTextI18n")
    private void setDescription(Integer milestoneIndex, String percentage) {
        firebaseData.readToddlerData(requireContext(), (imgUri, basicData, detailData) -> {
            timeConverter = new TimeConverter(basicData.get(1), "yyyy-MM-dd");
            monthAge = Integer.parseInt(timeConverter.getMonths());

            if (monthAge >= monthSpacing[milestoneIndex]){
                if (Float.parseFloat(percentage) >= 50){
                    description.setText("根據檢測結果，小孩在這個面向為發展正常。\n" +
                            "您可以進行下一階段的檢測，來更加瞭解小孩發展的進度。");
                } else {
                    description.setText("根據檢測結果，小孩的在這個面向的發展狀況疑似遲緩。\n" +
                            "為了瞭解小孩發展遲緩的程度，建議您尋求語言治療師來作評估與檢查，預防小孩錯過治療的黃金期。");
                }
            } else {
                if (Float.parseFloat(percentage) >= 50){
                    description.setText("根據檢測結果，小孩在這個面向的發展相當迅速，能夠快速掌握比現階段更進一步的溝通能力。\n" +
                            "不過，檢測的結果僅供參考，請您持續觀察小孩發展的情形，確保小孩都有維持正常的發展水平。");
                } else {
                    description.setText("小孩的年齡尚未達到該階段面向的月齡區間，檢測結果不如預期是正常的，請您無須擔心。\n" +
                            "耐心等待小孩成長至該階段後再進行檢測，結果才會準確哦！");
                }
            }
        });
    }
}