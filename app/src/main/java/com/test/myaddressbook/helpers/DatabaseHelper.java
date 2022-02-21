package com.test.myaddressbook.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "employee", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create Table employee(" +
                "id INTEGER primary key," +
                "name TEXT," +
                "city TEXT," +
                "phone TEXT," +
                "email TEXT," +
                "picture TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS employee");
    }

    public boolean insertData(int id, String name, String city, String phone, String email, String picture){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("city", city);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("picture", picture);

        long result = db.insert("employee", null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }

    public Cursor getEmployee() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM employee", null);
        return result;
    }
}
