package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.Modal.StockWidgetItem;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.ListWidgetService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;
import com.sam_chordas.android.stockhawk.ui.StockDetailActivity;
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
            Intent listWidgetIntent = new Intent(context, ListWidgetService.class);
            listWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//            listWidgetIntent.setData(Uri.parse(listWidgetIntent.toUri(Intent.URI_INTENT_SCHEME)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views,listWidgetIntent);
            } else {
                setRemoteAdapterV11(context, views,listWidgetIntent);
            }
            views.setEmptyView(R.id.list_view, R.id.empty_view);
            Intent intent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, 0
            );
            views.setOnClickPendingIntent(R.id.frame_widget, pendingIntent);

            //ListItemIntent
            Intent listItemIntent = new Intent(context, StockDetailActivity.class);
            listItemIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            PendingIntent pendingIntentListItem = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(listItemIntent)
                    .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.list_view,pendingIntentListItem);

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

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, getClass()));

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views,Intent intent) {
        views.setRemoteAdapter(R.id.list_view,
                intent);
    }
    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views,Intent intent) {
        views.setRemoteAdapter(0, R.id.list_view,
                intent);
    }

}
