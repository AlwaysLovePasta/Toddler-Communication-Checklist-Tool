package com.example.TCCT.ScoreLineChart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.TCCT.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public class ChartMarkerView extends MarkerView {

    private TextView txtMarker;
    private int index;
    private Context context;

    public ChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        this.context = context;
        txtMarker = findViewById(R.id.txtMarker);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        index = highlight.getDataSetIndex(); //取得當前被點擊座標所屬的資料組索引
        txtMarker.setText(Utils.formatNumber(e.getY(), 0, true) + "%");
        if (index == 0) {
            txtMarker.setBackground(ContextCompat.getDrawable(context, R.drawable.chart_marker_blue));
        } else {
            txtMarker.setBackground(ContextCompat.getDrawable(context, R.drawable.chart_marker_amber));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF((-getWidth() / 2), (-getHeight() * 1.5F));
    }
}
