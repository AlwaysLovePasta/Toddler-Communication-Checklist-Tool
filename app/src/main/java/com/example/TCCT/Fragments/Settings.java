package com.example.TCCT.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.TCCT.Activities.MainActivity;
import com.example.TCCT.Adapter.RecyclerView.MeListAdapter;
import com.example.TCCT.R;

public class Settings extends Fragment {

    private static final String INDEX = "index";
    private Integer position;

    private RecyclerView meList;

    public Settings() {
        // Required empty public constructor
    }

    public static Settings newInstance(Integer position) {
        Settings fragment = new Settings();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        meList = view.findViewById(R.id.meList);

        setMeList();
    }

    private void setMeList() {
        MeListAdapter meListAdapter = new MeListAdapter(requireActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        meList.setLayoutManager(linearLayoutManager);
        meList.setAdapter(meListAdapter);
    }
}