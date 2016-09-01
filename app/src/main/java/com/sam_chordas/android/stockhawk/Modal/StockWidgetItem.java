package com.sam_chordas.android.stockhawk.Modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lohra on 9/1/2016.
 */
public class StockWidgetItem implements Parcelable {
    String SYMBOL;
    String PERCENT_CHANGE;
    String CHANGE;
    String BIDPRICE;
    int ISUP;

    public StockWidgetItem(String SYMBOL, String PERCENT_CHANGE, String CHANGE, String BIDPRICE, int ISUP) {
        this.SYMBOL = SYMBOL;
        this.PERCENT_CHANGE = PERCENT_CHANGE;
        this.CHANGE = CHANGE;
        this.BIDPRICE = BIDPRICE;
        this.ISUP = ISUP;
    }

    public StockWidgetItem() {
    }

    public String getSYMBOL() {
        return SYMBOL;
    }

    public void setSYMBOL(String SYMBOL) {
        this.SYMBOL = SYMBOL;
    }

    public String getPERCENT_CHANGE() {
        return PERCENT_CHANGE;
    }

    public void setPERCENT_CHANGE(String PERCENT_CHANGE) {
        this.PERCENT_CHANGE = PERCENT_CHANGE;
    }

    public String getCHANGE() {
        return CHANGE;
    }

    public void setCHANGE(String CHANGE) {
        this.CHANGE = CHANGE;
    }

    public String getBIDPRICE() {
        return BIDPRICE;
    }

    public void setBIDPRICE(String BIDPRICE) {
        this.BIDPRICE = BIDPRICE;
    }

    public int getISUP() {
        return ISUP;
    }

    public void setISUP(int ISUP) {
        this.ISUP = ISUP;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SYMBOL);
        dest.writeString(this.PERCENT_CHANGE);
        dest.writeString(this.CHANGE);
        dest.writeString(this.BIDPRICE);
        dest.writeInt(this.ISUP);
    }

    protected StockWidgetItem(Parcel in) {
        this.SYMBOL = in.readString();
        this.PERCENT_CHANGE = in.readString();
        this.CHANGE = in.readString();
        this.BIDPRICE = in.readString();
        this.ISUP = in.readInt();
    }

    public static final Parcelable.Creator<StockWidgetItem> CREATOR = new Parcelable.Creator<StockWidgetItem>() {
        @Override
        public StockWidgetItem createFromParcel(Parcel source) {
            return new StockWidgetItem(source);
        }

        @Override
        public StockWidgetItem[] newArray(int size) {
            return new StockWidgetItem[size];
        }
    };
}
