package com.smolda.god.sqlitetest;

/**
 * Created by god on 31.03.16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedRepo {
    private DBHelper dbHelper;

    public FeedRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Feed feed) {
        // TODO Auto-generated method stub
        Log.i("repo","insert "+feed.feed_ID);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Feed.KEY_ID,feed.feed_ID);
        values.put(Feed.KEY_title, feed.title);
        values.put(Feed.KEY_body,feed.body);
        values.put(Feed.KEY_top_image, feed.top_image);
        values.put(Feed.KEY_published, feed.published);
        values.put(Feed.KEY_is_important,feed.is_important);
        values.put(Feed.KEY_is_smolensk,feed.is_smolensk);



        long student_Id = -1;
        try {
            if (!has(feed.feed_ID)) {
                student_Id = db.insertOrThrow(Feed.TABLE, null, values);
            }
             // Closing database connection
        } catch (SQLiteConstraintException e) {
            Log.i("sql",e.toString());
            db.close();
            return -1;
        } catch(SQLiteException exception) {
            Log.i("error la inserare child", "on the next line");
            exception.printStackTrace();
        } catch(Exception exception) {
            //handle it: must handle it, don't just swallow it.
        }
        finally {
            db.close();
        }



        return (int) student_Id;
    }

    public void delete(int student_Id) {
        //int student_Id = getFirstStudent();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Feed.TABLE, Feed.KEY_ID + "=" + student_Id, null);
        db.close(); // Closing database connection
    }

    public boolean has(int id) {

        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(id);
        String strI = sb.toString();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] args = {strI};
        Cursor c = db.rawQuery("select * from " + Feed.TABLE + " where "+Feed.KEY_ID+" = ?", args);
        int count = c.getCount();
        Log.i("has", "count " + count);
        if (count == 1) {
            return true;
        }else {
            return false;
        }
    }

   /* public void update(Feed feed) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Student.KEY_age, student.age);
        values.put(Student.KEY_email,student.email);
        values.put(Student.KEY_name, student.name);

        db.update(Student.TABLE, values, Student.KEY_ID + "=" + student.student_ID, null);
        db.close(); // Closing database connection
    }*/

    public ArrayList getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Feed.KEY_ID + "," +
                Feed.KEY_title + "," +
                Feed.KEY_body + "," +
                Feed.KEY_top_image +
                Feed.KEY_published +
                " FROM " + Feed.TABLE;

        ArrayList feedList =new ArrayList();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                feedList.add(cursor.getString(cursor.getColumnIndex(Feed.KEY_ID)) + "_"
                        + cursor.getString(cursor.getColumnIndex(Feed.KEY_title)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return feedList;

    }

    public ArrayList<HashMap<String, String>>  getFeedList(Boolean smolensk_only,Boolean important_only) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String where = "";
        if (smolensk_only) {
            where = Feed.KEY_is_smolensk + " = 1";
        }
        if (important_only) {
            if (where == "") {
                where = Feed.KEY_is_important + " = 1 ";
            }else {
                where += " and "+ Feed.KEY_is_important + " = 1 ";
            }
        }
        if (where != "") {
            where = " where "+where;
        }
        String selectQuery =  "SELECT  " +
                Feed.KEY_ID + "," +
                Feed.KEY_title + "," +
                Feed.KEY_body + "," +
                Feed.KEY_top_image + "," +
                Feed.KEY_published +"," +
                Feed.KEY_is_important+","+
                Feed.KEY_is_smolensk+
                " FROM " + Feed.TABLE +
                where +
                " order by "+Feed.KEY_ID+ " desc limit 50";

        ArrayList<HashMap<String, String>> feedList = new ArrayList<HashMap<String, String>>();


        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> feed = new HashMap<String, String>();
                feed.put("id", cursor.getString(cursor.getColumnIndex(Feed.KEY_ID)));
                feed.put("title", cursor.getString(cursor.getColumnIndex(Feed.KEY_title)));

                feedList.add(feed);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return feedList;

    }

    public Feed getFeedById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Feed.KEY_ID + "," +
                Feed.KEY_title + "," +
                Feed.KEY_body + "," +
                Feed.KEY_top_image + "," +
                Feed.KEY_published +
                " FROM " + Feed.TABLE
                + " WHERE " +
                Feed.KEY_ID + "=" + Id;


        int iCount =0;
        Feed feed = new Feed();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                feed.feed_ID =cursor.getInt(cursor.getColumnIndex(Feed.KEY_ID));
                feed.title =cursor.getString(cursor.getColumnIndex(Feed.KEY_title));
                feed.body  =cursor.getString(cursor.getColumnIndex(Feed.KEY_body));
                feed.top_image =cursor.getString(cursor.getColumnIndex(Feed.KEY_top_image));
                feed.published =cursor.getString(cursor.getColumnIndex(Feed.KEY_published));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return feed;
    }

    private int getFirstFeed(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Feed.KEY_ID +
                " FROM " + Feed.TABLE
                + " LIMIT 1;" ;


        int feed_Id =0;


        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                feed_Id =cursor.getInt(cursor.getColumnIndex(Feed.KEY_ID));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return feed_Id;
    }

    private int getLastFeed(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Feed.KEY_ID +
                " FROM " + Feed.TABLE
                + " ORDER BY " +
                Feed.KEY_ID + " DESC ";
        //" DESC LIMIT 1;" ;


        int feed_Id =0;


        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                feed_Id =cursor.getInt(cursor.getColumnIndex(Feed.KEY_ID));
                break;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return feed_Id;
    }
}
