package com.sam_chordas.android.stockhawk.myInterface;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.sam_chordas.android.stockhawk.Modal.StockDetail;

import java.util.ArrayList;

/**
 * Created by lohra on 9/5/2016.
 */
public class DayAxisValueFormatter implements AxisValueFormatter{
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private LineChart chart;


    String [] date;
    //date[0]:year
    //date[1]:month
    //date[2]:date
    ArrayList stockDetailArrayList;

    public DayAxisValueFormatter(ArrayList stockDetailArrayList) {
//        this.date = date;
        this.stockDetailArrayList = stockDetailArrayList;
    }

        @Override
    public String getFormattedValue(float value, AxisBase axis) {

            StockDetail stockDetail = (StockDetail)  stockDetailArrayList.get((int)value);

            String str = (stockDetail.getDate());
            date = new String[3];
            date = str.split("-");//yyyy-mm-dd

            return date[1]+"-"+date[2];
//
//
//            int index = (int)value;
//            //yyyy-mm-dd
//            String year = date[index].split()
//
//        int days = (int) value;
//
//        int year = determineYear(days);
//
//        int month = determineMonth(days);
//        String monthName = mMonths[month % mMonths.length];
//        String yearName = String.valueOf(year);
//
//        if (chart.getVisibleXRange() > 30 * 6) {
//
//            return monthName + " " + yearName;
//        } else {
//
//            int dayOfMonth = determineDayOfMonth(days, month + 12 * (year - 2016));
//
//            String appendix = "th";
//
//            switch (dayOfMonth) {
//                case 1:
//                    appendix = "st";
//                    break;
//                case 2:
//                    appendix = "nd";
//                    break;
//                case 3:
//                    appendix = "rd";
//                    break;
//                case 21:
//                    appendix = "st";
//                    break;
//                case 22:
//                    appendix = "nd";
//                    break;
//                case 23:
//                    appendix = "rd";
//                    break;
//                case 31:
//                    appendix = "st";
//                    break;
//            }
//
//            return dayOfMonth == 0 ? "" : dayOfMonth + appendix + " " + monthName;

    }

    private int getDaysForMonth(int month, int year) {

        // month is 0-based

        if (month == 1) {
            int x400 = month % 400;
            if (x400 < 0)
            {
                x400 = -x400;
            }
            boolean is29 = (month % 4) == 0 && x400 != 100 && x400 != 200 && x400 != 300;

            return is29 ? 29 : 28;
        }

        if (month == 3 || month == 5 || month == 8 || month == 10)
            return 30;
        else
            return 31;
    }

    private int determineMonth(int dayOfYear) {

        int month = -1;
        int days = 0;

        while (days < dayOfYear) {
            month = month + 1;

            if (month >= 12)
                month = 0;

            int year = determineYear(days);
            days += getDaysForMonth(month, year);
        }

        return Math.max(month, 0);
    }

    private int determineDayOfMonth(int dayOfYear, int month) {

        int count = 0;
        int days = 0;

        while (count < month) {

            int year = determineYear(days);
            days += getDaysForMonth(count % 12, year);
            count++;
        }

        return dayOfYear - days;
    }

    private int determineYear(int days) {

        if (days <= 366)
            return 2016;
        else if (days <= 730)
            return 2017;
        else if (days <= 1094)
            return 2018;
        else if (days <= 1458)
            return 2019;
        else
            return 2020;

    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
