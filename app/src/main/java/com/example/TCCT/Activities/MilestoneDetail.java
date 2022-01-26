package com.example.TCCT.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.TCCT.Adapter.Fragment.MilestoneDetailAdapter;
import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.example.TCCT.ViewModels.MilestoneDetailViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MilestoneDetail extends AppCompatActivity {

    private FirebaseData firebaseData;

    private TabLayout tabAbility;
    private ViewPager2 tabPager;
    private Integer milestonePosition;

    private final static String[] tabTitle = {"寶寶聽懂", "寶寶會說"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestone_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        MaterialButton btnBack = findViewById(R.id.btnBack);
        tabAbility = findViewById(R.id.tabAbility);
        tabPager = findViewById(R.id.tabPager);

        btnBack.setOnClickListener(view -> finish());
        getMilestoneIndex();
        setTabPager();
    }

    private void getMilestoneIndex() {
        MilestoneDetailViewModel viewModel = new ViewModelProvider(this).get(MilestoneDetailViewModel.class);
        firebaseData = new FirebaseData(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseData.INIT_FIRESTORE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        milestonePosition = bundle.getInt("POSITION");
        Log.e("Detail Page", "Position => " + milestonePosition);

        firebaseData.getMilestoneIndex(milestoneIndex -> viewModel.setMilestoneIndex(milestoneIndex.get(milestonePosition)));
    }

    private void setTabPager() {

        final int colorWhite = ContextCompat.getColor(this, R.color.white);
        final int colorBlue = ContextCompat.getColor(this, R.color.lightBlue_400);
        final int colorAmber = ContextCompat.getColor(this, R.color.amber_600);

        MilestoneDetailAdapter milestoneDetailAdapter = new MilestoneDetailAdapter(this);
        tabPager.setAdapter(milestoneDetailAdapter);

        new TabLayoutMediator(tabAbility, tabPager, (tab, position) -> tab.setText(tabTitle[position])).attach();

        tabAbility.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tabAbility.setTabTextColors(colorWhite, colorBlue);
                }
                if (tab.getPosition() == 1) {
                    tabAbility.setTabTextColors(colorWhite, colorAmber);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}