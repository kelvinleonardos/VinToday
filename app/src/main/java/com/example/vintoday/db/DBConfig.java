package com.example.vintoday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBConfig extends SQLiteOpenHelper {

    public static final String DB_NAME = "news.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_NAME = "news";
    public static final String COL_ID = "id";
    public static final String COL_SOURCE_ID = "source_id";
    public static final String COL_SOURCE_NAME = "source_name";
    public static final String COL_AUTHOR = "author";
    public static final String COL_TITLE = "title";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_URL = "url";
    public static final String COL_URL_TO_IMAGE = "url_to_image";
    public static final String COL_PUBLISHED_AT = "published_at";
    public static final String COL_CONTENT = "content";
    public static final String COL_EMAIL = "EMAIL";
    


    public DBConfig(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBConfig(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NEWS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_SOURCE_ID + " TEXT,"
                + COL_SOURCE_NAME + " TEXT,"
                + COL_AUTHOR + " TEXT,"
                + COL_TITLE + " TEXT,"
                + COL_DESCRIPTION + " TEXT,"
                + COL_URL + " TEXT,"
                + COL_URL_TO_IMAGE + " TEXT,"
                + COL_PUBLISHED_AT + " TEXT,"
                + COL_CONTENT + " TEXT,"
                + COL_EMAIL + " TEXT" + ")";
        db.execSQL(CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
