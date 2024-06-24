package com.example.chaindzadministration.Utils;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AppCompat extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateLang(languageManager.getLang());
    }
}
