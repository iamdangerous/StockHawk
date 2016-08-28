package com.sam_chordas.android.stockhawk.Modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lohra on 8/28/2016.
 */
public class StockDetail implements Parcelable {
    String symbol;
    String date;
    String open;
    String high;
    String low;
    String close;
    String volume;
    String adj_close;

    public StockDetail(String symbol, String date, String open, String high, String low, String close, String volume, String adj_close) {
        this.symbol = symbol;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.adj_close = adj_close;
    }

    public StockDetail() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getAdj_close() {
        return adj_close;
    }

    public void setAdj_close(String adj_close) {
        this.adj_close = adj_close;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.symbol);
        dest.writeString(this.date);
        dest.writeString(this.open);
        dest.writeString(this.high);
        dest.writeString(this.low);
        dest.writeString(this.close);
        dest.writeString(this.volume);
        dest.writeString(this.adj_close);
    }

    protected StockDetail(Parcel in) {
        this.symbol = in.readString();
        this.date = in.readString();
        this.open = in.readString();
        this.high = in.readString();
        this.low = in.readString();
        this.close = in.readString();
        this.volume = in.readString();
        this.adj_close = in.readString();
    }

    public static final Parcelable.Creator<StockDetail> CREATOR = new Parcelable.Creator<StockDetail>() {
        @Override
        public StockDetail createFromParcel(Parcel source) {
            return new StockDetail(source);
        }

        @Override
        public StockDetail[] newArray(int size) {
            return new StockDetail[size];
        }
    };
}
