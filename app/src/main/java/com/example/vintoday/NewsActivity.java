package com.example.vintoday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vintoday.db.DBControllers;
import com.example.vintoday.models.News;
import com.example.vintoday.utils.LanguageUtils;
import com.example.vintoday.utils.Themes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    TextView titleTextView, authorTextView, contentTextView;
    ImageView newsImageView;
    ImageButton btnsave;
    DBControllers dbControllers;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("ThemePref", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("theme", "Default");
        Themes.applyTheme(currentTheme);

        String language = LanguageUtils.getSavedLanguage(this);
        LanguageUtils.setLocale(this, language);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setTitle(R.string.read_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        News news = intent.getParcelableExtra("news");

        titleTextView = findViewById(R.id.tv_title);
        authorTextView = findViewById(R.id.tv_author);
        newsImageView = findViewById(R.id.iv_news);
        contentTextView = findViewById(R.id.tv_content);
        btnsave = findViewById(R.id.save_btn);

        titleTextView.setText(news.getTitle());
        if (news.getAuthor() != null) {
            authorTextView.setText(news.getAuthor() + " | " + news.getPublishedAt());
        } else {
            authorTextView.setText(news.getPublishedAt());
        }
        if (news.getUrlToImage() != null && !news.getUrlToImage().isEmpty()) {
            Picasso.get().load(news.getUrlToImage()).into(newsImageView);
        } else {
            newsImageView.setImageResource(R.drawable.no_image);
        }
        contentTextView.setText(news.getSource() == null ? news.getContent() : news.getSource() + " - " + news.getContent());

        btnsave.setOnClickListener(v -> {
            dbControllers = new DBControllers(NewsActivity.this);
            assert firebaseUser != null;
            dbControllers.addNews(news, firebaseUser.getEmail());
        });

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