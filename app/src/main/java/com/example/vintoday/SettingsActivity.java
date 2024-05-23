package com.example.vintoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vintoday.utils.Themes;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setTitle("Settings");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("ThemePref", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("theme", "Default");
        Themes.applyTheme(currentTheme);

        Spinner languageSpinner = findViewById(R.id.language_spinner);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                // Handle language change
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no language selected
            }
        });

        Spinner themeSpinner = findViewById(R.id.theme_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.themes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(currentTheme);
        themeSpinner.setSelection(spinnerPosition);
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTheme = parent.getItemAtPosition(position).toString();
                if (selectedTheme.equals("Light")) {
                    Themes.applyTheme(selectedTheme);
                    saveThemeInPref(selectedTheme);
                } else if (selectedTheme.equals("Dark")) {
                    Themes.applyTheme(selectedTheme);
                    saveThemeInPref(selectedTheme);
                } else if (selectedTheme.equals("Default")) {
                    Themes.applyTheme(selectedTheme);
                    saveThemeInPref(selectedTheme);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no theme selected
            }
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
    private void saveThemeInPref(String theme) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("theme", theme);
        editor.apply();
    }
}