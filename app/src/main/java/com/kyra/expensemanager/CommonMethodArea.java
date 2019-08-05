package com.kyra.expensemanager;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonMethodArea {

    DBManager dbManager;
    private Context context;

    public CommonMethodArea(Context context) {
        this.context = context;
    }

    public JSONArray getAllData(){
        dbManager = new DBManager(context);
        Cursor cursor = dbManager.getAllDataAPI();

        JSONArray resultSet = new JSONArray();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
//                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
//                        Log.d("TAG_NAME", e.getMessage()  );
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
//        Log.d("Json Length", String.valueOf(resultSet.length()));
        return resultSet;
    }

    public String getFileName() {
        String pattern = "yyyy-MM-dd_HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateString = simpleDateFormat.format(new Date());
        String fileName = "EMDATA_"+dateString+".csv";
        return fileName;
    }

    public void exportDB() {

        JSONArray jsonArray = getAllData();
        File exportDir = new File(Environment.getExternalStorageDirectory(), "/EMDATA");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir,getFileName());
        try
        {
            file.createNewFile();
            String csv = CDL.toString(jsonArray);
            FileUtils.writeStringToFile(file,csv);
            Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
        }
        catch(Exception sqlEx)
        {
            Log.d("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
    public String getCurrentYear(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(date);
    }

}
