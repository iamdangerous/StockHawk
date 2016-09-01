package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.Modal.StockWidgetItem;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.ListWidgetService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;
import com.sam_chordas.android.stockhawk.ui.WidgetActivity;

import java.util.ArrayList;

/**
 * Created by lohra on 8/28/2016.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    String TAG = "WidgetProvider";
    ImageView imageViewAdd;
    boolean isReceiverRegistered;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.my_widget
            );

            //SERVICE STARTS
            Intent listWidgetIntent = new Intent(context, ListWidgetService.class);
            // Add the app widget ID to the intent extras.
            listWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            listWidgetIntent.setData(Uri.parse(listWidgetIntent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(appWidgetId, R.id.list_view, listWidgetIntent);
            views.setEmptyView(R.id.list_view, R.id.empty_view);

            //SERVICE ENDS


            Intent intent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, 0
            );
            views.setOnClickPendingIntent(R.id.frame_widget, pendingIntent);


            Intent intent2 = new Intent(context, WidgetActivity.class);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(
                    context, 0, intent2, 0
            );
            views.setOnClickPendingIntent(R.id.image_view_add, pendingIntent2);


            appWidgetManager.updateAppWidget(appWidgetId, views);


        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceiveCalled");
        if (intent != null) {
            Log.d(TAG, "intent Action:" + intent.getAction());
            //execute intent service
            if (intent.getAction().equals(WidgetActivity.TAG)) {
                ArrayList<StockWidgetItem> stockWidgetItemArrayList =
                        intent.getParcelableArrayListExtra(context.getString(R.string.stock_widget_item_list));

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, getClass()));

//                    RemoteViews views = new RemoteViews(
//                            context.getPackageName(),
//                            R.layout.my_widget
//                    );
//                    Intent listWidgetIntent = new Intent(context, ListWidgetService.class);
//                    listWidgetIntent.putParcelableArrayListExtra(context.getString(R.string.stock_widget_item_list), stockWidgetItemArrayList);
//                    listWidgetIntent.setData(Uri.parse(listWidgetIntent.toUri(Intent.URI_INTENT_SCHEME)));
//                    views.setRemoteAdapter(R.id.list_view, listWidgetIntent);
//                    appWidgetManager.updateAppWidget(mAppWidgetId, views);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.list_view);

                }
            }
        }


        @Override
        public void onAppWidgetOptionsChanged (Context context, AppWidgetManager appWidgetManager,
        int appWidgetId, Bundle newOptions){
            super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

            Log.d(TAG, "onAppWidgetOptionsChanged called");
            //execute intent service
        }
    }
