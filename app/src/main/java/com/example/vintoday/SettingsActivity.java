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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vintoday.utils.LanguageUtils;
import com.example.vintoday.utils.Themes;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private boolean isLanguageSpinnerInitialized = false;
    private boolean isThemeSpinnerInitialized = false;

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

        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("ThemePref", MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString("theme", "Default");
        Themes.applyTheme(currentTheme);

        String currentLanguage = LanguageUtils.getSavedLanguage(this);
        LanguageUtils.setLocale(this, currentLanguage);

        setupLanguageSpinner(currentLanguage);
        setupThemeSpinner(currentTheme);
    }

    private void setupLanguageSpinner(String currentLanguage) {
        Spinner languageSpinner = findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        String currentLanguageDisplay = getLanguageDisplayName(currentLanguage);
        int langSpinnerPosition = languageAdapter.getPosition(currentLanguageDisplay);
        languageSpinner.setSelection(langSpinnerPosition);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isLanguageSpinnerInitialized) {
                    isLanguageSpinnerInitialized = true;
                } else {
                    String selectedLanguage = parent.getItemAtPosition(position).toString();
                    String newLanguageCode = getLanguageCode(selectedLanguage);
                    if (!currentLanguage.equals(newLanguageCode)) {
                        LanguageUtils.setLocale(SettingsActivity.this, newLanguageCode);
                        recreate();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no language selected
            }
        });
    }

    private void setupThemeSpinner(String currentTheme) {
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
                if (!isThemeSpinnerInitialized) {
                    isThemeSpinnerInitialized = true;
                } else {
                    String selectedTheme = parent.getItemAtPosition(position).toString();
                    if (!selectedTheme.equals(currentTheme)) {
                        Themes.applyTheme(selectedTheme);
                        saveThemeInPref(selectedTheme);
                        recreate();
                    }
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

    private String getLanguageDisplayName(String languageCode) {
        switch (languageCode) {
            case "en":
                return "English";
            case "in":
                return "Indonesia";
            default:
                return "Unknown";
        }
    }

    private String getLanguageCode(String languageDisplayName) {
        switch (languageDisplayName) {
            case "English":
                return "en";
            case "Indonesia":
                return "in";
            default:
                return "en";
        }
    }
}