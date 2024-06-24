package com.example.chaindzadministration.Views;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.AppCompat;
import com.example.chaindzadministration.Utils.LanguageManager;


public class ChooseLanguageActivity extends AppCompat {
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioGroup radioGroup;
    private Button nextBtn;
    private String selectedLang;
    private SharedPreferences sharedPreferences;
    private LanguageManager languageManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        init();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.enRBtn) {
                    radioButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uk, 0, R.drawable.checkmark, 0);
                    radioButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fr, 0, 0, 0);
                    radioButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dz, 0, 0, 0);
                    selectedLang="en";
                    languageManager.setLang(selectedLang);
                } else if (checkedId == R.id.frRBtn) {
                    radioButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uk, 0, 0, 0);
                    radioButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fr, 0, R.drawable.checkmark, 0);
                    radioButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dz, 0, 0, 0);
                    selectedLang="fr";
                    languageManager.setLang(selectedLang);
                } else if (checkedId == R.id.arRBtn) {
                    radioButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uk, 0, 0, 0);
                    radioButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fr, 0, 0, 0);
                    radioButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dz, 0, R.drawable.checkmark, 0);
                    selectedLang="ar";
                    languageManager.setLang(selectedLang);
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.setLang(selectedLang);
                Bundle b = getIntent().getExtras();
                String yes = b.getString("yes");
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("done",true);
                editor.apply();
                if(yes.equals("yes"))
                {
                    startActivity(new Intent(ChooseLanguageActivity.this,MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else
                {
                    startActivity(new Intent(ChooseLanguageActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });
    }
    private void init()
    {
        radioGroup = findViewById(R.id.languageRadioGroup);
        radioButton1 = findViewById(R.id.enRBtn);
        radioButton2 = findViewById(R.id.frRBtn);
        radioButton3 = findViewById(R.id.arRBtn);
        nextBtn = findViewById(R.id.lNextBtn);
        languageManager = new LanguageManager(this);
        selectedLang=languageManager.getLang();
        if(selectedLang.equals("en"))
        {
            radioButton1.setChecked(true);
            radioButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uk, 0, R.drawable.checkmark, 0);
            radioButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fr, 0, 0, 0);
            radioButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dz, 0, 0, 0);
        } else if (selectedLang.equals("fr"))
        {
            radioButton2.setChecked(true);
            radioButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uk, 0, 0, 0);
            radioButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fr, 0, R.drawable.checkmark, 0);
            radioButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dz, 0, 0, 0);
        }else
        {
            radioButton3.setChecked(true);
            radioButton1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.uk, 0, 0, 0);
            radioButton2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fr, 0, 0, 0);
            radioButton3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dz, 0, R.drawable.checkmark, 0);
        }
    }
}