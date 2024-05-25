package com.example.vintoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vintoday.utils.LanguageUtils;
import com.example.vintoday.utils.Themes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    TextView tv_name, tv_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setTitle(R.string.profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("ThemePref", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("theme", "Default");
        Themes.applyTheme(currentTheme);

        String language = LanguageUtils.getSavedLanguage(this);
        LanguageUtils.setLocale(this, language);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        tv_name = findViewById(R.id.profile_name);
        tv_email = findViewById(R.id.profile_email);

        assert firebaseUser != null;
        tv_name.setText(firebaseUser.getDisplayName());
        tv_email.setText(firebaseUser.getEmail());

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