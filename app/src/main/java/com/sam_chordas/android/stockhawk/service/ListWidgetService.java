package com.sam_chordas.android.stockhawk.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.Modal.StockWidgetItem;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lohra on 8/31/2016.
 */
public class ListWidgetService extends RemoteViewsService {

    String TAG = "ListWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "RemoteViewsFactory");
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);

    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private int mAppWidgetId;
        private Cursor data = null;

        String[] mProjection =
                {
                        QuoteColumns._ID,
                        QuoteColumns.SYMBOL,
                        QuoteColumns.PERCENT_CHANGE,
                        QuoteColumns.ISUP,
                        QuoteColumns.BIDPRICE,
                        QuoteColumns.CHANGE

                };
        String mSelectionClause = QuoteColumns.ISCURRENT + " = ?";
        String mSelectionArgs[] = new String[]{"1"};

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        @Override
        public void onCreate() {

            Log.d(TAG, "onCreate");
        }

        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataSetChanged");
            final long identityToken = Binder.clearCallingIdentity();
            data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, mProjection, mSelectionClause, mSelectionArgs, null);
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION ||
                    data == null || !data.moveToPosition(position)) {
                return null;
            }
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            Log.d(TAG, "getViewAt");
            String symbol = data.getString(data.getColumnIndex(QuoteColumns.SYMBOL));

            String percent_change = data.getString(data.getColumnIndex(QuoteColumns.CHANGE));
            String bidPrice = data.getString(data.getColumnIndex(QuoteColumns.BIDPRICE));
            Log.wtf(TAG,"bidPrice:"+bidPrice);

            rv.setTextViewText(R.id.tv_bid_price,bidPrice);
            rv.setTextViewText(R.id.tv_change,percent_change);
            rv.setTextViewText(R.id.tv_symbol, symbol.toUpperCase());
//
//            int sdk = Build.VERSION.SDK_INT;
//
//            if (data.getInt(data.getColumnIndex("is_up")) == 1) {
//                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//                    TextView tv;
////                    tv.setBackground
//                    rv.setInt(R.id.tv_change,"setBackgroundDrawable",R.drawable.percent_change_pill_green);
////                    .setBackgroundDrawable(
////                            mContext.getResources().getDrawable(R.drawable.percent_change_pill_green));
//                } else {
////                    rv.setInt(R.id.tv_change,"setBackground",R.drawable.percent_change_pill_green);
//                    rv.setInt(R.id.tv_change,"setBackgroundDrawable",R.drawable.percent_change_pill_green);
//
//                }
//            } else {
//                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//                    rv.setInt(R.id.tv_change,"setBackgroundDrawable",R.drawable.percent_change_pill_red);
//                } else {
//                    rv.setInt(R.id.tv_change,"setBackground",R.drawable.percent_change_pill_red);
//
//                }
//            }



            final Intent fillInIntent = new Intent();
            fillInIntent.putExtra("symbol", symbol);
            rv.setOnClickFillInIntent(R.id.relative_layout, fillInIntent);
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
