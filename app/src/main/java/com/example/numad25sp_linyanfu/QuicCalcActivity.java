package com.example.numad25sp_linyanfu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuicCalcActivity extends AppCompatActivity {
    private TextView displayText;
    private StringBuilder currentExpression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quic_calc);

        displayText = findViewById(R.id.calc_display);
        currentExpression = new StringBuilder();
        displayText.setText("CALC");

        // Set up button click listeners
        setupNumberButton(R.id.button0, "0");
        setupNumberButton(R.id.button1, "1");
        setupNumberButton(R.id.button2, "2");
        setupNumberButton(R.id.button3, "3");
        setupNumberButton(R.id.button4, "4");
        setupNumberButton(R.id.button5, "5");
        setupNumberButton(R.id.button6, "6");
        setupNumberButton(R.id.button7, "7");
        setupNumberButton(R.id.button8, "8");
        setupNumberButton(R.id.button9, "9");
        setupNumberButton(R.id.buttonPlus, "+");
        setupNumberButton(R.id.buttonMinus, "-");

        Button deleteButton = findViewById(R.id.buttonX);
        deleteButton.setOnClickListener(v -> {
            if (currentExpression.length() > 0) {
                currentExpression.deleteCharAt(currentExpression.length() - 1);
                displayText.setText(currentExpression.length() > 0 ? currentExpression.toString() : "CALC");
            }
        });

        Button equalsButton = findViewById(R.id.buttonEquals);
        equalsButton.setOnClickListener(v -> {
            try {
                String result = evaluateExpression(currentExpression.toString());
                currentExpression = new StringBuilder(result);
                displayText.setText(result);
            } catch (Exception e) {
                displayText.setText("Error");
                currentExpression = new StringBuilder();
            }
        });
    }

    private void setupNumberButton(int buttonId, String value) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            if (currentExpression.length() == 0 && displayText.getText().toString().equals("CALC")) {
                currentExpression.append(value);
                displayText.setText(value);
            } else {
                currentExpression.append(value);
                displayText.setText(currentExpression.toString());
            }
        });
    }

    private String evaluateExpression(String expression) {
        String[] parts = expression.split("\\+|-");
        if (parts.length != 2) return "Error";

        int operator = expression.indexOf('+');
        if (operator == -1) operator = expression.indexOf('-');

        try {
            int num1 = Integer.parseInt(parts[0].trim());
            int num2 = Integer.parseInt(parts[1].trim());
            int result = expression.charAt(operator) == '+' ? num1 + num2 : num1 - num2;
            return String.valueOf(result);
        } catch (NumberFormatException e) {
            return "Error";
        }
    }
}