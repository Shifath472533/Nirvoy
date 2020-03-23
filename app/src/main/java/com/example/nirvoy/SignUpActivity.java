package com.example.nirvoy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText signUpEmailEditText, signUpPasswordEditText,usernameEditText;
    private TextView signInTextView;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up Activity");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbarId);
        signUpEmailEditText = findViewById(R.id.signUpEmailEditTextId);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditTextId);
        usernameEditText = findViewById(R.id.username);
        signUpButton= findViewById(R.id.signUpButtonId);
        signInTextView = findViewById(R.id.signInTextViewId);

        signInTextView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.signUpButtonId:
                userRegister();
                break;

            case R.id.signInTextViewId:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;

        }

    }

    private void userRegister() {

        final String email = signUpEmailEditText.getText().toString().trim();
        final String password = signUpPasswordEditText.getText().toString().trim();
        final String username = usernameEditText.getText().toString().trim();

        if(username.isEmpty()){
            usernameEditText.setError("Enter Username");
            usernameEditText.requestFocus();
            return;
        }

        //checking the validity of the email
        if(email.isEmpty())
        {
            signUpEmailEditText.setError("Enter an email address");
            signUpEmailEditText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signUpEmailEditText.setError("Enter a valid email address");
            signUpEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            signUpPasswordEditText.setError("Enter a password");
            signUpPasswordEditText.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            signUpPasswordEditText.setError("Minimum length of a password should be 6");
            signUpPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            finish();

                            SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("state","true");
                            editor.putString("email",email);
                            editor.putString("password",password);
                            editor.apply();

                            user = FirebaseAuth.getInstance().getCurrentUser();
                            assert user != null;
                            uid = user.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Username").child(uid);
                            databaseReference.setValue(username);

                            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("msg","SignedUp");
                            intent.putExtra("username",username);
                            startActivity(intent);

                        } else {

                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(getApplicationContext(), "User is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        // ...
                    }
                });


    }
}
