package com.kyra.expensemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DashboardAdapter extends BaseAdapter {


    public ArrayList<DashboardData> dashboardList ;
    public Context context;

    public DashboardAdapter(ArrayList<DashboardData>  dashboardList, Context context) {
        this.dashboardList = dashboardList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return dashboardList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint({"ViewHolder", "InflateParams"})
        View myView = layoutInflater.inflate(R.layout.item_dashboard, null);

        final DashboardData dashboardSingleData = dashboardList.get(position);

        TextView tvCategory = myView.findViewById(R.id.tvCategory);
        tvCategory.setText(dashboardSingleData.category);

        TextView tvAmount = myView.findViewById(R.id.tvAmount);
        tvAmount.setText(dashboardSingleData.amount);

        return myView;
    }
}
