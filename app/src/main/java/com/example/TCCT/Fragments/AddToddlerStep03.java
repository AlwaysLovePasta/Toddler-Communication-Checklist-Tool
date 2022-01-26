package com.example.TCCT.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.TCCT.R;
import com.example.TCCT.ViewModels.ToddlerViewModel;

public class AddToddlerStep03 extends Fragment {

    private static final String INDEX = "index";

    private NavController navController;
    private ToddlerViewModel toddlerModel;

    private String time;

    public AddToddlerStep03() {
        // Required empty public constructor
    }

    public static AddToddlerStep03 newInstance(Integer position) {
        AddToddlerStep03 fragment = new AddToddlerStep03();
        Bundle args = new Bundle();
        args.putInt(INDEX, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Integer position = getArguments().getInt(INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_toddler_step03, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toddlerModel = new ViewModelProvider(requireActivity()).get(ToddlerViewModel.class);
        navController = Navigation.findNavController(view);

        Button btnNext = view.findViewById(R.id.btnNext);
        RadioGroup optionGroup = view.findViewById(R.id.optionGroup);

        getOption(optionGroup);
        btnNext.setOnClickListener(view1 -> navController.navigate(R.id.action_addToddlerStep03_to_addToddlerStep04));
    }

    @SuppressLint("NonConstantResourceId")
    private void getOption(RadioGroup optionGroup) {
        optionGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.optionTime01:
                    time = requireActivity().getResources().getString(R.string.time01);
                    break;
                case R.id.optionTime02:
                    time = requireActivity().getResources().getString(R.string.time02);
                    break;
                case R.id.optionTime03:
                    time = requireActivity().getResources().getString(R.string.time03);
                    break;
                case R.id.optionTime04:
                    time = requireActivity().getResources().getString(R.string.time04);
                    break;
                case R.id.optionTime05:
                    time = requireActivity().getResources().getString(R.string.time05);
                    break;
            }
            toddlerModel.setTime(time);
        });
    }
}