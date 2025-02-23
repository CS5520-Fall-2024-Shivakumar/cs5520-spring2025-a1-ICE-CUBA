package com.example.numad25sp_linyanfu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button b = findViewById(R.id.button);
        b.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
            startActivity(intent);
        });

        Button calcButton = findViewById(R.id.button2);
        calcButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuicCalcActivity.class);
            startActivity(intent);
        });

        Button contactButton = findViewById(R.id.buttonContact);
        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
            startActivity(intent);
        });


    }
}