package com.kyra.expensemanager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class DBManager {

    private SQLiteDatabase sqLiteDatabase;
    public static final String DBName = "ExpenseManager";
    public static final String colID = "ID";
    public static final String colRemarks = "Remarks";

    public static final String tableName = "Expense";
    public static final String colItem = "Item";
    public static final String colDate = "Date";
    public static final String colPrice = "Price";
    public static final String colCategory = "Category";
    public static final String colStatus = "Status";
    public static final String colCurrency = "Currency";

    public static final String tableUser = "User";
    public static final String colUser = "UserName";
    public static final String colPassword = "Password";
    public static final String colSecurityQuestion = "SecurityQuestion";
    public static final String colSecurityAnswer = "SecurityAnswer";
    public static final String colBankFlag = "BankFlag";
    public static final String colBankPassword = "BankPassword";

    public static final String tableBank = "Bank";
    public static final String colBankName = "BankName";
    public static final String colAccountNumber = "AccountNumber";
    public static final String colIFSCCOde = "IFSCCode";

    public static final String tableDebt = "Debt";
    public static final String colName = "Name";
    public static final String colAmount = "Amount";
    public static final String colType = "IFSCCode";

    private CommonMethodArea commonMethodArea;

    private static final int DBVersion = 1;

    private static final String createTable = "CREATE TABLE IF NOT EXISTS " + tableName + " ( "+ colID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + colItem + " TEXT NOT NULL, " + colDate +" DATE NOT NULL, " + colPrice + " INTEGER NOT NULL, "
        + colCategory + " TEXT NOT NULL, " + colRemarks + " TEXT NOT NULL, " + colStatus
        + " TEXT, " + colCurrency + " TEXT DEFAULT 'INR' );";

    private static final String createUserTable = "CREATE TABLE IF NOT EXISTS " + tableUser + " ( "+ colID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + colUser + " TEXT NOT NULL, " + colPassword +" TEXT NOT NULL, " + colSecurityQuestion +" INTEGER NOT NULL, " + colSecurityAnswer +" TEXT NOT NULL, " + colBankFlag + " BOOLEAN NOT NULL DEFAULT '0', "
            + colBankPassword + " TEXT NOT NULL DEFAULT '1234' );";

    private static final String createBankTable = "CREATE TABLE IF NOT EXISTS " + tableBank + " ( "+ colID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + colBankName + " TEXT NOT NULL, " + colAccountNumber +" INTEGER NOT NULL, " + colIFSCCOde + " TEXT NOT NULL, "
            + colRemarks + " TEXT NOT NULL);";

    private static final String createDebtTable = "CREATE TABLE IF NOT EXISTS " + tableDebt + " ( "+ colID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + colName + " TEXT NOT NULL, " + colAmount +" INTEGER NOT NULL, " + colType + " TEXT NOT NULL, "
            + colRemarks + " TEXT NOT NULL);";

    static class DBHelper extends SQLiteOpenHelper{
        Context context;
        public DBHelper(@Nullable Context context) {
            super(context, DBName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createTable);
            db.execSQL(createUserTable);
            db.execSQL(createDebtTable);
            db.execSQL(createBankTable);
            // Toast.makeText(context,"Tables are Created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public DBManager(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        commonMethodArea = new CommonMethodArea(context);
    }

    //List Data

    public long insertData(ContentValues contentValues){
        return sqLiteDatabase.insert(tableName,null,contentValues);
    }

    public Cursor fetchData(String[] projection,String selection,String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(tableName);
        return sqLiteQueryBuilder.query(sqLiteDatabase,projection,selection,selectionArgs,null,null,sortOrder + " DESC");
    }

    public long updateData(ContentValues contentValues, String id){
        return sqLiteDatabase.update(tableName,contentValues,"ID = ?" ,new String[]{id});
    }

    public long deleteData(String id){
        return sqLiteDatabase.delete(tableName,"ID = ?", new String[]{id});
    }

    public Cursor searchByDate(String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(tableName);
        return sqLiteQueryBuilder.query(sqLiteDatabase,projection,selection,selectionArgs,null,null,sortOrder);
    }

    public Cursor searchByMonth(String[] selectionArgs){
        Cursor cursor;
        String query= "SELECT *  FROM " + tableName+ " WHERE strftime('%m-%Y', " + colDate + ") = '" + selectionArgs[0] + "'";
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor searchByYear(String[] selectionArgs){
        Cursor cursor;
        String query= "SELECT *  FROM " + tableName+ " WHERE strftime('%Y', " + colDate + ") = '"+ selectionArgs[0]+"'";
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor searchByProduct(String[] selectionArgs){
        Cursor cursor;
        String query = "SELECT *  FROM " + tableName + " WHERE " + colItem + " = '"+ selectionArgs[0]+"'";
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor searchByPriceRange(String[] selectionArgs){
        Cursor cursor;
        String query = "SELECT * FROM " + tableName + " WHERE " + colPrice + " >= "+ selectionArgs[0] + " AND " + colPrice + " <= "+ selectionArgs[1];
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor searchAdvanced(String[] selectionArgs){
        Cursor cursor;
        String query= "SELECT *  FROM " + tableName+ " WHERE strftime('%m-%Y', " + colDate + ") = '"
                + selectionArgs[0] + "' AND " + colItem + " = '" + selectionArgs[1] + "'";
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor getSumCategoryDayWise(String selectionArgs){
        Cursor cursor;
        String query = " SELECT " + colCategory + " ,SUM(" + colPrice + ") FROM "
                + tableName + " WHERE " + colDate + " = '" + selectionArgs + "' GROUP BY " + colCategory + " ORDER BY SUM(Price) DESC";
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor getSumCategoryMonthWise(String selectionArgs){
        Cursor cursor;
        String query = " SELECT " + colCategory + " ,SUM(" + colPrice + ") FROM "
                + tableName + " WHERE strftime('%m-%Y', " + colDate + ") = '"+ selectionArgs + "-" + commonMethodArea.getCurrentYear() + "'" + " GROUP BY " + colCategory + " ORDER BY SUM(Price) DESC";
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor getSumCategoryYearWise(String selectionArgs){
        Cursor cursor;
        String query = " SELECT " + colCategory + " ,SUM(" + colPrice + ") FROM "
                + tableName + " WHERE strftime('%Y', " + colDate + ") = '"+ selectionArgs+"'" + " GROUP BY " + colCategory + " ORDER BY SUM(Price) DESC";
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    //User Data

    public String insertUserData(ContentValues contentValues){
        return String.valueOf(sqLiteDatabase.insert(tableUser,null,contentValues));
    }

    public String getUsername(){
        Cursor cursor;
        String username;
        String query = "SELECT " + colUser + " FROM " + tableUser + " WHERE " + colID + "=" + 1;
        cursor = sqLiteDatabase.rawQuery(query,null);
        if (cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex(colUser));
            return username;
        }
        return "";
    }

    public Cursor getCurrentUser(String id){
        Cursor cursor;
        String query = "SELECT " + colUser + "," + colPassword + "," + colBankPassword + " FROM " + tableUser + " WHERE " + colID + "=" + id;
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor getLoginUser(String username){
        Cursor cursor;
        String query = "SELECT " + colID + "," + colUser + "," + colPassword + " FROM " + tableUser + " WHERE " + colUser + "='" + username + "'";
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor forgetPassword(String username){
        Cursor cursor;
        String query = "SELECT " + colID + "," + colUser + "," + colSecurityQuestion + "," + colSecurityAnswer + " FROM " + tableUser + " WHERE " + colUser + "='" + username + "'";
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public Cursor getUserNames(){
        Cursor cursor;
        String query = "SELECT " + colUser + " FROM " + tableUser;
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public long changePassword(ContentValues contentValues,String id){
        return sqLiteDatabase.update(tableUser,contentValues,colID + " = ?" ,new String[]{id});
    }

    public long changeBankPassword(ContentValues contentValues,String id){
        return sqLiteDatabase.update(tableUser,contentValues,colID + " = ?" ,new String[]{id});
    }

    //Bank Data

    public long insertBankData(ContentValues contentValues){
        return sqLiteDatabase.insert(tableBank,null,contentValues);
    }

    public Cursor fetchBankData(String[] projection,String selection,String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(tableBank);
        return sqLiteQueryBuilder.query(sqLiteDatabase,projection,selection,selectionArgs,null,null,sortOrder);
    }

    public long updateBankData(ContentValues contentValues, String id){
        return sqLiteDatabase.update(tableBank,contentValues,"ID = ?" ,new String[]{id});
    }

    public long deleteBankData(String id){
        return sqLiteDatabase.delete(tableBank,"ID = ?", new String[]{id});
    }

    //Debt Data

    public long insertDebtData(ContentValues contentValues){
        return sqLiteDatabase.insert(tableDebt,null,contentValues);
    }

    public Cursor fetchDebtData(String[] projection,String selection,String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(tableDebt);
        return sqLiteQueryBuilder.query(sqLiteDatabase,projection,selection,selectionArgs,null,null,sortOrder);
    }

    public long updateDebtData(ContentValues contentValues, String id){
        return sqLiteDatabase.update(tableDebt,contentValues,"ID = ?" ,new String[]{id});
    }

    public long deleteDebtData(String id){
        return sqLiteDatabase.delete(tableDebt,"ID = ?", new String[]{id});
    }

    public Cursor getAllDataAPI(){
        Cursor cursor;
        String query = "SELECT " + colItem + "," + colDate + "," + colPrice + "," + colCategory + "," + colRemarks + " FROM " + tableName;
        cursor = sqLiteDatabase.rawQuery(query,null);
        return cursor;
    }

    public void clearExpenses(Context context) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.onCreate(sqLiteDatabase);
    }

}
