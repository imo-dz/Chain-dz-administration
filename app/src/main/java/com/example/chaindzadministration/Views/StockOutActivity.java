package com.example.chaindzadministration.Views;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chaindzadministration.Controllers.LoadingDialog;
import com.example.chaindzadministration.Models.Product;
import com.example.chaindzadministration.Models.ProductLot;
import com.example.chaindzadministration.Models.Stock;
import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.math.BigInteger;

public class StockOutActivity extends AppCompatActivity {
    private String barCode;
    private LoadingDialog loadingDialog;

    // Web3j variables
    private Web3j web3j;
    private String contractAddress = "YOUR_CONTRACT_ADDRESS";
    private String infuraUrl = "https://mainnet.infura.io/v3/2001122a9bbc4e87a090f384a0bd6a58";
    private ContractGasProvider contractGasProvider;
    private Credentials credentials;
    private YourSmartContract smartContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_out);
        loadingDialog = new LoadingDialog(StockOutActivity.this);

        // Initialize Web3j
        web3j = Web3j.build(new HttpService(infuraUrl));

        // Initialize Gas Provider
        contractGasProvider = new StaticGasProvider(BigInteger.valueOf(20000000000L), BigInteger.valueOf(6721975));

        // Load credentials
        try {
            credentials = WalletUtils.loadCredentials("KQijslla6#ls", "C:\\Users\\imodz\\AppData\\wallet");
            smartContract = YourSmartContract.load(contractAddress, web3j, credentials, contractGasProvider);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("StockOutActivity", "Failed to load credentials: " + e.getMessage());
        }

        scanCode();
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getResources().getString(R.string.volume_up));
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            barCode = result.getContents();
            showDialog();
        } else {
            finish();
        }
    });

    public Bitmap generateBarcodeWithNumbers(String barcodeText) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(barcodeText, BarcodeFormat.EAN_13, 140, 60);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap barcodeBitmap = encoder.createBitmap(matrix);

            // Create a new bitmap with extra height for the numbers
            Bitmap bitmapWithNumbers = Bitmap.createBitmap(barcodeBitmap.getWidth(), barcodeBitmap.getHeight() + 20, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapWithNumbers);

            // Draw the barcode bitmap on the new canvas
            canvas.drawBitmap(barcodeBitmap, 0, 0, null);

            // Draw the barcode numbers below the barcode
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(16);
            paint.setTextAlign(Paint.Align.CENTER);
            int xPos = canvas.getWidth() / 2;
            int yPos = barcodeBitmap.getHeight() + 20;
            canvas.drawText(barcodeText, xPos, yPos, paint);

            return bitmapWithNumbers;

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    private void showDialog() {
        // Inflate the dialog layout
        loadingDialog.start();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_text, null);

        // Find the EditText in the dialog layout
        final EditText editTextProductLot = dialogView.findViewById(R.id.editTextProductLot);

        // Fetch product information from blockchain
        try {
            RemoteCall<Product> productCall = smartContract.getProductByBarcode(barCode);
            Product product = productCall.send();

            if (product == null) {
                loadingDialog.dismiss();
                showProductNotExistDialog();
            } else {
                TextView productCompany = dialogView.findViewById(R.id.product_company_tv);
                TextView productPrice = dialogView.findViewById(R.id.product_price_tv);
                ImageView productImageView = dialogView.findViewById(R.id.product_iv);
                ImageView productBarCodeIv = dialogView.findViewById(R.id.product_bar_code_iv);
                productCompany.setText(product.getCompanyName());
                productPrice.setText(product.getPrice() + " " + getResources().getString(R.string.dzd));
                Glide.with(StockOutActivity.this)
                        .load(product.getImgLink())
                        .into(productImageView);

                // generating bar code
                Bitmap barcodeWithNumbers = generateBarcodeWithNumbers(product.getBarCode());
                productBarCodeIv.setImageBitmap(barcodeWithNumbers);

                AlertDialog.Builder builder = new AlertDialog.Builder(StockOutActivity.this);
                builder.setTitle(product.getName());
                builder.setView(dialogView);
                builder.setCancelable(false);
                builder.setPositiveButton(getResources().getString(R.string.OK), null);
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Retrieve the input value
                        String productLot = editTextProductLot.getText().toString();

                        // Add your validation logic here
                        if (productLot.isEmpty()) {
                            editTextProductLot.setError(getResources().getString(R.string.product_lot_canot_be_empty));
                        } else {
                            dialog.dismiss();
                            loadingDialog.start();

                            // Fetch product lot from blockchain
                            try {
                                RemoteCall<ProductLot> productLotCall = smartContract.getProductLot(product.getId(), productLot);
                                ProductLot productLot1 = productLotCall.send();

                                if (productLot1 == null) {
                                    showProductLotNotExistDialog();
                                } else {
                                    // Check if the product lot is in stock and mark as out
                                    Stock stock = productLot1.getStockList().get(productLot1.getStockList().size() - 1);
                                    if (stock.getStockMId().equals(credentials.getAddress()) && !stock.isOut()) {
                                        markProductLotAsOut(productLot1.getId(), stock);
                                    } else {
                                        loadingDialog.dismiss();
                                        showProductNotInStockDialog();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                loadingDialog.dismiss();
                                Log.e("StockOutActivity", "Failed to fetch product lot: " + e.getMessage());
                            }
                        }
                    }
                });

                loadingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadingDialog.dismiss();
            Log.e("StockOutActivity", "Failed to fetch product: " + e.getMessage());
        }
    }

    private void markProductLotAsOut(String productLotId, Stock stock) {
        // Update stock status on blockchain
        try {
            TransactionReceipt transactionReceipt = smartContract.updateProductLot(productLotId, stock).send();
            if (transactionReceipt.isStatusOK()) {
                loadingDialog.dismiss();
                finish();
            } else {
                loadingDialog.dismiss();
                Log.e("StockOutActivity", "Transaction failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadingDialog.dismiss();
            Log.e("StockOutActivity", "Failed to update product lot: " + e.getMessage());
        }
    }

    private void showProductNotExistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.error));
        builder.setMessage(getResources().getString(R.string.product_not_exist));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.scan_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanCode();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the "Cancel" action
                dialog.dismiss();
                finish();
            }
        });

        // Show the dialog
        builder.create().show();
    }

    private void showProductLotNotExistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.error));
        builder.setMessage(getResources().getString(R.string.product_lot_not_exist));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.scan_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanCode();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the "Cancel" action
                dialog.dismiss();
                finish();
            }
        });

        // Show the dialog
        builder.create().show();
    }

    private void showProductNotInStockDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.error));
        builder.setMessage(getResources().getString(R.string.product_not_in));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        // Show the dialog
        builder.create().show();
    }
}
