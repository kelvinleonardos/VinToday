package com.example.vintoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.example.vintoday.utils.LanguageUtils;
import com.example.vintoday.utils.Themes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SavedListActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    NewsAdapter newsAdapter;
    RecyclerView recyclerView;
    List<News> newsList = new ArrayList<>();
    DBControllers dbControllers;
    TextView nodata;

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

        getSupportActionBar().setTitle(R.string.saved_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("ThemePref", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("theme", "Default");
        Themes.applyTheme(currentTheme);

        String language = LanguageUtils.getSavedLanguage(this);
        LanguageUtils.setLocale(this, language);

        nodata = findViewById(R.id.s_no_data);

        newsAdapter = new NewsAdapter(newsList);
        recyclerView = findViewById(R.id.rv_saved_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);

        loadSavedNews();


    }

    void loadSavedNews() {
        newsList.clear();
        dbControllers = new DBControllers(this);
        ProgressBar progressBar = findViewById(R.id.pb_s);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        new Thread(() -> {
            assert firebaseUser != null;
            newsList.addAll(dbControllers.getAllNews(firebaseUser.getEmail()));
            runOnUiThread(() -> {
                newsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (newsList.isEmpty()) {
                    nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    nodata.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        }).start();
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