package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.Modal.StockWidgetItem;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorWidgetAdapter;
import com.sam_chordas.android.stockhawk.service.StockTaskService;

import java.util.ArrayList;
import java.util.List;

public class WidgetActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Cursor mCursor;
    private QuoteCursorWidgetAdapter mCursorAdapter;
    RecyclerView recyclerView;
    Button btnDone, btnCancel;
    int mAppWidgetId;

   public static String TAG = "WidgetActivity";
    private static final int CURSOR_LOADER_ID = 0;
    ArrayList<StockWidgetItem> stockWidgetItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnDone = (Button) findViewById(R.id.button_done);
        btnCancel = (Button) findViewById(R.id.button_cancel);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        mCursorAdapter = new QuoteCursorWidgetAdapter(this, null);
        recyclerView.setAdapter(mCursorAdapter);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalSize = recyclerView.getAdapter().getItemCount();
                stockWidgetItemList = new ArrayList<>();

                for (int i = 0; i < totalSize; ++i) {

                    LinearLayout outerLinearLayout = (LinearLayout) recyclerView.getChildAt(i);
                    LinearLayout innerLinearLayout = (LinearLayout) outerLinearLayout.getChildAt(1);
                    CheckBox cb = (CheckBox) innerLinearLayout.getChildAt(2);
                    Cursor  cursor = mCursorAdapter.getCursor();



                    if (cb.isChecked()) {
                        if(cursor.moveToPosition(i))
                        {
                            String symbol = cursor.getString(cursor.getColumnIndex("symbol"));
                            String percent_change = cursor.getString(cursor.getColumnIndex("percent_change"));
                            String change = cursor.getString(cursor.getColumnIndex("change"));
                            String bid_price = cursor.getString(cursor.getColumnIndex("bid_price"));
                            int is_up = cursor.getInt(cursor.getColumnIndex("is_up"));

                            StockWidgetItem stockWidgetItem =
                                    new StockWidgetItem(symbol,percent_change,change,bid_price,is_up);
                            stockWidgetItemList.add(stockWidgetItem);
                        }

                    }
                }

                if(stockWidgetItemList.size()>0)
                {
                    updateWidget(stockWidgetItemList);
//                    Intent intent = new Intent(WidgetActivity.this,MyWidgetProvider.class);
//                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//                    intent.
                }

                Log.d(TAG, "integerListSize:" + stockWidgetItemList.size());
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void updateWidget(ArrayList<StockWidgetItem> stockWidgetItemList){
//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//
//        if (extras != null) {
//
//             mAppWidgetId = extras.getInt(
//                    AppWidgetManager.EXTRA_APPWIDGET_ID,
//                    AppWidgetManager.INVALID_APPWIDGET_ID);
//        }
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//        RemoteViews views = new RemoteViews(this.getPackageName(),
//                R.layout.my_widget);
//        appWidgetManager.updateAppWidget(mAppWidgetId, views);

//        Intent resultValue = new Intent();
//        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//        intent.putParcelableArrayListExtra(getString(R.string.stock_widget_item_list),stockWidgetItemList);
//        setResult(RESULT_OK, resultValue);
//        finish();

//        Intent intent2 = new Intent();
//        intent2.setAction(StockTaskService.INTENT_TAG);
//        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//        intent2.putParcelableArrayListExtra(getString(R.string.stock_widget_item_list),stockWidgetItemList);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);

        Intent intent1 = new Intent(TAG)
                .setPackage(this.getPackageName());
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        intent1.putParcelableArrayListExtra(getString(R.string.stock_widget_item_list),stockWidgetItemList);
        sendBroadcast(intent1);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        mCursor = data;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}
