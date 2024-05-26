package com.example.vintoday.db;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vintoday.models.News;
import com.example.vintoday.models.Source;
import com.google.gson.Gson;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DBControllers {

    private DBConfig dbHelper;
    private Gson gson;
    private Context context;

    public DBControllers(Context context) {
        dbHelper = new DBConfig(context);
        gson = new Gson();
        this.context = context;
    }

    public void addNews(News news, String email) {
        List<News> newsList = getAllNews();
        for (News n : newsList) {
            if (n.getTitle().equals(news.getTitle())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error");
                builder.setMessage("News already exists");
                builder.setPositiveButton("OK", null);
                builder.show();
                return;
            }
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBConfig.COL_SOURCE_ID, gson.toJson(news.getSource()));
        values.put(DBConfig.COL_AUTHOR, news.getAuthor());
        values.put(DBConfig.COL_TITLE, news.getTitle());
        values.put(DBConfig.COL_DESCRIPTION, news.getDescription());
        values.put(DBConfig.COL_URL, news.getUrl());
        values.put(DBConfig.COL_URL_TO_IMAGE, news.getUrlToImage());
        values.put(DBConfig.COL_PUBLISHED_AT, news.getPublishedAt());
        values.put(DBConfig.COL_CONTENT, news.getContent());
        values.put(DBConfig.COL_EMAIL, email);

        db.insert(DBConfig.TABLE_NAME, null, values);
        db.close();

        String sourceJson = gson.toJson(news.getSource());

        News firestoreNews = new News();
        firestoreNews.setSource(new Source());
        firestoreNews.getSource().setId(news.getSource().getId());
        firestoreNews.getSource().setName(sourceJson);
        firestoreNews.setAuthor(news.getAuthor());
        firestoreNews.setTitle(news.getTitle());
        firestoreNews.setDescription(news.getDescription());
        firestoreNews.setUrl(news.getUrl());
        firestoreNews.setUrlToImage(news.getUrlToImage());
        firestoreNews.setPublishedAt(news.getPublishedAt());
        firestoreNews.setContent(news.getContent());

        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
        firestoreDb.collection("news").add(firestoreNews);
    }

    @SuppressLint("Range")
    public List<News> getAllNews() {
        return getAllNews(null);
    }

    @SuppressLint("Range")
    public List<News> getAllNews(String email) {
        List<News> newsList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        if (email == null) {
            cursor = db.query(DBConfig.TABLE_NAME, null, null, null, null, null, null);
        } else {
            cursor = db.query(DBConfig.TABLE_NAME, null, DBConfig.COL_EMAIL + "=?", new String[]{email}, null, null, null);
        }

        if (cursor.moveToFirst()) {
            do {
                News news = new News();
                news.setSource(gson.fromJson(cursor.getString(cursor.getColumnIndex(DBConfig.COL_SOURCE_ID)), Source.class));
                news.setAuthor(cursor.getString(cursor.getColumnIndex(DBConfig.COL_AUTHOR)));
                news.setTitle(cursor.getString(cursor.getColumnIndex(DBConfig.COL_TITLE)));
                news.setDescription(cursor.getString(cursor.getColumnIndex(DBConfig.COL_DESCRIPTION)));
                news.setUrl(cursor.getString(cursor.getColumnIndex(DBConfig.COL_URL)));
                news.setUrlToImage(cursor.getString(cursor.getColumnIndex(DBConfig.COL_URL_TO_IMAGE)));
                news.setPublishedAt(cursor.getString(cursor.getColumnIndex(DBConfig.COL_PUBLISHED_AT)));
                news.setContent(cursor.getString(cursor.getColumnIndex(DBConfig.COL_CONTENT)));

                newsList.add(news);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return newsList;
    }

    public void deleteNews(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBConfig.TABLE_NAME, DBConfig.COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}