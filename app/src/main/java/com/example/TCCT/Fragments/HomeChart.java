package com.example.TCCT.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.example.TCCT.ScoreLineChart.ChartMarkerView;
import com.example.TCCT.ScoreLineChart.LineChartData;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.auth.FirebaseAuth;


public class HomeChart extends Fragment {

    private static final String INDEX = "index";
    private Integer position;

    private String userUID;

    public HomeChart() {
        // Required empty public constructor
    }


    public static HomeChart newInstance(Integer position) {
        HomeChart fragment = new HomeChart();
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
        return inflater.inflate(R.layout.fragment_home_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayout noDataLayout = view.findViewById(R.id.noDataLayout);
        HorizontalScrollView chartView = view.findViewById(R.id.chartView);

        setVisibility(chartView, noDataLayout);
        displayChartData(view);
    }

    private void setVisibility(HorizontalScrollView chartView, LinearLayout noDataLayout) {
        FirebaseData firebaseData = new FirebaseData(userUID, FirebaseData.INIT_FIRESTORE);
        firebaseData.getMilestoneIndex(milestoneIndex -> {
            if (milestoneIndex.size() == 0){
                chartView.setVisibility(View.INVISIBLE);
                noDataLayout.setVisibility(View.VISIBLE);
            } else {
                chartView.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void displayChartData(View view) {
        FirebaseData firebaseData = new FirebaseData(userUID, FirebaseData.INIT_FIRESTORE);
        LineChart lineChart = view.findViewById(R.id.lineChart);
        LineChartData lineChartData = new LineChartData(requireContext(), lineChart);
        ChartMarkerView markerView = new ChartMarkerView(requireContext(), R.layout.item_chart_marker);
        firebaseData.getCurrentToddler(currentPosition -> lineChartData.displayLineChart(userUID, currentPosition));
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);
    }
}