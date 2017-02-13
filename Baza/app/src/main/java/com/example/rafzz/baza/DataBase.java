package com.example.rafzz.baza;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rafzz on 05.11.2016.
 */

public class DataBase extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "person";
    private static final String PK_NAME = "id";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String DB_NAME = "data.db";
    private static final String PATH = "path";
    private static final String BIRTH_DATE = "birthDate";


    public DataBase(Context context) {
        super(context, DB_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table " + TABLE_NAME + "(" +
                        PK_NAME + " integer primary key autoincrement," +
                        NAME + " text," +
                        AGE + " integer, " +
                        PATH + " text, " +
                        BIRTH_DATE + " text);" +
                        "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If you need to add a column
        if (newVersion > oldVersion) {

        }
    }


    public void addData(String name, int age, String path, String birth_date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(this.NAME, name);
        values.put(this.AGE, age);
        values.put(this.PATH, path);
        values.put(this.BIRTH_DATE, birth_date);
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    public Cursor writeAllData() {
        String[] column = {PK_NAME, NAME, AGE, PATH, BIRTH_DATE};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, column, null, null, null, null, null);
        return cursor;
    }

    public void removeAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public void updateData(int id, String name, int age, String path, String birth_date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(this.NAME, name);
        values.put(this.AGE, age);
        values.put(this.PATH, path);
        values.put(this.BIRTH_DATE, birth_date);
        String[] args = {"" + id};
        db.update(TABLE_NAME, values, PK_NAME + "=?", args);
    }

    public void removeData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {"" + id};
        db.delete(TABLE_NAME, PK_NAME + "=?", args);
    }
}
