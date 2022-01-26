package com.example.TCCT.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.TCCT.R;
import com.example.TCCT.ViewModels.ToddlerViewModel;

import java.util.ArrayList;

public class AddToddlerStep04 extends Fragment {

    private static final String INDEX = "index";

    private NavController navController;
    private ToddlerViewModel toddlerModel;

    private Uri imgUri;
    private ArrayList<String> basicData, detailData;
    private String dev;

    private FragmentListener fragmentListener;

    public interface FragmentListener {
        void onResultCallback(Uri imgUri, ArrayList<String> basicData, ArrayList<String> detailData);
    }

    public AddToddlerStep04() {
        // Required empty public constructor
    }

    public static AddToddlerStep04 newInstance(Integer position) {
        AddToddlerStep04 fragment = new AddToddlerStep04();
        Bundle args = new Bundle();
        args.putInt(INDEX, position);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_add_toddler_step04, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toddlerModel = new ViewModelProvider(requireActivity()).get(ToddlerViewModel.class);
        navController = Navigation.findNavController(view);
        detailData = new ArrayList<>();

        Button btnNext = view.findViewById(R.id.btnNext);
        RadioGroup optionGroup = view.findViewById(R.id.optionGroup);

        getData();
        getOption(optionGroup);
        btnNext.setOnClickListener(view1 -> {
            toddlerModel.getDevLevel().observe(requireActivity(), s -> detailData.add(s)); //不寫在getData的原因為防止陣列疊加
            fragmentListener.onResultCallback(imgUri, basicData, detailData);
        });
    }

    private void getData() {
        toddlerModel.getImgUri().observe(requireActivity(), uri -> imgUri = uri);
        toddlerModel.getFormData().observe(requireActivity(), strings -> basicData = strings);
        toddlerModel.getRelationship().observe(requireActivity(), s -> detailData.add(s));
        toddlerModel.getTime().observe(requireActivity(), s -> detailData.add(s));
    }

    @SuppressLint("NonConstantResourceId")
    private void getOption(RadioGroup optionGroup) {
        optionGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.optionDev01:
                    dev = requireActivity().getResources().getString(R.string.dev01);
                    break;
                case R.id.optionDev02:
                    dev = requireActivity().getResources().getString(R.string.dev02);
                    break;
                case R.id.optionDev03:
                    dev = requireActivity().getResources().getString(R.string.dev03);
                    break;
                case R.id.optionDev04:
                    dev = requireActivity().getResources().getString(R.string.dev04);
                    break;
            }
            toddlerModel.setDevLevel(dev);
        });
    }
}