package com.amriksinghpadam.asyncdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "LOADER_DB";
    private static final String TABLE_NAME = "LOADER_TABLE";
    private static final int VERSION = 1;
    private static final String COL0 = "id";
    private static final String COL1 = "listitem";
    SQLiteDatabase db;

    MyDatabase(Context context){
        super(context,DB_NAME,null,VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COL0+" INTEGER PRIMARY KEY, "+COL1+" VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void insert(String item){

//        ContentValues values = new ContentValues();
//        values.put(COL1,item);
//        long result = db.insert(TABLE_NAME,null,values);
//
//        return result;
        db.execSQL("INSERT INTO " + TABLE_NAME + " ("+ COL1  + ") Values ('"+item+"')");
    }

    public Cursor readItem(){
        Cursor data = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return data;
    }
    public void deleteItem(){
        db.execSQL("DELETE FROM "+TABLE_NAME);
    }
}
