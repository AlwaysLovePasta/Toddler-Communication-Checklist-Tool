package com.example.TCCT.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.example.TCCT.Adapter.Fragment.FragmentMainAdapter;
import com.example.TCCT.Adapter.RecyclerView.ToddlerListAdapter;
import com.example.TCCT.DataModel.Toddler;
import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final String[] tabTitle = {"首頁", "聊天機器人", "其它"};
    private final int[] tabIcon = {R.drawable.ic_tab_home, R.drawable.ic_tab_chatbot, R.drawable.ic_tab_settings};

    private DrawerLayout mainDrawer;
    private Toolbar toolbar;
    private RecyclerView toddlerListView;
    private ViewPager2 navPager;
    private TabLayout navTab;
    private static String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*View Components*/
        mainDrawer = findViewById(R.id.mainDrawer);
        toolbar = findViewById(R.id.toolbar);
        toddlerListView = findViewById(R.id.toddlerList);
        navPager = findViewById(R.id.navPager);
        navTab = findViewById(R.id.navTab);
        Button btnAdd = findViewById(R.id.btnAdd);
        /*View Components*/

        getCurrentUser();
        setActionBar();
        setToddlerList();
        setNavPager();

        toolbar.setNavigationOnClickListener(view -> mainDrawer.open());
        btnAdd.setOnClickListener(view -> startActivity(new Intent(this, AddToddler.class)));
    }

    private void getCurrentUser() {
        userUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tabTitle[0]);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_toolbar_indicator));
        mainDrawer.addDrawerListener(drawerToggle);
        mainDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerToggle.syncState();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setToddlerList() {
        FirebaseData firebaseData = new FirebaseData(userUID, FirebaseData.INIT_FIRESTORE);
        ArrayList<Toddler> toddlerList = new ArrayList<>();
        ToddlerListAdapter toddlerListAdapter = new ToddlerListAdapter(this, toddlerList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        toddlerListAdapter.setActivity(this);
        toddlerListView.setLayoutManager(linearLayoutManager);
        toddlerListView.setAdapter(toddlerListAdapter);
        toddlerListAdapter.notifyDataSetChanged();
        firebaseData.displayToddlerList(toddlerList, toddlerListAdapter);
    }

    private void setNavPager() {
        FragmentMainAdapter mainAdapter = new FragmentMainAdapter(this);
        navPager.setAdapter(mainAdapter);
        navPager.setUserInputEnabled(false);

        new TabLayoutMediator(navTab, navPager, true, false,(tab, position) -> {
            tab.setText(tabTitle[position]);
            tab.setIcon(tabIcon[position]);
        }).attach();

        navTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                toolbar.setTitle(tabTitle[tab.getPosition()]);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}