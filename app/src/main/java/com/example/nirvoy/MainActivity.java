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

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressbarId);
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String isLoggedIn = sharedPreferences.getString("state","false");
        if(isLoggedIn.equals("true")){
            if(sharedPreferences.contains("email") && sharedPreferences.contains("password")){
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        else{
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }


    @Override
    protected void onStart() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String isLoggedIn = sharedPreferences.getString("state","false");
        if(isLoggedIn.equals("true")){
            if(sharedPreferences.contains("email") && sharedPreferences.contains("password")){
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        else{
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        super.onStart();
    }

    @Override
    protected void onRestart() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String isLoggedIn = sharedPreferences.getString("state","false");
        if(isLoggedIn.equals("true")){
            if(sharedPreferences.contains("email") && sharedPreferences.contains("password")){
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        else{
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        super.onRestart();
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        String isLoggedIn = sharedPreferences.getString("state","false");
        if(isLoggedIn.equals("true")){
            if(sharedPreferences.contains("email") && sharedPreferences.contains("password")){
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        else{
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
