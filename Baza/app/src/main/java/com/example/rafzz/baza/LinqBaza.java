package com.example.rafzz.baza;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rafzz on 05.11.2016.
 */

public class LinqBaza extends SQLiteOpenHelper{

    private static final String table_name = "osoby";
    private static final String pk_name = "nr";
    private static final String name = "imie";
    private static final String age = "wiek";
    private static final String db_name = "dane.db";
    private static final String path = "sciezka";

    public LinqBaza(Context context) {
        super(context, db_name, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table "+ table_name +"(" +
                        pk_name+" integer primary key autoincrement," +
                        name+" text," +
                        age+" integer);" +
                        "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If you need to add a column
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE osoby ADD COLUMN sciezka text");
        }
    }



    public void addData(String name,int age,String path){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(this.name, name);
        values.put(this.age,age);
        values.put(this.path,path);
        db.insertOrThrow(table_name,null, values);
    }

    public Cursor writeAllData(){
        String[] column={pk_name,name,age,path};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =db.query(table_name,column,null,null,null,null,null);
        return cursor;
    }

    public void removeAllData(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table_name, null, null);
    }

    public void updateData(int id, String name, int age,String path){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(this.name, name);
        values.put(this.age, age);
        values.put(this.path,path);
        String[] args={""+id};
        db.update(table_name, values,pk_name+"=?",args);
    }

    public void removeData(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] args={""+id};
        db.delete(table_name, pk_name+"=?", args);
    }
}
