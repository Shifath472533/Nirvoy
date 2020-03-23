package com.example.nirvoy;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView usernameTextView,emailTextView;
    FirebaseAuth mAuth;
    ImageButton medicalButton,policeButton;
    DatabaseReference databaseReference,deleteReference,databaseReference1;
    public ArrayList<String> contactDataList;
    public String msg,signInMsg,username;
    FirebaseUser user;
    String uid;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        contactDataList = new ArrayList<String>();

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        medicalButton = findViewById(R.id.medical);
        policeButton = findViewById(R.id.police);
        databaseReference = FirebaseDatabase.getInstance().getReference("ContactDatas").child(uid);



        medicalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = "medical";
                Intent intent = new Intent(NavigationActivity.this, MapActivity.class);
                intent.putExtra("msg",msg);
                startActivity(intent);
            }
        });
        policeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = "police";
                Intent intent = new Intent(NavigationActivity.this, MapActivity.class);
                intent.putExtra("msg",msg);
                startActivity(intent);

            }
        });

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(NavigationActivity.this, "Permission not given. This App needs all the premission granted for proper function", Toast.LENGTH_LONG).show();
            }
        };

        TedPermission.with(NavigationActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS)
                .check();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
            // Handle the camera action
            //Toast.makeText(this, "Contacts", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Contacts.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(this, RequestHistory.class);
            startActivity(intent);
            //Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_aboutUs) {

            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.nav_settings_2) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            finish();

            SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("state","false");
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
