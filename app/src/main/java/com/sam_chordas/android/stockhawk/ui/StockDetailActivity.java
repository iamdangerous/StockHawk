package com.sam_chordas.android.stockhawk.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.Modal.StockDetail;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.myInterface.DayAxisValueFormatter;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.service.StockIntentService;

import java.util.ArrayList;
import java.util.List;

public class StockDetailActivity extends AppCompatActivity {

    LineChart lineChart;
    ArrayList<StockDetail> stockDetailArrayList;
    String date[];
    TextView tvNoInternet;
    boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        lineChart = (LineChart) findViewById(R.id.line_chart);
        tvNoInternet = (TextView) findViewById(R.id.tv_no_internet);

        if (getIntent() != null) {
            String symbol = getIntent().getStringExtra("symbol");
            loadData(symbol);
        }

    }

    void loadData(String symbol) {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork;
        activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            Intent intentService = new Intent(this, StockIntentService.class);
            intentService.putExtra("job", "history");
            intentService.putExtra("symbol", symbol);
            startService(intentService);

        } else {
            showNoInternet();
        }

    }

    void plotData(@NonNull ArrayList stockDetailArrayList) {

        List<Entry> entries = new ArrayList<>();


        for (int i = stockDetailArrayList.size() - 1; i > -1; ) {
            StockDetail stockDetail = (StockDetail) stockDetailArrayList.get(i);

            float open = Float.parseFloat(stockDetail.getOpen());
            String str = (stockDetail.getDate());
            date = new String[3];
            date = str.split("-");//yyyy-mm-dd
            Log.wtf("Date:", "date[0]:" + date[0] + ",date[1]=" + date[1] + ",date[2]=" + date[2] + "open=" + open);

            //1.mm-dd-yyyy
//            formattedDateArray[i] = date[1]+"/"+date[2]+"/"+date[0];

            Entry entry = new Entry(Float.parseFloat(date[2]), open);
            entries.add(entry);
            i = i - 5;
        }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new DayAxisValueFormatter(stockDetailArrayList));
        xAxis.setGranularity(1f);

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor((R.color.material_blue_500));
        LineData lineData = new LineData(dataSet);
        lineData.setValueTextSize(18);
        lineData.setValueTextColor(getResources().getColor(R.color.white_1));
        lineChart.setData(lineData);
        lineChart.invalidate();
    }


    public void showNoInternet() {
        tvNoInternet.setVisibility(View.VISIBLE);
    }

    void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                    new IntentFilter(StockIntentService.TAG));
        }
    }

    void unRegisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterReceiver();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null)
            {
                ArrayList<StockDetail> stockDetailArrayList = new ArrayList<>();
                stockDetailArrayList =  intent.getParcelableArrayListExtra("detail");
                tvNoInternet.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
                plotData(stockDetailArrayList);
            }
        }
    };
}

