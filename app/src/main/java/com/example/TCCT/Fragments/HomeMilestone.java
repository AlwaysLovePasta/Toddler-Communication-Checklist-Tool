package com.example.TCCT.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.TCCT.Adapter.RecyclerView.MilestoneListAdapter;
import com.example.TCCT.DataModel.Score;
import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class HomeMilestone extends Fragment {

    private static final String INDEX = "index";
    private Integer position;

    private FirebaseData firebaseData;

    private static String userUID;

    public HomeMilestone() {
        // Required empty public constructor
    }

    public static HomeMilestone newInstance(Integer position) {
        HomeMilestone fragment = new HomeMilestone();
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
        return inflater.inflate(R.layout.fragment_home_milestone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView monthList = view.findViewById(R.id.monthList);
        LinearLayout noDataLayout = view.findViewById(R.id.noDataLayout);
        userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        setMonthList(monthList);
        setVisibility(monthList, noDataLayout);
    }

    private void setVisibility(RecyclerView monthList, LinearLayout noDataLayout) {
        firebaseData.getMilestoneIndex(milestoneIndex -> {
            if (milestoneIndex.size() == 0){
                monthList.setVisibility(View.INVISIBLE);
                noDataLayout.setVisibility(View.VISIBLE);
            } else {
                monthList.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setMonthList(RecyclerView monthList) {
        firebaseData = new FirebaseData(userUID, FirebaseData.INIT_FIRESTORE);
        ArrayList<Score> scoreSet = new ArrayList<>();
        MilestoneListAdapter milestoneListAdapter = new MilestoneListAdapter(requireContext(), scoreSet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        monthList.setLayoutManager(linearLayoutManager);
        monthList.setAdapter(milestoneListAdapter);
        milestoneListAdapter.notifyDataSetChanged();
        retrieveScore(scoreSet, milestoneListAdapter);
    }

    @SuppressLint("DefaultLocale")
    private void retrieveScore(ArrayList<Score> scoreSet, MilestoneListAdapter milestoneListAdapter) {
        firebaseData.getCurrentToddler(currentPosition -> firebaseData.displayMilestoneList(scoreSet, milestoneListAdapter));
    }
}