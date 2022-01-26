package com.example.TCCT.ScoreLineChart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.TCCT.Database.FirebaseData;
import com.example.TCCT.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LineChartData {

    Context context;
    Activity activity;
    LineChart lineChart;
    LineDataSet set;
    ArrayList<Entry> scoreListen, scoreTalk;

    public LineChartData(Context context, LineChart lineChart) {
        this.context = context;
        this.lineChart = lineChart;
    }

    public void initDataSet() {

        if (scoreListen.size() > 0) {
            LineData data = new LineData(getDataSet(scoreListen, R.color.lightBlue_400),
                    getDataSet(scoreTalk, R.color.amber_400));
            lineChart.setData(data);//一定要放在最後
        } else {
            lineChart.clear();
            lineChart.setBorderWidth(10);
            lineChart.getPaint(Chart.PAINT_INFO).setTextSize(Utils.convertDpToPixel(14f));
            lineChart.setNoDataTextColor(ContextCompat.getColor(context, R.color.grey_400));//文字顏色
            lineChart.setNoDataTextTypeface(ResourcesCompat.getFont(context, R.font.jf_open));
        }

        initX();
        initY();

        lineChart.animateY(1500, Easing.EaseOutSine);
        //設定折線圖左右的間距大小
        lineChart.setExtraRightOffset(50f);
        lineChart.setExtraLeftOffset(20f);

        lineChart.setTouchEnabled(true);// 禁用縮放及點二下觸摸響應，點擊也不顯示高亮線
        lineChart.setPinchZoom(false); // true->X、Y軸同時按比例縮放、false:X、Y可單獨縮放
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();//繪製圖表

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false); //不顯示圖例
    }

    @SuppressLint("DefaultLocale")
    public void initX() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(9);//X軸標籤個數
        xAxis.setGranularity(1f); //X軸級距
        xAxis.setAxisMinimum(0f); //X軸顯示最小值
        xAxis.setAxisMaximum(8f); //X軸顯示最大值

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X軸標籤顯示位置(預設顯示在上方，分為上方內/外側、下方內/外側及上下同時顯示)
        xAxis.setTextColor(Color.GRAY);//X軸標籤顏色
        xAxis.setTypeface(ResourcesCompat.getFont(context, R.font.jf_open));
        xAxis.setTextSize(12);//X軸標籤大小
        xAxis.setAxisLineWidth(1.2f); //X軸線寬度

        xAxis.setDrawGridLines(false);//不顯示每個座標點對應X軸的線 (預設顯示)

        //自定義X軸標籤值
        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            label.add(String.format("%d-%d個月", i * 4, (i + 1) * 4));
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(label));
    }

    public void initY() {
        YAxis rightAxis = lineChart.getAxisRight();//獲取右側的軸線
        rightAxis.setEnabled(false);//不顯示右側Y軸
        YAxis leftAxis = lineChart.getAxisLeft();//獲取左側的軸線

        leftAxis.setLabelCount(4);//Y軸標籤個數
        leftAxis.setTextColor(Color.GRAY);//Y軸標籤顏色
        leftAxis.setTextSize(12);//Y軸標籤大小
        leftAxis.setTypeface(ResourcesCompat.getFont(context, R.font.jf_open));
        leftAxis.setAxisLineWidth(1.2f); //Y軸線寬度
        leftAxis.setGridColor(ContextCompat.getColor(context, R.color.grey_400));

        leftAxis.setAxisMinimum(0F);//Y軸標籤最小值
        leftAxis.setAxisMaximum(100F);//Y軸標籤最大值
        leftAxis.setGranularity(5f);
        leftAxis.resetAxisMinimum();
    }

    public LineDataSet getDataSet(ArrayList<Entry> entryList, int color) {
        for (int i = 0; i < entryList.size(); i++) {
            set = new LineDataSet(entryList, "");
            set.setMode(LineDataSet.Mode.LINEAR);
            set.setColor(ContextCompat.getColor(context, color));//線的顏色
            set.setCircleColor(ContextCompat.getColor(context, color));//圓點顏色
            set.setCircleRadius(4);//圓點大小
            set.setDrawCircleHole(false);//圓點為實心(預設空心)
            set.setLineWidth(2f);//線寬
            set.setDrawValues(false);//顯示座標點對應Y軸的數字(預設顯示)
            set.setValueTextSize(12);//座標點數字大小
            set.setDrawHighlightIndicators(false);
            Description description = lineChart.getDescription();
            description.setEnabled(false);//不顯示Description Label (預設顯示)
            set.setDrawFilled(true);
            set.setFillColor(context.getResources().getColor(color));
            set.setFillAlpha(45);
        }
        return set;
    }

    @SuppressLint("DefaultLocale")
    public void displayLineChart(String userUID, int currentToddler) {
        String collectionPath = String.format("User/%s/Toddler/%03d/Milestone", userUID, currentToddler);
        CollectionReference collectionMilestone = FirebaseFirestore.getInstance().collection(collectionPath);
        collectionMilestone.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        scoreListen = new ArrayList<>();
                        scoreTalk = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            ArrayList<Integer> scoreSet = (ArrayList<Integer>) documentSnapshot.get("scoreSet");
                            int x = Integer.parseInt(documentSnapshot.getId());
                            float listen = ((Number) scoreSet.get(0)).floatValue();
                            float talk = ((Number) scoreSet.get(1)).floatValue();
                            if (x == 0 || x == 1) {
                                scoreListen.add(new Entry(x, (listen / 25) * 100));
                                scoreTalk.add(new Entry(x, (talk / 25) * 100));
                            }
                            if (x == 2) {
                                scoreListen.add(new Entry(x, (listen / 20) * 100));
                                scoreTalk.add(new Entry(x, (talk / 30) * 100));
                            }
                            if (x >= 3 && x <= 7) {
                                scoreListen.add(new Entry(x, (listen / 20) * 100));
                                scoreTalk.add(new Entry(x, (talk / 25) * 100));
                            }
                            if (x == 8) {
                                scoreListen.add(new Entry(x, (listen / 30) * 100));
                                scoreTalk.add(new Entry(x, (talk / 30) * 100));
                            }
                        }
                        initDataSet();
                    }
                });
    }
}
