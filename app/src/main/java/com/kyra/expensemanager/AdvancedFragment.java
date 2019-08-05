package com.kyra.expensemanager;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */

public class AdvancedFragment extends Fragment {

    Spinner spinnerSearch;
    EditText etSearch;
    ListView lvList;
    Button buSearch;
    FloatingActionButton buAmount;

    DBManager dbManager;
    CommonMethodArea commonMethodArea;
    long totalAmount = 0;

    public AdvancedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_advanced, container, false);
        dbManager = new DBManager(getContext());
        spinnerSearch = view.findViewById(R.id.spinnerSearch);
        etSearch = view.findViewById(R.id.etSearch);
        lvList = view.findViewById(R.id.lvList);
        commonMethodArea = new CommonMethodArea(getContext());
        buSearch = view.findViewById(R.id.buSearch);
        buAmount = view.findViewById(R.id.buAmount);
        final SentenceCase sentenceCase = new SentenceCase();

        buAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Total Amount : " + totalAmount,Toast.LENGTH_LONG).show();
            }
        });

        spinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                String searchItem=spinnerSearch.getSelectedItem().toString();
                etSearch.setText("");

//                Log.d("Selected item ",searchItem);
                switch (searchItem){
                    case "Day-Wise":
                        etSearch.setHint("Enter Date");
                        callCalendarView();
                        if(etSearch.getHint().equals("Enter Date")){
                            etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (hasFocus){
                                        etSearch.setInputType(InputType.TYPE_NULL);
                                        callCalendarView();
                                    }
                                }
                            });
                            etSearch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    etSearch.setInputType(InputType.TYPE_NULL);
                                    callCalendarView();
                                }
                            });
                        }
                        break;
                    case "Month-Wise":
                        etSearch.setHint("Enter Month");
                        callMonthDialog();
                        if(etSearch.getHint().equals("Enter Month")){
                            etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (hasFocus){
                                        etSearch.setInputType(InputType.TYPE_NULL);
                                        callMonthDialog();
                                    }
                                }
                            });
                            etSearch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    callMonthDialog();
                                }
                            });
                        }
                        break;
                    case "Year-Wise":
                        etSearch.setHint("Enter Year(YYYY)");
                        etSearch.setText(commonMethodArea.getCurrentYear());
                        etSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                        etSearch.setOnFocusChangeListener(null);
                        etSearch.setOnClickListener(null);
                        buSearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String[] selectionArgs = {etSearch.getText().toString()};
//                                Log.d("Clicked Data",selectionArgs[0]);
                                Cursor cursor = dbManager.searchByYear(selectionArgs);
//                                Log.d("Cursor", String.valueOf(cursor.moveToFirst()));
                                loadData(cursor);
                            }
                        });
                        break;
                    case "Product":
                        etSearch.setHint("Enter Product");
                        etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                        etSearch.setOnFocusChangeListener(null);
                        etSearch.setOnClickListener(null);
                        buSearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String product = sentenceCase.caseConvertCapWords(etSearch.getText().toString());
                                String[] selectionArgs = {product};
//                                Log.d("Clicked Data",selectionArgs[0]);
                                Cursor cursor = dbManager.searchByProduct(selectionArgs);
//                                Log.d("Cursor", String.valueOf(cursor.moveToFirst()));
                                loadData(cursor);
                            }
                        });
                        break;
                    case "Price":
                        etSearch.setHint("Click To Select");
                        callPriceRangeDialog();
                        if(etSearch.getHint().equals("Click To Select")) {
                            etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (hasFocus){
                                        etSearch.setInputType(InputType.TYPE_NULL);
                                        callPriceRangeDialog();
                                    }
                                }
                            });
                            etSearch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    callPriceRangeDialog();
                                }
                            });
                        }
                        break;
                    case "Advanced":
                        etSearch.setHint("Click To Search");
                        callAdvancedDialog();
                        if(etSearch.getHint().equals("Click To Search")) {
                            etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (hasFocus){
                                        etSearch.setInputType(InputType.TYPE_NULL);
                                        callAdvancedDialog();
                                    }
                                }
                            });
                            etSearch.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    callAdvancedDialog();
                                }
                            });
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    public void loadData(Cursor cursor){
        final ArrayList<ListData> listData = new ArrayList<>();
        final ListAdapter listAdapter;
        if(cursor.moveToFirst()){
            do{
                listData.add(new ListData(cursor.getString(cursor.getColumnIndex(DBManager.colID)),cursor.getString(cursor.getColumnIndex(DBManager.colItem)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colDate)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colPrice)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colCategory)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colRemarks))));
            }while (cursor.moveToNext());
        }
        listAdapter = new ListAdapter(listData ,getContext());
        totalAmount = 0;
        lvList.setAdapter(listAdapter);
        for (int i = 0; i <listData.size() ; i++) {
            totalAmount+=Long.parseLong(listData.get(i).getPrice());
        }
    }

    private void callCalendarView(){

        buSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] selectionArgs = {etSearch.getText().toString()};
//                Log.d("Clicked Data",selectionArgs[0]);
                Cursor cursor = dbManager.searchByDate(null,DBManager.colDate + " LIKE ?",selectionArgs,DBManager.colID);
//                Log.d("Cursor", String.valueOf(cursor.moveToFirst()));
                loadData(cursor);
            }
        });

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
                String date =  year + "-" + (temp_month) + "-" + temp_date;
                etSearch.setText(date);
                calendarDialog.dismiss();
            }
        });
    }

    private void callMonthDialog(){
        buSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] selectionArgs = {etSearch.getText().toString()};
                Cursor cursor = dbManager.searchByMonth(selectionArgs);
                loadData(cursor);
            }
        });

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog monthDialog = new Dialog(Objects.requireNonNull(getContext()));
        monthDialog.setContentView(R.layout.dialog_months_year);
        Objects.requireNonNull(monthDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(monthDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        monthDialog.show();
        final EditText etYear = monthDialog.findViewById(R.id.etYear);
        Button buSet = monthDialog.findViewById(R.id.buSet);
        etYear.setText(commonMethodArea.getCurrentYear());
        final String[] temp_month = new String[1];
        Spinner spinnerMonths = monthDialog.findViewById(R.id.spinnerMonths);
        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temp_month[0] = new DecimalFormat("00").format(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = etYear.getText().toString();
                etSearch.setText(String.format("%s-%s", temp_month[0], year));
                monthDialog.dismiss();
            }
        });
    }

    private void callPriceRangeDialog(){

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog priceRangeDialog = new Dialog(Objects.requireNonNull(getContext()));
        priceRangeDialog.setContentView(R.layout.dialog_price_search);
        Objects.requireNonNull(priceRangeDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(priceRangeDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        priceRangeDialog.show();
        Button buSearch = priceRangeDialog.findViewById(R.id.buSearch);
        buSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etStartRange = priceRangeDialog.findViewById(R.id.etStartRange);
                EditText etEndRange = priceRangeDialog.findViewById(R.id.etEndRange);
                String startRange = etStartRange.getText().toString();
                String endRange = etEndRange.getText().toString();
//                Log.d("Clicked Data",startRange);
//                Log.d("Clicked Data",endRange);
                String[] selectionArgs = {startRange,endRange};
                Cursor cursor = dbManager.searchByPriceRange(selectionArgs);
//                Log.d("Cursor", String.valueOf(cursor.moveToFirst()));
                loadData(cursor);
                priceRangeDialog.dismiss();
            }
        });
    }

    private void callAdvancedDialog(){

        final SentenceCase sentenceCase = new SentenceCase();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog advancedDialog = new Dialog(Objects.requireNonNull(getContext()));
        advancedDialog.setContentView(R.layout.dialog_advanced_search);
        Objects.requireNonNull(advancedDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(advancedDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        advancedDialog.show();
        final EditText etYear = advancedDialog.findViewById(R.id.etYear);
        etYear.setText(commonMethodArea.getCurrentYear());
        final String[] temp_month = new String[1];
        Spinner spinnerMonth = advancedDialog.findViewById(R.id.spinnerMonths);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temp_month[0] = new DecimalFormat("00").format(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button buSearch = advancedDialog.findViewById(R.id.buSearch);
        buSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etProduct = advancedDialog.findViewById(R.id.etProduct);
                String product = sentenceCase.caseConvertCapWords(etProduct.getText().toString());
                String month = temp_month[0] + "-" + etYear.getText().toString();
                String[] selectionArgs = {month,product};
                Cursor cursor = dbManager.searchAdvanced(selectionArgs);
                loadData(cursor);
                advancedDialog.dismiss();
            }
        });
    }

}
