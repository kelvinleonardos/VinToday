package com.example.vintoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vintoday.db.DBControllers;
import com.example.vintoday.models.News;

import java.util.ArrayList;
import java.util.List;

import com.example.vintoday.recyclerview.NewsAdapter;
import com.example.vintoday.utils.Themes;

public class SavedListActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    NewsAdapter newsAdapter;
    RecyclerView recyclerView;
    List<News> newsList = new ArrayList<>();
    DBControllers dbControllers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_saved_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setTitle("Saved News");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("ThemePref", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("theme", "Default");
        Themes.applyTheme(currentTheme);

        newsAdapter = new NewsAdapter(newsList);
        recyclerView = findViewById(R.id.rv_saved_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);

        loadSavedNews();

    }

    void loadSavedNews() {
        newsList.clear();
        dbControllers = new DBControllers(this);
        newsList.addAll(dbControllers.getAllNews());
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}