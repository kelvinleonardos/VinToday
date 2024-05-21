package com.example.vintoday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBConfig extends SQLiteOpenHelper {

    private static final String DB_NAME = "news.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "news";
    private static final String COL_ID = "id";
    private static final String COL_SOURCE_ID = "source_id";
    private static final String COL_SOURCE_NAME = "source_name";
    private static final String COL_AUTHOR = "author";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_URL = "url";
    private static final String COL_URL_TO_IMAGE = "url_to_image";
    private static final String COL_PUBLISHED_AT = "published_at";
    private static final String COL_CONTENT = "content";
    private static final String COL_IS_UPLOADED = "is_uploaded";


    public DBConfig(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
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
                + COL_IS_UPLOADED + " TEXT" + ")";
        db.execSQL(CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
