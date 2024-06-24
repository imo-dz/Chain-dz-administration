package com.example.chaindzadministration.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context ctx;
    private SharedPreferences sharedPreferences;
    public LanguageManager(Context ctx) {
        this.ctx = ctx;
        sharedPreferences=ctx.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }
    public void updateLang(String code)
    {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ctx.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale=locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        setLang(code);
    }


    public String getLang()
    {
        return sharedPreferences.getString("lang","en");
    }
    public void setLang(String code)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",code);
        editor.commit();
    }
}
