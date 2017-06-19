package com.phacsin.educonn_school;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.phacsin.educonn_school.recyclerviewnotifications.DataModelNotification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by GD on 6/10/2017.
 */

public class DBHandler extends SQLiteOpenHelper {
    public DBHandler(Context context) {
        super(context, "student", null, 2);
    }

    public void onCreate(SQLiteDatabase db) {
        String TABLE_ACTIVE_NOTIFICATION = "CREATE TABLE notification(name TEXT,body TEXT,date TEXT)";
        db.execSQL(TABLE_ACTIVE_NOTIFICATION);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS notification");
        onCreate(database);
    }

    public void insertNotification (String name, String value,String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("body", value);
        contentValues.put("date", date);
        db.insert("notification", null, contentValues);
    }


    public void removeNotification (String name)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM notification WHERE name='"+name+"'";
            db.execSQL(query);
        }catch (SQLiteException e) {
            Log.d("sqlite",e.toString());
        }
    }

    public List<DataModelNotification> getAllNotifications()
    {
        List<DataModelNotification> list = new ArrayList<DataModelNotification>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * from notification", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            DataModelNotification details = new DataModelNotification();
            details.setName(res.getString(res.getColumnIndex("name")));
            details.setBody(res.getString(res.getColumnIndex("body")));
            details.setDate(res.getString(res.getColumnIndex("date")));
            list.add(details);
            res.moveToNext();
        }
        Collections.reverse(list);
        return list;
    }
}
