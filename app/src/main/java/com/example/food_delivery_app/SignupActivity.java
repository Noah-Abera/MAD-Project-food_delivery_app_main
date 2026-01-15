package com.example.food_delivery_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignupActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        EditText fullName = findViewById(R.id.inputFullName);
        EditText email = findViewById(R.id.inputEmail);
        EditText password = findViewById(R.id.inputPassword);
        EditText confirm = findViewById(R.id.inputConfirmPassword);
        Button signUp = findViewById(R.id.btnSignUp);
        TextView loginLink = findViewById(R.id.loginLink);

        // Sign Up button click
        signUp.setOnClickListener(v -> {
            String name = fullName.getText().toString().trim();
            String emailText = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirmPass = confirm.getText().toString().trim();

            // Validation
            if (name.isEmpty() || emailText.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Email validation
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                email.requestFocus();
                return;
            }

            // Password length validation
            if (pass.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                password.requestFocus();
                return;
            }

            // Password match validation
            if (!pass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                confirm.setText("");
                confirm.requestFocus();
                return;
            }

            // Check if email already exists
            if (databaseHelper.checkEmailExists(emailText)) {
                Toast.makeText(this, "Email already registered. Please login or use a different email.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Register user
            boolean isRegistered = databaseHelper.addUser(name, emailText, pass);

            if (isRegistered) {
                Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();

                // Navigate back to login page
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                // Pass the email to pre-fill the login field
                intent.putExtra("email", emailText);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        // Login link click
        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}