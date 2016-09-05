package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.Modal.StockDetail;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {
    public static String TAG = "StockIntentService";
    private OkHttpClient client = new OkHttpClient();
    public StockIntentService() {
        super(StockIntentService.class.getName());
    }

    public StockIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");

        if (intent.getStringExtra("job").equals("history")) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String endDate = sdf.format(new Date());;

            String arr[] = endDate.split("-");
            int customStartMonth = Integer.parseInt(arr[1]);

            if(customStartMonth<3)
            {
                customStartMonth = 1;
            }else {
                customStartMonth = Integer.parseInt(arr[1]) - 2;
            }

            String formattedStartDate = arr[0]+"-"+String.valueOf(customStartMonth)+"-"+arr[2];
            Log.d(TAG,"formatted End Date"+formattedStartDate);

            String symbol = intent.getStringExtra("symbol");
            String url = "\thttps://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"+symbol+"%22%20and%20startDate%20%3D%20%22"+formattedStartDate+"%22%20and%20endDate%20%3D%20%22"+endDate+"%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Log.d("url:", "->" + url.toString());
            Response response = null;
            ArrayList<StockDetail> stockDetailList = new ArrayList<>();
            try {
                response = client.newCall(request).execute();
//                Log.d(TAG,"response:"+response.body().string());

                String s =response.body().string();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONObject("query").getJSONObject("results").getJSONArray("quote");

                for(int i=0;i<jsonArray.length();++i)
                {
                    String sym = jsonArray.getJSONObject(i).getString("Symbol");
                    String date = jsonArray.getJSONObject(i).getString("Date");
                    String open = jsonArray.getJSONObject(i).getString("Open");
                    String high = jsonArray.getJSONObject(i).getString("High");
                    String low = jsonArray.getJSONObject(i).getString("Low");
                    String close = jsonArray.getJSONObject(i).getString("Close");
                    String vol = jsonArray.getJSONObject(i).getString("Volume");
                    String adj_close = jsonArray.getJSONObject(i).getString("Adj_Close");
                    StockDetail stockDetail = new StockDetail(sym,
                            date,
                            open,
                            high,
                            low,
                            close,
                            vol,
                            adj_close);
                    stockDetailList.add(stockDetail);

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent1 = new Intent(TAG);
            intent1.putParcelableArrayListExtra("detail",stockDetailList);
            Log.d(TAG,"size:"+stockDetailList.size());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
        } else {
            StockTaskService stockTaskService = new StockTaskService(this);
            Bundle args = new Bundle();
            if (intent.getStringExtra("tag").equals("add")) {
                args.putString("symbol", intent.getStringExtra("symbol"));
            }
            // We can call OnRunTask from the intent service to force it to run immediately instead of
            // scheduling a task.
            stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
        }

    }
}
