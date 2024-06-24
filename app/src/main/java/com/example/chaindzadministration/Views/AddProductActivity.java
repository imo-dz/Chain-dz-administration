package com.example.chaindzadministration.Views;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaindzadministration.Controllers.LoadingDialog;
import com.example.chaindzadministration.Controllers.UserSingleton;
import com.example.chaindzadministration.Models.Product;
import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.Constants;
import com.example.chaindzadministration.Utils.InputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {
    private ImageView productImageView;
    private TextInputLayout productNameInput;
    private TextInputLayout priceInput;
    private TextInputLayout descriptionInput;
    private TextInputLayout barCodeInput;
    private AppCompatButton addButton;
    private Uri uri;
    private static final int PERMISSION_REQUEST_CODE = 666;
    private ActivityResultLauncher<String> fileChooserLauncher;
    private static final int FILE_CHOOSER_REQUEST_CODE = 333;
    private LoadingDialog dialog ;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private ImageView scanBarCodeIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        init();
    }
    private void init()
    {
        uri=null;
        db= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new LoadingDialog(this);
        productImageView = findViewById(R.id.imageView7);
        productNameInput = findViewById(R.id.quantity_input);
        priceInput = findViewById(R.id.price_input);
        descriptionInput = findViewById(R.id.sell_description_input);
        barCodeInput = findViewById(R.id.bar_code_input);
        scanBarCodeIv = findViewById(R.id.scan_bar_code_iv);
        addButton = findViewById(R.id.next_btn);
        scanBarCodeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
        fileChooserLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result!=null)
                            uri=result;
                        productImageView.setImageURI(uri);
                    }
                });
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermissions())
                {
                    fileChooserLauncher.launch("image/*");

                }else
                    requestPermissions();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri==null)
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.please_add_product_image),Toast.LENGTH_LONG).show();
                }else
                {
                    Product product =checkInputs();
                    if (product!=null)
                    {
                        //upload image then upload product
                        dialog.start();
                        uploadPp(uri,product);
                    }
                }
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
            barCodeInput.getEditText().setText(result.getContents());

        }
    });

    private void uploadPp(Uri result,Product product)
    {
        storageReference = storage.getReference().child("Products/"+ UUID.randomUUID().toString()+"."+getMimeType(getApplicationContext(), result));
        storageReference.putFile(result).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    Task<Uri> downloadUrlTask = task.getResult().getStorage().getDownloadUrl();
                    downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            // Retrieve the download URL
                            String fileDownloadUrl = downloadUrl.toString();
                            product.setImgLink(fileDownloadUrl);
                            db.collection(Constants.PRODUCTS_COLLECTION).add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    dialog.dismiss();
                                    Snackbar snackbar = Snackbar
                                            .make(findViewById(R.id.main),
                                                    String.valueOf(getResources().getString(R.string.product_created_succefully)), Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.green));
                                    dialog.dismiss();
                                    snackbar.show();
                                    productNameInput.getEditText().setText("");
                                    priceInput.getEditText().setText("");
                                    barCodeInput.getEditText().setText("");
                                    descriptionInput.getEditText().setText("");
                                    productImageView.setImageDrawable(ContextCompat.getDrawable(AddProductActivity.this, R.drawable.add_product_img));
                                    uri=null;

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(AddProductActivity.this, ManageProductsActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            finish();
                                        }
                                    },Snackbar.LENGTH_LONG+500);
                                }
                            });

                        }
                    });
                } else {
                    // Handle the failure of file upload


                }

            }
        });
    }
    private Product checkInputs()
    {
        productNameInput.setError(null);
        priceInput.setError(null);
        descriptionInput.setError(null);
        barCodeInput.setError(null);
        String name = productNameInput.getEditText().getText().toString().trim();
        String price = priceInput.getEditText().getText().toString().trim();
        String desc = descriptionInput.getEditText().getText().toString().trim();
        String barCode = barCodeInput.getEditText().getText().toString().trim();
        boolean cond = true;
        Product product = new Product();
        if(InputValidator.isEmpty(name))
        {
            productNameInput.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }else
        {
            product.setName(name);
        }
        if(InputValidator.isEmpty(price))
        {
            priceInput.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }else
        {
            product.setPrice(price);
        }
        if(InputValidator.isEmpty(desc))
        {
            descriptionInput.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }else
        {
            product.setDescription(desc);
        }
        if(InputValidator.isEmpty(barCode))
        {
            barCodeInput.setError(getResources().getString(R.string.field_can_not_be_empty));
            cond=false;
        }else
        {
            product.setBarCode(barCode);
        }
        product.setProductionId(FirebaseAuth.getInstance().getUid());
        product.setTimeStamp(System.currentTimeMillis());
        product.setCompanyName(UserSingleton.getInstance().getUser().getFirstName());
        if (cond)
            return product;
        else
            return null;
    }
    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }


    private boolean checkPermissions()
    {
        int storagePermission = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Return true if both permissions are granted, false otherwise
        return storagePermission == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        // Request storage and location permissions
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions are granted, perform your operations
                fileChooserLauncher.launch("image/*");
            } else {
                // Permissions are denied, show a message or handle accordingly
                Toast.makeText(getApplicationContext(), getString(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();
            }
        }
    }
}