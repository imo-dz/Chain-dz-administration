package com.example.chaindzadministration.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaindzadministration.Controllers.UserSingleton;
import com.example.chaindzadministration.R;
import com.google.firebase.auth.FirebaseAuth;

public class StockManagerHomeActivity extends AppCompatActivity {
    private CardView aboutUsCv;
    private CardView stockInCv;
    private CardView stockOutCv;
    private CardView changeLangCv;
    private CardView logoutCv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_manager_home);
        init();
    }
    private void init()
    {
        aboutUsCv = findViewById(R.id.about_us_cv);
        stockInCv = findViewById(R.id.organize_event_cv);
        stockOutCv = findViewById(R.id.reservation_cv);
        changeLangCv=findViewById(R.id.change_lang_cv);
        logoutCv = findViewById(R.id.logout_cv);
        changeLangCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putString("yes","yes");
                Intent intent = new Intent(getApplicationContext(),ChooseLanguageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        logoutCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                UserSingleton.getInstance().setUser(null);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        stockInCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StockInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        stockOutCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StockOutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}