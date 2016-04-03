package com.smolda.god.sqlitetest;

/**
 * Created by god on 31.03.16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "crud.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + Feed.TABLE  + "("
                + Feed.KEY_ID  + " INTEGER PRIMARY KEY ,"
                + Feed.KEY_title + " TEXT, "
                + Feed.KEY_body + " TEXT, "
                + Feed.KEY_top_image + " TEXT, "
                + Feed.KEY_published + " TEXT, "
                + Feed.KEY_is_important + " INTEGER, "
                + Feed.KEY_is_smolensk + " INTEGER"
                + " )";

        db.execSQL(CREATE_TABLE_STUDENT);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + Feed.TABLE);

        // Create tables again
        onCreate(db);

    }

}

