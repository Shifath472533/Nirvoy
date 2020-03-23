package com.example.nirvoy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContact extends AppCompatActivity {

    private Button addContactButton;
    private EditText nameEditText, numberEditText;

    FirebaseUser user;
    String uid;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        databaseReference = FirebaseDatabase.getInstance().getReference("ContactDatas");

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        addContactButton = findViewById(R.id.save);
        nameEditText = findViewById(R.id.name);
        numberEditText = findViewById(R.id.number);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });
    }

    public void saveContact()
    {
        String name = nameEditText.getText().toString().trim();
        String number = numberEditText.getText().toString().trim();

        String key = databaseReference.push().getKey();

        ContactData contactData = new ContactData(name,number);

        databaseReference.child(uid).child(key).setValue(contactData);

        nameEditText.setText("");
        numberEditText.setText("");

        Toast.makeText(this, "New Contact Added", Toast.LENGTH_SHORT).show();

        onBackPressed();

    }
}
