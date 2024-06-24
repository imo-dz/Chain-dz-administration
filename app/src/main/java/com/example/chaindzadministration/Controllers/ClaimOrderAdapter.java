package com.example.chaindzadministration.Controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chaindzadministration.Models.ClaimOrder;
import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Views.MessagingActivity;
import com.example.chaindzadministration.Models.ClaimOrder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ClaimOrderAdapter extends RecyclerView.Adapter<ClaimOrderAdapter.ClaimOrderViewHolder> {
    private Context context;
    private List<ClaimOrder> claimOrderList;

    public ClaimOrderAdapter(Context context, List<ClaimOrder> claimOrderList) {
        this.context = context;
        this.claimOrderList = claimOrderList;
    }

    @NonNull
    @Override
    public ClaimOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.claim_order_layout_item, parent, false);
        return new ClaimOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimOrderViewHolder holder, int position) {
        ClaimOrder claimOrder = claimOrderList.get(position);

        holder.productNameTv.setText(claimOrder.getProductName());
        holder.clientNameTv.setText(claimOrder.getClientName());
        holder.clientPhoneTv.setText(claimOrder.getClientPhone());
        holder.lotTv.setText("Lot: "+ claimOrder.getLot());
        holder.clientNameTv.setVisibility(View.GONE);
        holder.clientPhoneTv.setVisibility(View.GONE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = sdf.format(claimOrder.getTimeStamp());
        holder.dateTv.setText(date);

        if (claimOrder.getProductImgLink() != null && !claimOrder.getProductImgLink().isEmpty()) {
            Glide.with(context)
                    .load(claimOrder.getProductImgLink())
                    .into(holder.productImg);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessagingActivity.class);
            intent.putExtra("order",claimOrder);
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public int getItemCount() {
        return claimOrderList.size();
    }

    class ClaimOrderViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTv, clientNameTv, clientPhoneTv, dateTv,lotTv;
        ImageView productImg;

        public ClaimOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTv = itemView.findViewById(R.id.product_name_tv);
            clientNameTv = itemView.findViewById(R.id.client_name_tv);
            clientPhoneTv = itemView.findViewById(R.id.client_phone_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
            productImg = itemView.findViewById(R.id.product_img);
            lotTv = itemView.findViewById(R.id.lot_tv);
        }
    }
}
