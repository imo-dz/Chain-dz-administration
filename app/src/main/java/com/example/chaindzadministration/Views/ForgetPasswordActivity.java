package com.example.chaindzadministration.Views;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.chaindzadministration.Controllers.LoadingDialog;
import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.AppCompat;
import com.example.chaindzadministration.Utils.InputValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompat {
    private TextInputLayout forgetPassEmail;
    private AppCompatButton sendBtn;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        auth=FirebaseAuth.getInstance();
        forgetPassEmail = findViewById(R.id.email_forget_pass);
        sendBtn =findViewById(R.id.register_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPassEmail.setError(null);
                String email = forgetPassEmail.getEditText().getText().toString().trim();
                LoadingDialog dialog = new LoadingDialog(ForgetPasswordActivity.this);
                dialog.start();
                if(InputValidator.isEmail(email))
                {
                    auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Snackbar snackbar = Snackbar
                                    .make(findViewById(R.id.forget_pass_layout),
                                            String.valueOf(getResources().getString(R.string.email_has_been_sent)), Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.green));
                            dialog.dismiss();
                            snackbar.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                }
                            },Snackbar.LENGTH_LONG+1000);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar snackbar = Snackbar
                                    .make(findViewById(R.id.forget_pass_layout),
                                            String.valueOf(getResources().getString(R.string.something_wrong_happened)), Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.red));
                            dialog.dismiss();
                            snackbar.show();

                        }
                    });
                }else
                {
                    forgetPassEmail.setError(getResources().getString(R.string.please_enter_a_valid_email));
                }
            }
        });
    }
}