package com.example.culpe.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
//schoolName	city	stateAbr	schoolUrl	satAvg

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "colleges.db";
    public static final String TABLE_NAME = "college_table";
    public static final String col_0 = "id";
    public static final String col_1 = "schoolName";
    public static final String col_2= "city";
    public static final String col_3 = "stateAbr";
    public static final String col_4 = "schoolUrl";
    public static final String col_5 = "satAvg";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME  + " (id INTEGER PRIMARY KEY AUTOINCREMENT,schoolName TEXT , city TEXT,stateAbr TEXT,schoolUrl TEXT,satAvg TEXT )");
    }
    //CREATE TABLE "Colleges" ("schoolName" TEXT PRIMARY KEY, "city" TEXT, "stateAbr" TEXT, "schoolUrl" TEXT, "satAvg" TEXT);

    //possible call oncreate each time
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String schoolName,String city,String stateAbr,String schoolUrl,String satAvg)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,schoolName);
        contentValues.put(col_2,city);
        contentValues.put(col_3,stateAbr);
        contentValues.put(col_4,schoolUrl);
        contentValues.put(col_5,satAvg);

        db.insert(TABLE_NAME,null,contentValues);

        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result == -1)
            return false;
        else
            return  true;
    }
    public Cursor getDataNoSatAvg(String search)
    {
        // + " where stateAbr = " + search,
       search = search.toUpperCase();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id%2 =0 and stateAbr = \""+search+"\"",null);
        return res;
    }
    public Cursor getDataWithSatAvg(String search,int min,int max)
    {
        search = search.toUpperCase();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id%2 =0 and stateAbr = \"" + search + "\" and satAvg != \"NULL\" and satAvg between '" + min +"' and '" + max + "' order by satAvg asc",null);

        return res;
    }
}

