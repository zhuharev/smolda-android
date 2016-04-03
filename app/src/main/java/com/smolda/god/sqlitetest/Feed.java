package com.smolda.god.sqlitetest;

/**
 * Created by god on 31.03.16.
 */

public class Feed {
    // Labels table name
    public static final String TABLE = "Feed";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_title = "title";
    public static final String KEY_body = "body";
    public static final String KEY_top_image = "top_image";
    public static final String KEY_published = "published";
    public static final String KEY_is_smolensk = "is_smolensk";
    public static final String KEY_is_important = "is_important";

    public int feed_ID;
    public String title;
    public String body;
    public String top_image;
    public String published;
    public Boolean is_smolensk;
    public Boolean is_important;



}
