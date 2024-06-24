package com.example.chaindzadministration.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaindzadministration.Controllers.LoadingDialog;
import com.example.chaindzadministration.Models.User;
import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.Constants;
import com.example.chaindzadministration.Utils.InputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddStockProductionActivity extends AppCompatActivity {
    private FirebaseAuth mAuth1;
    private FirebaseAuth mAuth2;
    private FirebaseFirestore db;
    private TextInputLayout firstname_client_signup, email_clientSignUp,
            client_wilayas_holder_register, city_client_register,
            client_password_signUp, client_confirm_password_signUp;

    private AutoCompleteTextView wilayaAutoCompleteTextView;
    private AppCompatButton registerButton;
    private LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock_production);
        init();
    }
    private void init()
    {
        mAuth1 = FirebaseAuth.getInstance();
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("[Database_url_here]")
                .setApiKey("AIzaSyDCRJj_L5mtOrM8tagsCs18kS736kT0EMc")
                .setApplicationId("chain-dz").build();

        try {
            FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e) {
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
        }
        db= FirebaseFirestore.getInstance();

        firstname_client_signup = findViewById(R.id.firstname_client_signup);
        email_clientSignUp = findViewById(R.id.email_clientSignUp);
        client_wilayas_holder_register = findViewById(R.id.client_wilayas_holder_register);
        city_client_register = findViewById(R.id.city_client_register);
        client_password_signUp = findViewById(R.id.client_password_signUp);
        client_confirm_password_signUp = findViewById(R.id.client_confirm_password_signUp);

        // Initialize TextInputEditTexts
        wilayaAutoCompleteTextView = client_wilayas_holder_register.findViewById(R.id.client_wilaya_register);
        ArrayAdapter wilayasadapter = new ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, Constants.DZ_WILAYAS);
        wilayaAutoCompleteTextView.setAdapter(wilayasadapter);




        // Initialize Button
        registerButton = findViewById(R.id.register_client_btn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = checkInputs();
                dialog = new LoadingDialog(AddStockProductionActivity.this);
                if(user!=null)
                {
                    dialog.start();
                    createUser(user);
                }else
                {




                }
            }
        });
    }

    private void createUser(User user)
    {
        mAuth2.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    // User creation successful

                    uploadUserToDb(user);


                }
                else
                {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                    {

                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.client_register_layout),
                                        String.valueOf(getResources().getString(R.string.email_already_used)), Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.red));
                        dialog.dismiss();
                        snackbar.show();
                    } else
                    {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.client_register_layout),
                                        String.valueOf(getResources().getString(R.string.something_wrong_happened)), Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.red));
                        dialog.dismiss();
                        snackbar.show();
                    }
                }
            }
        });
    }

    private void uploadUserToDb(User user) {
        user.setId(mAuth2.getUid());
        db.collection(Constants.USERS_COLLECTION).document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mAuth2.signOut();
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.client_register_layout),
                                        String.valueOf(getResources().getString(R.string.account_created_succefuly)), Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.green));
                        dialog.dismiss();
                        snackbar.show();
                        firstname_client_signup.getEditText().setText("");
                        client_confirm_password_signUp.getEditText().setText("");
                        client_password_signUp.getEditText().setText("");
                        email_clientSignUp.getEditText().setText("");
                        wilayaAutoCompleteTextView.setText("");
                        city_client_register.getEditText().setText("");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(AddStockProductionActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            }
                        },Snackbar.LENGTH_LONG+500);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mAuth2.signOut();
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.client_register_layout),
                                        String.valueOf(getResources().getString(R.string.something_wrong_happened)), Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.red));
                        dialog.dismiss();
                        snackbar.show();
                    }
                });
    }

    private User checkInputs()
    {
        boolean cond =true;
        firstname_client_signup.setError(null);
        email_clientSignUp.setError(null);

        client_password_signUp.setError(null);
        client_confirm_password_signUp.setError(null);
        city_client_register.setError(null);
        client_wilayas_holder_register.setError(null);

        String firstName = capitalizeFirstLetter(firstname_client_signup.getEditText().getText().toString()).trim();
        String email = email_clientSignUp.getEditText().getText().toString().trim();

        String password = client_password_signUp.getEditText().getText().toString();
        String confirmPass = client_confirm_password_signUp.getEditText().getText().toString();
        String city = city_client_register.getEditText().getText().toString().trim();
        String wilaya = wilayaAutoCompleteTextView.getText().toString().trim();
        if(InputValidator.isEmpty(firstName))
        {
            firstname_client_signup.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }
        if (InputValidator.isEmpty(wilaya)) {
            client_wilayas_holder_register.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }
        if (InputValidator.isEmpty(city)) {
            city_client_register.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }

        if(!InputValidator.isEmail(email))
        {
            email_clientSignUp.setError(getResources().getString(R.string.please_enter_a_valid_email));
            cond=false;
        }
        if(InputValidator.isEmpty(email))
        {
            email_clientSignUp.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }

        if (password.length()<8) {
            client_password_signUp.setError(String.valueOf(getResources().getString(R.string.weak_password)));
            cond=false;
        }
        if (InputValidator.isEmpty(password)) {
            client_password_signUp.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }
        if (!confirmPass.equals(password)) {
            client_confirm_password_signUp.setError(String.valueOf(getResources().getString(R.string.passwords_not_match)));
            cond=false;
        }
        if (InputValidator.isEmpty(confirmPass)) {
            client_confirm_password_signUp.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }
        if(cond)
        {
            User user = new User();
            if(getIntent().getExtras().getString("type").equals("s"))
            {
                user.setAccountType(Constants.ACCOUNT_TYPE_STOCK_MANAGER);
            }else
            {
                user.setAccountType(Constants.ACCOUNT_TYPE_PRODUCTION_MANGER);
            }

            user.setFirstName(firstName);
            user.setLastName("");
            user.setEmail(email);
            user.setPassword(password);
            user.setCity(city);
            user.setWilaya(wilaya);
            return user;
        }else
            return null;
    }
    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        char[] charArray = input.toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return new String(charArray);
    }
}