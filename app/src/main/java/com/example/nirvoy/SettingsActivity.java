package com.example.nirvoy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private Button btn_Radius;
    private EditText radiusEdittext;

    static String radius = "2000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn_Radius = findViewById(R.id.radiusBtnId);
        radiusEdittext = findViewById(R.id.radiusEditText);

        btn_Radius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius = radiusEdittext.getText().toString().trim();
                radiusEdittext.setText("");
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent);
            }
        });

    }
}
