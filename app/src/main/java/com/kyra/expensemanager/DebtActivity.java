package com.kyra.expensemanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class DebtActivity extends AppCompatActivity {

    String category;
    DBManager dbManager;
    TextView tvBorrowed,tvOwed,tvNetAmount;
    long totalBorrowed = 0,totalOwed = 0,netAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        category = bundle.getString("name");
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(category);
        dbManager = new DBManager(this);
        tvBorrowed = findViewById(R.id.tvBorrowed);
        tvOwed = findViewById(R.id.tvOwed);
        tvNetAmount = findViewById(R.id.tvNetAmount);
        loadData();
    }

    public void loadData(){
        totalBorrowed = 0;
        totalOwed = 0;
        Cursor cursor = dbManager.fetchDebtData(null,null,null,DBManager.colID);
        final ArrayList<DebtData> debtList = new ArrayList<>();
        final DebtAdapter debtAdapter;

        if(cursor.moveToFirst()){
            do{
                debtList.add(new DebtData(cursor.getString(cursor.getColumnIndex(DBManager.colID)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colName)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colAmount)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colType)),
                        cursor.getString(cursor.getColumnIndex(DBManager.colRemarks))));
            }while (cursor.moveToNext());
        }

        for (int i = 0; i < debtList.size(); i++) {
            if (debtList.get(i).type.equals("Borrowed")){
                totalBorrowed += Long.parseLong(debtList.get(i).amount);
            }
            else {
                totalOwed += Long.parseLong(debtList.get(i).amount);
            }
        }

        tvBorrowed.setText(String.valueOf(totalBorrowed));
        tvOwed.setText(String.valueOf(totalOwed));

        if (totalBorrowed>totalOwed){
            tvNetAmount.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.tvBorrowed));
            tvNetAmount.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.tvTextBorrowed));
        }
        else if (totalOwed>totalBorrowed){
            tvNetAmount.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.tvOwed));
            tvNetAmount.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.tvTextOwed));
        }
        else {
            tvNetAmount.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.tvNeutral));
            tvNetAmount.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.tvTextNeutral));
        }
        tvNetAmount.setText(String.valueOf(Math.abs(totalBorrowed-totalOwed)));

        debtAdapter = new DebtAdapter(debtList ,this);
        final ListView lvList = findViewById(R.id.lvList);
        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DebtData debtEdit = (DebtData) parent.getItemAtPosition(position);
                callEditDelItemDialog(debtEdit);
                return true;
            }
        });
        lvList.setAdapter(debtAdapter);
    }

    public void buAddData(View view) {
        callAddDataDialog();
    }

    public void callAddDataDialog(){

        final SentenceCase sentenceCase = new SentenceCase();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.dialog_add_debt);
        Objects.requireNonNull(addDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(addDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        addDialog.show();
        Button buAddData = addDialog.findViewById(R.id.buAddData);
        buAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = addDialog.findViewById(R.id.etName);
                EditText etAmount = addDialog.findViewById(R.id.etAmount);
                RadioGroup rgDebt = addDialog.findViewById(R.id.rgDebt);
                EditText etRemarks = addDialog.findViewById(R.id.etRemarks);
                String name = etName.getText().toString();
                name = sentenceCase.caseConvertCapWords(name);
                String amount = etAmount.getText().toString();
                String remarks = etRemarks.getText().toString();
                remarks = sentenceCase.caseConvertCapWords(remarks);
                int selID = rgDebt.getCheckedRadioButtonId();
                RadioButton rbDebt = addDialog.findViewById(selID);
                if(name.isEmpty() || amount.isEmpty()){
                    Toast.makeText(getApplicationContext(),"One or more required fields are empty",Toast.LENGTH_LONG).show();
                }
                else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBManager.colName, name );
                    contentValues.put(DBManager.colAmount, amount);
                    contentValues.put(DBManager.colType, rbDebt.getText().toString());
                    contentValues.put(DBManager.colRemarks, remarks);
                    long id = dbManager.insertDebtData(contentValues);
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

    public void callEditDelItemDialog(final DebtData debtEdit){
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
                callEditProductDialog(debtEdit);
                editDelDialog.dismiss();
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Delete Button",tvDelete.toString());
                long id =  dbManager.deleteDebtData(debtEdit.id);
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

    private void callEditProductDialog(@NonNull final DebtData debtEdit){

        final SentenceCase sentenceCase = new SentenceCase();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        final Dialog editDialog = new Dialog(this);
        editDialog.setContentView(R.layout.dialog_add_debt);
        Objects.requireNonNull(editDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Objects.requireNonNull(editDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        editDialog.show();
        final EditText etName = editDialog.findViewById(R.id.etName);
        final EditText etAmount = editDialog.findViewById(R.id.etAmount);
        final RadioGroup rgDebt = editDialog.findViewById(R.id.rgDebt);
        final EditText etRemarks = editDialog.findViewById(R.id.etRemarks);
        Button buAddData = editDialog.findViewById(R.id.buAddData);
        etName.setText(debtEdit.name);
        etAmount.setText(debtEdit.amount);
        if(debtEdit.type.equals("Owed")){
            RadioButton rbEdit = editDialog.findViewById(R.id.rbOwed);
            rbEdit.setChecked(true);
        }
        else {
            RadioButton rbEdit = editDialog.findViewById(R.id.rbBorrowed);
            rbEdit.setChecked(true);
        }
        etRemarks.setText(debtEdit.remarks);
        buAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                name = sentenceCase.caseConvertCapWords(name);
                String amount = etAmount.getText().toString();
                String remarks = etRemarks.getText().toString();
                remarks = sentenceCase.caseConvertCapWords(remarks);
                int selID = rgDebt.getCheckedRadioButtonId();
                RadioButton rbDebt = editDialog.findViewById(selID);
                if(name.isEmpty() || amount.isEmpty()){
                    Toast.makeText(getApplicationContext(),"One or more required fields are empty",Toast.LENGTH_LONG).show();
                }
                else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBManager.colName, name );
                    contentValues.put(DBManager.colAmount, amount);
                    contentValues.put(DBManager.colType, rbDebt.getText().toString());
                    contentValues.put(DBManager.colRemarks, remarks);
                    long id = dbManager.updateDebtData(contentValues,debtEdit.id);
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
