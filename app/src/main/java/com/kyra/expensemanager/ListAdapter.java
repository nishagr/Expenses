package com.kyra.expensemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    public ArrayList<ListData> listData ;
    public Context context;

    public ListAdapter(ArrayList<ListData>  listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public ListData getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(listData.get(position).id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint({"ViewHolder", "InflateParams"})
        View myView = layoutInflater.inflate(R.layout.item_list, null);

        final ListData listSingleData = listData.get(position);

//        TextView tvID = myView.findViewById(R.id.tvID);
//        tvID.setText(listSingleData.id);

        TextView tvItem = myView.findViewById(R.id.tvItem);
        tvItem.setText(listSingleData.item);

        TextView tvDate = myView.findViewById(R.id.tvDate);
        tvDate.setText(listSingleData.date);

        TextView tvPrice = myView.findViewById(R.id.tvPrice);
        tvPrice.setText(listSingleData.price);

        TextView tvRemarks = myView.findViewById(R.id.tvRemarks);
        tvRemarks.setText(listSingleData.remarks);

        return myView;
    }

}