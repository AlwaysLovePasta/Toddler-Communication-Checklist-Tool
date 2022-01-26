package com.example.TCCT.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.TCCT.Activities.EditToddler;
import com.example.TCCT.Activities.MainActivity;
import com.example.TCCT.Activities.MonthMenu;
import com.example.TCCT.Adapter.Fragment.FragmentHomeAdapter;
import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.example.TCCT.Utils.DownloadImageTask;
import com.example.TCCT.Utils.TimeConverter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Home extends Fragment {

    private static final String INDEX = "index";
    private Integer position;

    private final String[] tabTitle = {"發展折線圖", "發展里程碑"};

    private AsyncTask<String, Void, Bitmap> downloadImageTask;
    private TimeConverter timeConverter;

    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(Integer position) {
        Home fragment = new Home();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 homePager = view.findViewById(R.id.homePager);
        TabLayout homeTab = view.findViewById(R.id.homeTab);
        MaterialButton btnTest = view.findViewById(R.id.btnTest);
        ShapeableImageView toddlerImg = view.findViewById(R.id.toddlerImg);
        TextView toddlerName = view.findViewById(R.id.toddlerName);
        TextView monthAge = view.findViewById(R.id.monthAge);
        View edit = view.findViewById(R.id.edit);

        setProfileField(toddlerImg, toddlerName, monthAge);
        setHomePager(homePager, homeTab);
        btnTest.setOnClickListener(view1 -> startActivity(new Intent(requireActivity(), MonthMenu.class)));
        edit.setOnClickListener(view1 -> startActivity(new Intent(requireActivity(), EditToddler.class)));
    }

    private void setProfileField(ShapeableImageView toddlerImg, TextView toddlerName, TextView monthAge) {
        FirebaseData firebaseData = new FirebaseData(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseData.INIT_FIRESTORE);
        firebaseData.readToddlerData(requireContext(), (imgUri, basicData, detailData) -> {
            downloadImageTask = new DownloadImageTask(toddlerImg).execute(imgUri);
            toddlerName.setText(basicData.get(0));
            timeConverter = new TimeConverter(basicData.get(1), "yyyy-MM-dd");
            monthAge.setText(String.format("%s 個月又 %s 天", timeConverter.getMonths(), timeConverter.getDays()));
        });
    }


    private void setHomePager(ViewPager2 homePager, TabLayout homeTab) {
        FragmentHomeAdapter homeAdapter = new FragmentHomeAdapter(requireActivity());
        homePager.setAdapter(homeAdapter);
        homePager.setUserInputEnabled(false);

        new TabLayoutMediator(homeTab, homePager, ((tab, position) -> {
            tab.setText(tabTitle[position]);
        })).attach();
    }
}