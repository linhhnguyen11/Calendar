package com.example.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "MyCalendar.db";
    public static final String CONTACTS_TABLE_NAME = "events";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_TITLE = "title";
    public static final String CONTACTS_COLUMN_DAYSTART = "daystart";
    public static final String CONTACTS_COLUMN_DAYEND = "dayend";
    public static final String CONTACTS_COLUMN_TIMESTART = "timestart";
    public static final String CONTACTS_COLUMN_TIMEEND = "timeend";
    private HashMap hp;

    DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table events " +
                        "(id integer primary key , title text,daystart date,dayend date, timestart time,timeend time)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DROP TABLE IF EXISTS events;";
        db.execSQL(sql);
        db.close();
    }
    public boolean insertEvent (String title, String datestart, String timestart, String dateend,String timeend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("daystart", datestart);
        contentValues.put("timestart", timestart);
        contentValues.put("dayend", dateend);
        contentValues.put("timeend", timeend);
        long result = db.insert("events", null, contentValues);
        if (result > 0) {
            // Thao tác đã thành công
            Log.d("INSERT", "Data inserted successfully");
        } else {
            // Thao tác không thành công
            Log.d("INSERT", "Data insertion failed");
        }
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from events where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public void updateEvent (String id, String title, String datestart, String timestart, String dateend,String timeend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("daystart", datestart);
        contentValues.put("timestart", timestart);
        contentValues.put("dayend", dateend);
        contentValues.put("timeend", timeend);
        long result = db.update("events", contentValues, "id = ? ", new String[] { id } );
        if(result == -1 ){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("Success");
        }
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("events",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    Cursor readAllData(String dateChoice) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query =  "SELECT * FROM events WHERE daystart <= '" + dateChoice + "' AND dayend >= '" + dateChoice + "';";;
        Cursor res = null;
        if(db != null) {
            res = db.rawQuery(query, null);
        }
        return res;
    };

    Cursor readAllDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query =  "SELECT daystart, dayend FROM events ";
        Cursor res = null;
        if(db != null) {
            res = db.rawQuery(query, null);
        }
        return res;
    }

//    @SuppressLint("Range")
//    public ArrayList<String> getEventsByDay(String dateChoice) {
//        ArrayList<String> array_list = new ArrayList<String>();
//        List<Event> recordList = new ArrayList<Event>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query =  "SELECT * FROM events WHERE daystart <= '" + dateChoice + "' AND dayend >= '" + dateChoice + "';";;
//        Cursor res =  db.rawQuery( query, null );
//
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TITLE)));
//            res.moveToNext();
//        }
//        return array_list;
//    }

}
