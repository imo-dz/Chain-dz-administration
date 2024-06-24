package com.example.chaindzadministration.Views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaindzadministration.R;


import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaindzadministration.Controllers.UserSingleton;
import com.example.chaindzadministration.Models.User;
import com.example.chaindzadministration.R;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.chaindzadministration.Utils.AppCompat;
import com.example.chaindzadministration.Utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompat {
    private ImageView logoImageView ;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logoImageView = findViewById(R.id.logo_splash);
        logoImageView.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.reefind_txt_animation);
        TextView textView = findViewById(R.id.under_logo_tv);
        textView.startAnimation(animation2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(auth.getCurrentUser()!=null)
                {
                    //user loged in

                    db.collection(Constants.USERS_COLLECTION).document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            UserSingleton.getInstance().setUser(user);
                            chooseActivity();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar snackbar = Snackbar
                                    .make(findViewById(R.id.splash_screen_layout),
                                            String.valueOf(getResources().getString(R.string.something_wrong_happened)), Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.red));
                            snackbar.show();
                        }
                    });
                }else
                {
                    //user not loged in
                    Bundle bundle =new Bundle();
                    bundle.putString("yes","no");
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
                    boolean retrievedValue = sharedPreferences.getBoolean("done", false);
                    if(retrievedValue)
                    {
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }else
                    {
                        Intent intent = new Intent(MainActivity.this,ChooseLanguageActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }

                }

            }
        },1500);

    }
    private void chooseActivity()
    {
        User user = UserSingleton.getInstance().getUser();
        Intent intent;

        if(user.getAccountType().equals(Constants.ACCOUNT_TYPE_ADMIN))
        {
            intent=new Intent(MainActivity.this,AdminHomeActivity.class);
        }else if((user.getAccountType().equals(Constants.ACCOUNT_TYPE_CLAIM_MANAGER)))
        {
            intent=new Intent(MainActivity.this,ClaimManagerHomeActivity.class);
        }else if((user.getAccountType().equals(Constants.ACCOUNT_TYPE_PRODUCTION_MANGER)))
        {
            intent=new Intent(MainActivity.this,ProductionManagerHomeActivity.class);
        }
        else if((user.getAccountType().equals(Constants.ACCOUNT_TYPE_STOCK_MANAGER)))
        {
            intent=new Intent(MainActivity.this,StockManagerHomeActivity.class);
        }
        else{
            intent = new Intent(MainActivity.this, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}