package com.kyra.expensemanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {

    public ArrayList<BankData> bankDataList;
    public Context context;
    public LayoutInflater layoutInflater;

    public BankAdapter(ArrayList<BankData> bankDataList,Context context) {
        this.context = context;
        this.bankDataList = bankDataList;
        layoutInflater =LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_bank,viewGroup,false);
        return new ViewHolder(view,bankDataList);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapter.ViewHolder viewHolder, int i) {
        final BankData bankData = bankDataList.get(i);
//        viewHolder.tvID.setText(bankData.id);
        viewHolder.tvBankName.setText(bankData.bankName);
        viewHolder.tvAccountNumber.setText(bankData.accountNumber);
        viewHolder.tvIFSCCode.setText(bankData.ifscCode);
        viewHolder.tvRemarks.setText(bankData.remarks);
    }

    @Override
    public int getItemCount() {
        return bankDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        TextView tvID;
        TextView tvBankName,tvAccountNumber,tvIFSCCode,tvRemarks;
        public DBManager dbManager;

        public ViewHolder(@NonNull final View itemView, final ArrayList<BankData> bankDataList) {
            super(itemView);

            dbManager = new DBManager(itemView.getContext());
//            tvID = itemView.findViewById(R.id.tvID);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            tvAccountNumber = itemView.findViewById(R.id.tvAccountNumber);
            tvIFSCCode = itemView.findViewById(R.id.tvIFSCCode);
            tvRemarks = itemView.findViewById(R.id.tvRemarks);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    BankData bankEdit = bankDataList.get(getLayoutPosition());
                    //int p = getLayoutPosition();
                    //Toast.makeText(itemView.getContext(),"Long Click = "+p,Toast.LENGTH_SHORT).show();
                    callEditDelItemDialog(bankEdit);
                    return true;
                }
            });
        }

        public void callEditDelItemDialog(final BankData bankEdit){
            int width = (int)(itemView.getContext().getResources().getDisplayMetrics().widthPixels*0.5);
            final Dialog editDelDialog = new Dialog(itemView.getContext());
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
//                    Log.d("Edit Button",tvEdit.toString());
                    callEditProductDialog(bankEdit);
                    editDelDialog.dismiss();
                }
            });
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("Delete Button",tvDelete.toString());
                    long id =  dbManager.deleteBankData(bankEdit.id);
                    if (id > 0) {
                        Toast.makeText(itemView.getContext(), "Data Deleted, Click on Refresh", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(itemView.getContext(), "Data Not Deleted", Toast.LENGTH_LONG).show();
                    }
                    editDelDialog.dismiss();
                }
            });
        }

        private void callEditProductDialog(@NonNull final BankData bankEdit){

            final SentenceCase sentenceCase = new SentenceCase();
            int width = (int)(itemView.getContext().getResources().getDisplayMetrics().widthPixels*0.95);
            final Dialog editDialog = new Dialog(itemView.getContext());
            editDialog.setContentView(R.layout.dialog_add_bank);
            Objects.requireNonNull(editDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Objects.requireNonNull(editDialog.getWindow()).setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            final EditText etBankName = editDialog.findViewById(R.id.etBankName);
            final EditText etAccountNumber = editDialog.findViewById(R.id.etAccountNumber);
            final EditText etIFSCCode = editDialog.findViewById(R.id.etIFSCCode);
            final EditText etRemarks = editDialog.findViewById(R.id.etBankRemarks);
            etBankName.setText(bankEdit.bankName);
            etAccountNumber.setText(bankEdit.accountNumber);
            etIFSCCode.setText(bankEdit.ifscCode);
            etRemarks.setText(bankEdit.remarks);
            editDialog.show();
            Button buAddBank = editDialog.findViewById(R.id.buAddBank);
            buAddBank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bankName = etBankName.getText().toString();
                    String accountNumber = etAccountNumber.getText().toString();
                    String ifscCode = etIFSCCode.getText().toString();
                    ifscCode = ifscCode.toUpperCase();
                    String remarks = etRemarks.getText().toString();
                    remarks = sentenceCase.caseConvertCapWords(remarks);
                    if(bankName.isEmpty() || accountNumber.isEmpty()){
                        Toast.makeText(itemView.getContext(),"One or more required fields are empty",Toast.LENGTH_LONG).show();
                    }
                    else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBManager.colBankName, bankName );
                        contentValues.put(DBManager.colAccountNumber, accountNumber);
                        contentValues.put(DBManager.colIFSCCOde, ifscCode);
                        contentValues.put(DBManager.colRemarks, remarks);
                        long id = dbManager.updateBankData(contentValues,bankEdit.id);
                        if (id > 0) {
                            Toast.makeText(itemView.getContext(), "Data Updated, Click on Refresh", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(itemView.getContext(), "Data Not Updated", Toast.LENGTH_LONG).show();
                        }
                        editDialog.dismiss();
                    }
                }
            });
        }
    }

}
