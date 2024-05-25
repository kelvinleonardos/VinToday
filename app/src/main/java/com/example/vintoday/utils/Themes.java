package com.example.vintoday.utils;

import androidx.appcompat.app.AppCompatDelegate;

public class Themes {

    public static void applyTheme(String theme) {
        switch (theme) {
            case "Light":
            case "Terang":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Dark":
            case "Gelap":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "Default":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

}
