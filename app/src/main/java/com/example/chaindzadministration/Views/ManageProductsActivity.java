package com.example.chaindzadministration.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaindzadministration.Controllers.ProductAdapter;
import com.example.chaindzadministration.Controllers.UserSingleton;
import com.example.chaindzadministration.Models.Product;
import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private SearchView searchView;
    private List<Product> products;
    private RecyclerView recyclerView;
    private ImageView scanIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);
        init();
    }
    private void init()
    {
        db = FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.products_rv);
        searchView = findViewById(R.id.searchView);
        products = new ArrayList<>();
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        scanIv = findViewById(R.id.scan_barcode_iv);
        scanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
    }
    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getResources().getString(R.string.volume_up));
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            searchView.setQuery(result.getContents(),false);
            filterList(result.getContents());
        }

    });
    private void filterList(String text)
    {
        List<Product> searchList = new ArrayList<>();
        for(Product product : products)
        {
            if(product.getName().toLowerCase().contains(text.toLowerCase()) || product.getBarCode().contains(text.toLowerCase()))
                searchList.add(product);
        }
        ProductAdapter adapter = new ProductAdapter(ManageProductsActivity.this,searchList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(ManageProductsActivity.this, 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        products = new ArrayList<>();
        Query query = db.collection(Constants.PRODUCTS_COLLECTION).whereEqualTo("productionId", FirebaseAuth.getInstance().getUid()).orderBy("timeStamp", Query.Direction.DESCENDING);
        if(UserSingleton.getInstance().getUser().getAccountType().equals(Constants.ACCOUNT_TYPE_ADMIN))
            query = db.collection(Constants.PRODUCTS_COLLECTION).orderBy("timeStamp", Query.Direction.DESCENDING);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    Product product = documentSnapshot.toObject(Product.class);
                    product.setId(documentSnapshot.getId());
                    products.add(product);
                }
                ProductAdapter adapter = new ProductAdapter(ManageProductsActivity.this,products);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(ManageProductsActivity.this, 2));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}