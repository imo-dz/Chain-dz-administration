package com.example.chaindzadministration.Controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chaindzadministration.Models.Product;

import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.Constants;
import com.example.chaindzadministration.Views.ProductDetailsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private List<Product> mProductList;
    private FirebaseFirestore db;
    public ProductAdapter(Context context, List<Product> productList) {
        this.mContext = context;
        this.mProductList = productList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item_layout, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mProductList.get(position);
        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText(product.getPrice()+" "+mContext.getResources().getString(R.string.dzd));
        holder.companyTv.setText(product.getCompanyName());
        // Load image using Glide library
        Glide.with(mContext)
                .load(product.getImgLink())
                .into(holder.productImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("product",product);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView companyTv;
        CardView cardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.product_iv);
            productNameTextView = itemView.findViewById(R.id.product_name_tv);
            productPriceTextView = itemView.findViewById(R.id.product_Price_tv);
            companyTv = itemView.findViewById(R.id.company_name_tv);
        }
    }
}