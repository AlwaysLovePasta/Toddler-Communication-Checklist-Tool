package com.example.TCCT.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.TCCT.R;
import com.example.TCCT.Utils.ImageSelector;
import com.example.TCCT.ViewModels.ToddlerViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddToddlerStep01 extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String INDEX = "index";

    private final String[] genders = {"男生", "女生"};

    private NavController navController;
    private ToddlerViewModel toddlerModel;
    private ImageSelector imageSelector;

    private ShapeableImageView toddlerImg;
    private TextInputLayout nameInputLayout, dateInputLayout;

    private String imgUri, name, birth, gender, resident;

    public AddToddlerStep01() {
        // Required empty public constructor
    }

    public static AddToddlerStep01 newInstance(Integer position) {
        AddToddlerStep01 fragment = new AddToddlerStep01();
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
        imageSelector = new ImageSelector(requireActivity().getActivityResultRegistry());
        getLifecycle().addObserver(imageSelector);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_toddler_step01, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* View Components */
        toddlerImg = view.findViewById(R.id.toddlerImg);
        nameInputLayout = view.findViewById(R.id.nameInputLayout);
        dateInputLayout = view.findViewById(R.id.dateInputLayout);
        TextInputEditText edtDate = view.findViewById(R.id.edtDate);
        MaterialAutoCompleteTextView menuGender = view.findViewById(R.id.menuGender);
        MaterialAutoCompleteTextView menuResident = view.findViewById(R.id.menuResident);
        View btnImg = view.findViewById(R.id.btnImg);
        Button btnNext = view.findViewById(R.id.btnNext);
        /* View Components */

        navController = Navigation.findNavController(view);
        toddlerModel = new ViewModelProvider(requireActivity()).get(ToddlerViewModel.class);

        edtDate.setOnClickListener(view1 -> showDatePickerDialog());
        setMenuGender(menuGender);
        setMenuResident(menuResident);

        btnImg.setOnClickListener(view1 -> imageSelector.selectImage(toddlerImg));
        btnNext.setOnClickListener(view1 -> {
            storeFormData();
            navController.navigate(R.id.action_addToddlerStep01_to_addToddlerStep02);
        });
    }

    public void storeFormData(){
        name = Objects.requireNonNull(nameInputLayout.getEditText()).getText().toString();
        ArrayList<String> formData = new ArrayList<>();
        formData.add(name);
        formData.add(birth);
        formData.add(gender);
        formData.add(resident);
        toddlerModel.setImgUri(imageSelector.getImageUri());
        toddlerModel.setFormData(formData);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(
                requireContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(new Date().getTime()); //限制只有今天（含）之前的日期可以選擇
        datePicker.show();
    }

    private void setMenuGender(MaterialAutoCompleteTextView menuGender) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_down_menu, genders);
        menuGender.setAdapter(adapter);
        menuGender.setOnItemClickListener((adapterView, view, i, l) -> gender = adapterView.getItemAtPosition(i).toString());
    }

    private void setMenuResident(MaterialAutoCompleteTextView menuResident) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_down_menu, requireActivity().getResources().getStringArray(R.array.residents));
        menuResident.setAdapter(adapter);
        menuResident.setOnItemClickListener((adapterView, view, i, l) -> resident = adapterView.getItemAtPosition(i).toString());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        birth = String.format("%d-%02d-%02d", year, month+1, dayOfMonth);
        Objects.requireNonNull(dateInputLayout.getEditText()).setText(birth);
    }
}