package com.example.nirvoy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Contacts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private FloatingActionButton add,back;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        FirebaseRecyclerOptions<ContactData> options =
                new FirebaseRecyclerOptions.Builder<ContactData>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("ContactDatas").child(uid), ContactData.class)
                        .build();

        adapter = new ContactAdapter(options,this);
        recyclerView.setAdapter(adapter);

        add = findViewById(R.id.add);
        back = findViewById(R.id.back);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Contacts.this,  AddContact.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contacts.super.onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}




