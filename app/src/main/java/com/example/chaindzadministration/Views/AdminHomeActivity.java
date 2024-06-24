package com.example.chaindzadministration.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaindzadministration.Controllers.UserSingleton;
import com.example.chaindzadministration.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {
    private CardView addProductionCv;
    private CardView addStockCv;
    private CardView addClaimCv;
    private CardView manageUsersCv;
    private CardView browseProductsCv;
    private CardView changeLangCv;
    private CardView logoutCv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        init();
    }
    private void init()
    {
        // Initialize the CardViews
        addProductionCv = findViewById(R.id.add_production_cv);
        addStockCv = findViewById(R.id.add_stock_cv);
        addClaimCv = findViewById(R.id.add_claim_cv);
        manageUsersCv = findViewById(R.id.manage_users_cv);
        browseProductsCv = findViewById(R.id.browse_products_cv);
        changeLangCv = findViewById(R.id.change_lang_cv);
        logoutCv = findViewById(R.id.logout_cv);

        // Add click listeners or any other logic here
        addProductionCv.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddStockProductionActivity.class);
            intent.putExtra("type","p");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        addStockCv.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddStockProductionActivity.class);
            intent.putExtra("type","s");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        addClaimCv.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddClaimActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        manageUsersCv.setOnClickListener(v -> {
            // Handle manage users click
        });

        browseProductsCv.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),ManageProductsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        changeLangCv.setOnClickListener(v -> {
            Bundle bundle =new Bundle();
            bundle.putString("yes","yes");
            Intent intent = new Intent(getApplicationContext(),ChooseLanguageActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        logoutCv.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            UserSingleton.getInstance().setUser(null);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }
}