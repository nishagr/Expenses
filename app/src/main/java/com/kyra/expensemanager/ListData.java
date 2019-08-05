package com.kyra.expensemanager;


public class ListData {
    public String id,item,date,price,category,remarks,status,currency;

    public ListData(String id, String item, String date, String price, String category, String remarks) {
        this.id = id;
        this.item = item;
        this.date = date;
        this.price = price;
        this.category = category;
        this.remarks = remarks;
    }

    public String getPrice() {
        return price;
    }
}
