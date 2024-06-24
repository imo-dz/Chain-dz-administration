package com.example.chaindzadministration.Views;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaindzadministration.Controllers.LoadingDialog;
import com.example.chaindzadministration.Controllers.MessagesAdapter;
import com.example.chaindzadministration.Controllers.UserSingleton;
import com.example.chaindzadministration.Models.ClaimOrder;
import com.example.chaindzadministration.Models.Message;
import com.example.chaindzadministration.Models.User;
import com.example.chaindzadministration.R;
import com.example.chaindzadministration.Utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {
    private ImageView sendBtn;
    private EditText messageInput;
    private RecyclerView messageRecyclerView;

    private String sender;
    private String path;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private AppCompatButton markAsCompBtn;
    private FirebaseDatabase database ;
    private DatabaseReference msgRef;

    private List<Message> messagesList;
    private LoadingDialog loadingDialog;
    private MessagesAdapter adapter;
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        init();
    }
    private void init()
    {
        loadingDialog=new LoadingDialog(this);

        User user= UserSingleton.getInstance().getUser();

        markAsCompBtn=findViewById(R.id.mark_completed_btn);
        sendBtn =findViewById(R.id.send_message);
        messageInput = findViewById(R.id.message_input);
        messageRecyclerView = findViewById(R.id.messages_recyclerView);
        constraintLayout =findViewById(R.id.messages_fragment_layout);
        messagesList = new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://chain-dz-default-rtdb.firebaseio.com/");
        Bundle b = getIntent().getExtras();
        ClaimOrder order= (ClaimOrder) b.getSerializable("order");
        getMessages(order);
        adapter = new MessagesAdapter(messagesList,MessagingActivity.this);
        messageRecyclerView.setHasFixedSize(true);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(MessagingActivity.this));
        messageRecyclerView.setAdapter(adapter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uplaodMessage(order);
            }
        });
        final AppCompatActivity activity = this;


        markAsCompBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.start();

                //delete sell order
                db.collection(Constants.CLAIM_ORDERS_COLLECTION).document(order.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loadingDialog.dismiss();
                        finish();
                    }
                });
                // >------ to do delete messages from real time db
            }
        });
    }



    private void getMessages(ClaimOrder order)
    {

        msgRef = database.getReference("messages").child(order.getId());
        msgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Message message = dataSnapshot.getValue(Message.class);

                    messagesList.add(message);
                }

                adapter = new MessagesAdapter(messagesList,MessagingActivity.this);
                messageRecyclerView.setHasFixedSize(true);
                messageRecyclerView.setLayoutManager(new LinearLayoutManager(MessagingActivity.this));
                messageRecyclerView.setAdapter(adapter);
                messageRecyclerView.scrollToPosition(messagesList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uplaodMessage(ClaimOrder order)
    {
        Message message;
        User userM = UserSingleton.getInstance().getUser();
        message = new Message(userM.getFirstName(),messageInput.getText().toString(),FirebaseAuth.getInstance().getUid());
        messageInput.setText("");
        msgRef.push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("khnouna", "onSuccess: "+"success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("khnouna", "onFailure: "+e.toString());
            }
        });



    }
}