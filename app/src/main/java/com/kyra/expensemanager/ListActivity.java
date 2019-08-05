package com.kyra.expensemanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class ListActivity extends AppCompatActivity{

    String date;
    Button buAddExpense;
    EditText etDate;
    DBManager dbManager;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        category = bundle.getString("name");
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(category);
        dbManager = new DBManager(this);
        loadData();
    }

    public void loadData(){
        String[] selectionArgs = {category};
        Cursor cursor = dbManager.fetchData(null,DBManager.colCategory + " LIKE ?",selectionArgs,DBManager.colDate);
        final ArrayList<ListData>  listData = new ArrayList<>();
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

        listAdapter = new ListAdapter(listData ,this);
        final ListView  lvList = findViewById(R.id.lvList);
        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListData listEdit = (ListData) parent.getItemAtPosition(position);
                callEditDelItemDialog(listEdit);
                return true;
            }
        });
        lvList.setAdapter(listAdapter);
    }

    public void buAdd(View view) {
        callAddProductDialog();
    }

    private void callAddProductDialog(){

        final SentenceCase sentenceCase = new SentenceCase();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.dialog_add_product);
        Objects.requireNonNull(addDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(addDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addDialog.show();
        etDate = addDialog.findViewById(R.id.etDate);
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    etDate.setInputType(InputType.TYPE_NULL);
                    callCalendarView();
                }
            }
        });
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDate.setInputType(InputType.TYPE_NULL);
                callCalendarView();
            }
        });
        buAddExpense = addDialog.findViewById(R.id.buAddExpense);
        buAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etProduct = addDialog.findViewById(R.id.etProduct);
                EditText etPrice = addDialog.findViewById(R.id.etPrice);
                EditText etRemarks = addDialog.findViewById(R.id.etRemarks);
                String product = etProduct.getText().toString();
                product = sentenceCase.caseConvertCapWords(product);
                String date = etDate.getText().toString();
                String price = etPrice.getText().toString();
                String remarks = etRemarks.getText().toString();
                remarks = sentenceCase.caseConvertCapWords(remarks);
                if(product.isEmpty() || date.isEmpty() || price.isEmpty()){
                    Toast.makeText(getApplicationContext(),"One or more required fields are empty",Toast.LENGTH_LONG).show();
                }
                else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBManager.colItem, product );
                    contentValues.put(DBManager.colDate, etDate.getText().toString());
                    contentValues.put(DBManager.colPrice, price);
                    contentValues.put(DBManager.colCategory, category);
                    contentValues.put(DBManager.colRemarks, remarks);
                    long id = dbManager.insertData(contentValues);
                    if (id > 0) {
                        Toast.makeText(getApplicationContext(), "Data Added", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Data Not Added", Toast.LENGTH_LONG).show();
                    }
                    loadData();
                    addDialog.dismiss();
                }
            }
        });
    }

    private void callCalendarView(){
        final Dialog calendarDialog = new Dialog(this);
        CalendarView calendarView  = new CalendarView(this);
        calendarView.setMaxDate(System.currentTimeMillis());
        calendarDialog.setContentView(calendarView);
        Objects.requireNonNull(calendarDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(calendarDialog.getWindow()).setBackgroundDrawableResource(R.drawable.corners_rounded);
        calendarDialog.show();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String temp_month = new DecimalFormat("00").format(month+1);
                String temp_date = new DecimalFormat("00").format(dayOfMonth);
                date =  year + "-" + (temp_month) + "-" + temp_date;
                etDate.setText(date);
                calendarDialog.dismiss();
            }
        });
    }

    public void callEditDelItemDialog(final ListData listEdit){
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.5);
        final Dialog editDelDialog = new Dialog(this);
        editDelDialog.setContentView(R.layout.dialog_edit_delete);
        editDelDialog.setCancelable(true);
        Objects.requireNonNull(editDelDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(editDelDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        editDelDialog.show();
        final TextView tvEdit = editDelDialog.findViewById(R.id.tvEdit);
        final TextView tvDelete = editDelDialog.findViewById(R.id.tvDelete);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Edit Button",tvEdit.toString());
                callEditProductDialog(listEdit);
                editDelDialog.dismiss();
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Delete Button",tvDelete.toString());
                long id =  dbManager.deleteData(listEdit.id);
                if (id > 0) {
                    Toast.makeText(getApplicationContext(), "Data Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Data Not Deleted", Toast.LENGTH_LONG).show();
                }
                loadData();
                editDelDialog.dismiss();
            }
        });
    }

    private void callEditProductDialog(@NonNull final ListData listEdit){

        final SentenceCase sentenceCase = new SentenceCase();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog editDialog = new Dialog(this);
        editDialog.setContentView(R.layout.dialog_add_product);
        Objects.requireNonNull(editDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(editDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText etProduct = editDialog.findViewById(R.id.etProduct);
        final EditText etPrice = editDialog.findViewById(R.id.etPrice);
        final EditText etRemarks = editDialog.findViewById(R.id.etRemarks);
        etDate = editDialog.findViewById(R.id.etDate);
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    etDate.setInputType(InputType.TYPE_NULL);
                    callCalendarView();
                }
            }
        });
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDate.setInputType(InputType.TYPE_NULL);
                callCalendarView();
            }
        });
        etProduct.setText(listEdit.item);
        etPrice.setText(listEdit.price);
        etRemarks.setText(listEdit.remarks);
        etDate.setText(listEdit.date);
        editDialog.show();
        buAddExpense = editDialog.findViewById(R.id.buAddExpense);
        buAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("ListData", String.valueOf(listEdit.id));
                String product = etProduct.getText().toString();
                product = sentenceCase.caseConvertCapWords(product);
                String price = etPrice.getText().toString();
                String remarks = etRemarks.getText().toString();
                remarks = sentenceCase.caseConvertCapWords(remarks);
                String date = etDate.getText().toString();
                if(product.isEmpty() || date.isEmpty() || price.isEmpty()){
                    Toast.makeText(getApplicationContext(),"One or more required fields are empty",Toast.LENGTH_LONG).show();
                }
                else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBManager.colItem, product );
                    contentValues.put(DBManager.colDate, etDate.getText().toString());
                    contentValues.put(DBManager.colPrice, price);
                    contentValues.put(DBManager.colCategory, category);
                    contentValues.put(DBManager.colRemarks, remarks);
                    long id = dbManager.updateData(contentValues,listEdit.id);
                    if (id > 0) {
                        Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Data Not Updated", Toast.LENGTH_LONG).show();
                    }
                    loadData();
                    editDialog.dismiss();
                }
            }
        });
    }
}
