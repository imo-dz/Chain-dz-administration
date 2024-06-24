package com.example.chaindzadministration.Views;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.chaindzadministration.Controllers.LoadingDialog;
import com.example.chaindzadministration.Controllers.UserSingleton;
import com.example.chaindzadministration.Models.User;
import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.AppCompat;
import com.example.chaindzadministration.Utils.Constants;
import com.example.chaindzadministration.Utils.InputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompat {
    private TextInputLayout emailSignIn;
    private TextInputLayout passwordSignIn;
    private TextView forgetPasswordTv;
    private AppCompatButton loginBtn;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        emailSignIn = findViewById(R.id.email_signIn);
        passwordSignIn = findViewById(R.id.password_signIn);
        forgetPasswordTv = findViewById(R.id.forget_password_tv);
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailSignIn.getEditText().getText().toString().trim();
                String password = passwordSignIn.getEditText().getText().toString();
                LoadingDialog dialog = new LoadingDialog(LoginActivity.this);
                if(checkInputs(email,password))
                {
                    dialog.start();
                    emailSignIn.setError(null);
                    passwordSignIn.setError(null);
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                // user is loged in
                                db.collection(Constants.USERS_COLLECTION).document(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User user = documentSnapshot.toObject(User.class);
                                        UserSingleton.getInstance().setUser(user);
                                        dialog.dismiss();
                                        chooseActivity();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Snackbar snackbar = Snackbar
                                                .make(findViewById(R.id.login_layout),
                                                        String.valueOf(getResources().getString(R.string.something_wrong_happened)), Snackbar.LENGTH_LONG);
                                        snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.red));
                                        snackbar.show();
                                    }
                                });

                            }else
                            {
                                if (task.getException() instanceof FirebaseAuthInvalidUserException) {

                                    emailSignIn.setError(getResources().getString(R.string.email_doent_exist));
                                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    passwordSignIn.setError(getResources().getString(R.string.wrong_password));

                                } else {
                                    Log.e("LoginActivity", "Invalid user exception");
                                    Snackbar snackbar = Snackbar
                                            .make(findViewById(R.id.login_layout),
                                                    String.valueOf(getResources().getString(R.string.something_wrong_happened)), Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.red));
                                    snackbar.show();

                                }
                                dialog.dismiss();
                            }
                        }
                    });
                }

            }
        });
        forgetPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
    private boolean checkInputs(String email, String password)
    {
        emailSignIn.setError(null);
        passwordSignIn.setError(null);
        boolean cond = true;
        if(InputValidator.isEmpty(email))
        {
            emailSignIn.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }
        if(!InputValidator.isEmail(email))
        {
            emailSignIn.setError(getResources().getString(R.string.please_enter_a_valid_email));
            cond=false;
        }
        if(InputValidator.isEmpty(password))
        {
            passwordSignIn.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }
        return cond;
    }
    private void chooseActivity()
    {
        User user = UserSingleton.getInstance().getUser();
        Intent intent;

        if(user.getAccountType().equals(Constants.ACCOUNT_TYPE_ADMIN))
        {
            intent=new Intent(LoginActivity.this,AdminHomeActivity.class);
        }else if((user.getAccountType().equals(Constants.ACCOUNT_TYPE_CLAIM_MANAGER)))
        {
            intent=new Intent(LoginActivity.this,ClaimManagerHomeActivity.class);
        }else if((user.getAccountType().equals(Constants.ACCOUNT_TYPE_PRODUCTION_MANGER)))
        {
            intent=new Intent(LoginActivity.this,ProductionManagerHomeActivity.class);
        }
        else if((user.getAccountType().equals(Constants.ACCOUNT_TYPE_STOCK_MANAGER)))
        {
            intent=new Intent(LoginActivity.this,StockManagerHomeActivity.class);
        }
        else{
            intent = new Intent(LoginActivity.this, LoginActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}