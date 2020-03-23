package com.example.nirvoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText signInEmailEditText, signInPasswordEditText;
    private TextView signUpTextView;
    private Button signInButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.setTitle("Sign In Activity");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbarId);
        signInEmailEditText = findViewById(R.id.signInEmailEditTextId);
        signInPasswordEditText = findViewById(R.id.signInPasswordEditTextId);
        signInButton= findViewById(R.id.signInButtonId);
        signUpTextView = findViewById(R.id.signUpTextViewId);

        signUpTextView.setOnClickListener(this);
        signInButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.signInButtonId:
                userLogin();
                break;

            case R.id.signUpTextViewId:
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                break;

        }

    }

    private void userLogin() {

        final String email = signInEmailEditText.getText().toString().trim();
        final String password = signInPasswordEditText.getText().toString().trim();

        //checking the validity of the email
        if(email.isEmpty())
        {
            signInEmailEditText.setError("Enter an email address");
            signInEmailEditText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signInEmailEditText.setError("Enter a valid email address");
            signInEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            signInPasswordEditText.setError("Enter a password");
            signInPasswordEditText.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            signInPasswordEditText.setError("Minimum length of a password should be 6");
            signInPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    finish();

                    SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("state","true");
                    editor.putString("email",email);
                    editor.putString("password",password);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("msg","SignedIn");
                    intent.putExtra("username","noname");
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Login unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
