package com.example.chaindzadministration.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaindzadministration.Controllers.ClaimOrderAdapter;
import com.example.chaindzadministration.Controllers.UserSingleton;
import com.example.chaindzadministration.Models.ClaimOrder;
import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClaimManagerHomeActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private List<ClaimOrder> claimOrderList;
    private RecyclerView recyclerView;
    private FloatingActionButton logoutFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_manager_home);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.claims_rv);
        logoutFab = findViewById(R.id.logout_fab);
        logoutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ClaimManagerHomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        claimOrderList = new ArrayList<>();
        db.collection(Constants.CLAIM_ORDERS_COLLECTION).whereEqualTo("productionId", UserSingleton.getInstance().getUser().getProductionId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    ClaimOrder claimOrder = documentSnapshot.toObject(ClaimOrder.class);
                    claimOrder.setId(documentSnapshot.getId());
                    claimOrderList.add(claimOrder);
                }
                ClaimOrderAdapter adapter = new ClaimOrderAdapter(ClaimManagerHomeActivity.this,claimOrderList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ClaimManagerHomeActivity.this));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}