package com.example.chaindzadministration.Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaindzadministration.Models.Message;
import com.example.chaindzadministration.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>
{


    private List<Message> messagesList;
    private Context context;
    private FirebaseAuth auth;

    public MessagesAdapter(List<Message> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
        auth= FirebaseAuth.getInstance();


    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=  LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        return new MessagesViewHolder(view);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {

        Message message = messagesList.get(position);
        if (message.getSenderId().equals(auth.getCurrentUser().getUid()))
        {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.RIGHT);
            holder.messageCardView.setLayoutParams(params);
            holder.messageCardView.setCardBackgroundColor(context.getResources().getColor(R.color.lsecColor));
            holder.senderTv.setTextColor(context.getResources().getColor(R.color.white));
            holder.messageCntnt.setTextColor(context.getResources().getColor(R.color.white));
        }
        holder.senderTv.setText(message.getSender());
        holder.messageCntnt.setText(message.getMessage());

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        TextView senderTv,messageCntnt;
        FrameLayout messageCv ;
        CardView messageCardView;
        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTv =itemView.findViewById(R.id.sender_name);
            messageCntnt =itemView.findViewById(R.id.msg_content);
            messageCv=itemView.findViewById(R.id.containerframe);
            messageCardView = itemView.findViewById(R.id.msg_cardView);
        }
    }
}
