package com.kyra.expensemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DebtAdapter extends BaseAdapter {
    public ArrayList<DebtData> debtList ;
    public Context context;

    public DebtAdapter(ArrayList<DebtData>  debtList, Context context) {
        this.debtList = debtList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return debtList.size();
    }

    @Override
    public DebtData getItem(int position) {
        return debtList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(debtList.get(position).id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint({"ViewHolder", "InflateParams"})
        View myView = layoutInflater.inflate(R.layout.item_debt, null);

        final DebtData debtSingleData = debtList.get(position);

//        TextView tvID = myView.findViewById(R.id.tvID);
//        tvID.setText(debtSingleData.id);

        TextView tvName = myView.findViewById(R.id.tvName);
        tvName.setText(debtSingleData.name);

        TextView tvAmount = myView.findViewById(R.id.tvAmount);
        tvAmount.setText(debtSingleData.amount);

        TextView tvType = myView.findViewById(R.id.tvType);
        tvType.setText(debtSingleData.type);
        if (debtSingleData.type.equals("Borrowed")){
            tvType.setTextColor(Color.parseColor("#FF0000"));
        }
        else {
            tvType.setTextColor(Color.parseColor("#3CB44B"));
        }

        TextView tvRemarks = myView.findViewById(R.id.tvRemarks);
        tvRemarks.setText(debtSingleData.remarks);

        return myView;
    }

}