package edu.neu.madcourse.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import util.Message;
import util.MessageAdapter;

public class MainActivityChatFragment extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore database;
    Query query;
    private FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> adapter;
    private MultiAutoCompleteTextView input;
    private String userId;
    private String userName;
    private String gId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity_fragment_chat);

        FloatingActionButton btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        input = findViewById(R.id.input);
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Log.i("MainAcivity", " this os onCreate: " + user);

        //this.gId = savedInstanceState.getString("groupId");
        Bundle b = getIntent().getExtras();
        this.gId = ""; // or other values
        if(b != null)
            this.gId = b.getString("groupId");

        System.out.println("Group Id: " + this.gId);

        if(user==null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        System.out.println(user.getUid());
        userId = user.getUid();
        String n = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String m = n.substring(0, n.indexOf("@studybuddy.com"));
        userName = m;
        database = FirebaseFirestore.getInstance();

        CollectionReference productsRef = database.collection("messages");
        query = productsRef.whereEqualTo("gId", gId).orderBy("messageTime");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                }
            }
        });
        adapter = new MessageAdapter(query, userId, MainActivityChatFragment.this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSend){
            String message = input.getText().toString();
            if(TextUtils.isEmpty(message)){
                return;
            }
            database.collection("messages").add(new Message(userName, message, userId, gId));

            input.setText("");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

}
