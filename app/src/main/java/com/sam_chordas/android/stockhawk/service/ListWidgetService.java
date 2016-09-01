package com.sam_chordas.android.stockhawk.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.Modal.StockWidgetItem;
import com.sam_chordas.android.stockhawk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lohra on 8/31/2016.
 */
public class ListWidgetService extends RemoteViewsService {

    String TAG = "ListWidgetService";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG,"RemoteViewsFactory");
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);

    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private Context mContext;
        private int mAppWidgetId;
        ArrayList<StockWidgetItem> stockWidgetItemList;

        public ListRemoteViewsFactory(Context context,Intent intent)
        {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            stockWidgetItemList = intent.getParcelableArrayListExtra(context.getString(R.string.stock_widget_item_list));
            if(null == stockWidgetItemList)
            {
                stockWidgetItemList = new ArrayList<>();
            }
        }
        @Override
        public void onCreate() {

            Log.d(TAG,"onCreate");
        }

        @Override
        public void onDataSetChanged() {
            Log.d(TAG,"onDataSetChanged");
        }

        @Override
        public void onDestroy() {
            stockWidgetItemList.clear();
        }

        @Override
        public int getCount() {
            return stockWidgetItemList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            Log.d(TAG,"getViewAt");
            if(stockWidgetItemList!=null)
            {
                rv.setTextViewText(R.id.tv_symbol,stockWidgetItemList.get(position).getSYMBOL());
            }
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
