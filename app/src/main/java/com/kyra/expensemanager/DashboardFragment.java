package com.kyra.expensemanager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    public DashboardFragment() {
//        Required empty public constructor
    }

    DBManager dbManager;
    LinearLayout linearLayout;
    PieChartView pieChartView;
    String dataType;
    Cursor cursor;
    ListView lvList;
    long totalAmount = 0;
    TextView tvTotalAmount;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        linearLayout = view.findViewById(R.id.layoutButton);
        setSingleEvent(linearLayout);
        lvList = view.findViewById(R.id.lvList);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        dbManager = new DBManager(getContext());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat wordDateFormat = new SimpleDateFormat("MMM");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat digitDateFormat = new SimpleDateFormat("MM");
//        Log.d("Month",simpleDateFormat.format(new Date()));
        cursor = dbManager.getSumCategoryMonthWise(digitDateFormat.format(new Date()));
        dataType = "Month : " + wordDateFormat.format(new Date());
//        Pie Chart Region
        pieChartView = view.findViewById(R.id.pieChart);
        pieChartView.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
//                Log.d("Pie Chart Label", String.valueOf(value.getLabelAsChars()));
                Intent intent = new Intent(getContext(),ListActivity.class);
                intent.putExtra("name",String.valueOf(value.getLabelAsChars()));
                startActivity(intent);
            }

            @Override
            public void onValueDeselected() {

            }
        });
        loadChartData(cursor);
        return view;
    }

    private void setSingleEvent(@NonNull LinearLayout linearLayout){
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            final int final_i = i;
            final Button button = (Button) linearLayout.getChildAt(final_i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (final_i){
                        case 0:
                            dataType = "Date : ";
                            callCalendarView();
                            break;
                        case 1:
                            dataType = "Month : ";
                            callMonthDialog();
                            break;
                        case 2:
                            dataType = "Year : ";
                            callYearDialog();
                            break;
                    }
                }
            });
        }
    }



    private void callCalendarView(){

        final Dialog calendarDialog = new Dialog(Objects.requireNonNull(getContext()));
        CalendarView calendarView  = new CalendarView(getContext());
        calendarDialog.setContentView(calendarView);
        Objects.requireNonNull(calendarDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(calendarDialog.getWindow()).setBackgroundDrawableResource(R.drawable.corners_rounded);
        calendarDialog.show();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String temp_month = new DecimalFormat("00").format(month+1);
                String temp_date = new DecimalFormat("00").format(dayOfMonth);
                String date =  year + "-" + temp_month + "-" + temp_date;
                dataType += date;
                cursor = dbManager.getSumCategoryDayWise(date);
//                Toast.makeText(getContext(),date,Toast.LENGTH_LONG).show();
//                Toast.makeText(getContext(),dataType,Toast.LENGTH_LONG).show();
                loadChartData(cursor);
                calendarDialog.dismiss();
            }
        });
    }

    private void callMonthDialog(){

        final Dialog monthDialog = new Dialog(Objects.requireNonNull(getContext()));
        monthDialog.setContentView(R.layout.dialog_months);
        Objects.requireNonNull(monthDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(monthDialog.getWindow()).setBackgroundDrawableResource(R.drawable.corners_rounded);
        monthDialog.show();
        TextView tvJan = monthDialog.findViewById(R.id.tvJan);
        tvJan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Jan";
                cursor = dbManager.getSumCategoryMonthWise("01");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvFeb = monthDialog.findViewById(R.id.tvFeb);
        tvFeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Feb";
                cursor = dbManager.getSumCategoryMonthWise("02");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvMar = monthDialog.findViewById(R.id.tvMar);
        tvMar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Mar";
                cursor = dbManager.getSumCategoryMonthWise("03");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvApr = monthDialog.findViewById(R.id.tvApr);
        tvApr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Apr";
                cursor = dbManager.getSumCategoryMonthWise("04");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvMay = monthDialog.findViewById(R.id.tvMay);
        tvMay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="May";
                cursor = dbManager.getSumCategoryMonthWise("05");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvJun = monthDialog.findViewById(R.id.tvJun);
        tvJun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Jun";
                cursor = dbManager.getSumCategoryMonthWise("06");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvJul = monthDialog.findViewById(R.id.tvJul);
        tvJul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Jul";
                cursor = dbManager.getSumCategoryMonthWise("07");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvAug = monthDialog.findViewById(R.id.tvAug);
        tvAug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Aug";
                cursor = dbManager.getSumCategoryMonthWise("08");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvSep = monthDialog.findViewById(R.id.tvSep);
        tvSep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Sep";
                cursor = dbManager.getSumCategoryMonthWise("09");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvOct = monthDialog.findViewById(R.id.tvOct);
        tvOct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Oct";
                cursor = dbManager.getSumCategoryMonthWise("10");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvNov = monthDialog.findViewById(R.id.tvNov);
        tvNov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Nov";
                cursor = dbManager.getSumCategoryMonthWise("11");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        TextView tvDec = monthDialog.findViewById(R.id.tvDec);
        tvDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataType+="Dec";
                cursor = dbManager.getSumCategoryMonthWise("12");
                loadChartData(cursor);
                monthDialog.dismiss();
            }
        });
        loadChartData(cursor);
    }

    private void callYearDialog(){

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog yearDialog = new Dialog(Objects.requireNonNull(getContext()));
        yearDialog.setContentView(R.layout.dialog_year);
        Objects.requireNonNull(yearDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(yearDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        yearDialog.show();
        final EditText etYear = yearDialog.findViewById(R.id.etYear);
        Button buSearch = yearDialog.findViewById(R.id.buSearch);
        buSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = dbManager.getSumCategoryYearWise(etYear.getText().toString());
                dataType+=etYear.getText().toString();
                loadChartData(cursor);
                yearDialog.dismiss();
            }
        });

    }

    private void loadChartData(Cursor cursor){
        String[] colors = getResources().getStringArray(R.array.colors);
        totalAmount = 0;
        final ArrayList<DashboardData> dashboardData = new ArrayList<>();
        final DashboardAdapter dashboardAdapter;
        List<SliceValue> sliceValues =  new ArrayList<>();
        int i=0;
        if(cursor.moveToFirst()){
            do{
                sliceValues.add(new SliceValue(Float.parseFloat(cursor.getString(cursor.getColumnIndex("SUM(Price)"))),
                        Color.parseColor(colors[i])).setLabel(cursor.getString(cursor.getColumnIndex(DBManager.colCategory))));
                dashboardData.add(new DashboardData(cursor.getString(cursor.getColumnIndex(DBManager.colCategory)),
                        cursor.getString(cursor.getColumnIndex("SUM(Price)"))));
                i++;
            }while (cursor.moveToNext());
        }
        for (int j = 0; j <sliceValues.size() ; j++) {
            totalAmount+=sliceValues.get(j).getValue();
        }
        tvTotalAmount.setText(String.valueOf(totalAmount));
        PieChartData pieChartData = new PieChartData(sliceValues);
        pieChartData.setHasLabels(true).setValueLabelTextSize(8);
        pieChartData.setHasCenterCircle(true).setCenterText1("Expenses").setCenterText1FontSize(20)
                .setCenterText1Color(Color.parseColor("#0097A7")).setCenterText2(dataType);
        pieChartView.setPieChartData(pieChartData);
        dashboardAdapter = new DashboardAdapter(dashboardData ,getContext());
        lvList.setAdapter(dashboardAdapter);
    }

}
