package com.kyra.expensemanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class BankActivity extends AppCompatActivity{

    String category;
    DBManager dbManager;
    RecyclerView recyclerView;
//    ArrayList<BankData> bankDataList = new ArrayList<>();
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        category = bundle.getString("name");
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(category);
        recyclerView = findViewById(R.id.bankList);
        //createMockList();
        dbManager = new DBManager(this);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData();
    }

   /* private void createMockList(){
        BankData data;
        data = new BankData("Name","15111","7","hello");
        bankDataList.add(data);
        data = new BankData("Name","15221","8","hello");
        bankDataList.add(data);
        data = new BankData("Name","15331","9","hello");
        bankDataList.add(data);
        data = new BankData("Name","15441","5","hello");
        bankDataList.add(data);
    }*/

    public void loadData() {
        Cursor cursor = dbManager.fetchBankData(null, null, null, DBManager.colID);
        final ArrayList<BankData> bankDataList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                bankDataList.add(new BankData(cursor.getString(cursor.getColumnIndex(DBManager.colID)), cursor.getString(cursor.getColumnIndex(DBManager.colBankName)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colAccountNumber)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colIFSCCOde)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colRemarks))));
            } while (cursor.moveToNext());
        }

        final BankAdapter bankAdapter;
        bankAdapter = new BankAdapter(bankDataList, this);
        recyclerView.setAdapter(bankAdapter);
        bankAdapter.notifyDataSetChanged();
    }


    public void buAddBank(View view) {
        callAddBankDialog();
    }

    public void callAddBankDialog(){

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.dialog_add_bank);
        Objects.requireNonNull(addDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(addDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        addDialog.show();
        Button buAddBank = addDialog.findViewById(R.id.buAddBank);
        buAddBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etBankName = addDialog.findViewById(R.id.etBankName);
                EditText etAccountNumber = addDialog.findViewById(R.id.etAccountNumber);
                EditText etIFSCCode = addDialog.findViewById(R.id.etIFSCCode);
                EditText etRemarks = addDialog.findViewById(R.id.etBankRemarks);
                String bankName = etBankName.getText().toString();
                String accountNumber = etAccountNumber.getText().toString();
                String ifscCode = etIFSCCode.getText().toString();
                ifscCode = ifscCode.toUpperCase();
                String remarks = etRemarks.getText().toString();
                if(bankName.isEmpty() || accountNumber.isEmpty()){
                    Toast.makeText(getApplicationContext(),"One or more required fields are empty",Toast.LENGTH_LONG).show();
                }
                else {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBManager.colBankName, bankName );
                    contentValues.put(DBManager.colAccountNumber, accountNumber);
                    contentValues.put(DBManager.colIFSCCOde, ifscCode);
                    contentValues.put(DBManager.colRemarks, remarks);
                    long id = dbManager.insertBankData(contentValues);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_bank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            loadData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
