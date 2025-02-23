package com.example.numad25sp_linyanfu;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutMeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TextView nameText = findViewById(R.id.nameTextView);
        TextView emailText = findViewById(R.id.emailTextView);

        nameText.setText("Linyan Fu");
        emailText.setText("fu.liny@northeastern.edu");
    }
} 