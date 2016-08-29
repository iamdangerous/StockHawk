package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.Modal.StockDetail;
import com.sam_chordas.android.stockhawk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StockDetailActivity extends Activity {

    LineChart lineChart;
    ArrayList<StockDetail> stockDetailArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        lineChart = (LineChart)findViewById(R.id.line_chart);

        if(getIntent()!=null)
        {
           stockDetailArrayList = new ArrayList<>();
            stockDetailArrayList =  getIntent().getParcelableArrayListExtra("detail");
            plotData(stockDetailArrayList);
        }

    }

    void plotData(ArrayList stockDetailArrayList)
    {
        List<Entry> entries = new ArrayList<>();
        for(int i=stockDetailArrayList.size()-1;i>-1;--i)
        {
            StockDetail stockDetail = (StockDetail) stockDetailArrayList.get(i);

            float y = Float.parseFloat(stockDetail.getOpen());
            String str = (stockDetail.getDate());
            String date[] = new String[3];
            date = str.split("-");//yyyy-mm-dd

            Entry entry = new Entry(Float.parseFloat(date[2]),y);
            entries.add(entry);


        }
//        entries.add(new Entry(1,2));
//        entries.add(new Entry(3,4));
//        entries.add(new Entry(5,6));


        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor((R.color.material_blue_500));
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
}

