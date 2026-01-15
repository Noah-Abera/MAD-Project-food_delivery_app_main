package com.example.food_delivery_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;  // FIXED: android.os.Bundle
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;  // FIXED: EdgeToEdge
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;  // FIXED: WindowInsetsCompat

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView signUp = findViewById(R.id.signUp);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Check if email was passed from signup activity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            inputEmail.setText(email);
            inputPassword.requestFocus();
        }

        // Login button click - SIMPLIFIED
        btnLogin.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showErrorDialog("Fields Required", "Please enter both email and password");
                return;
            }

            // Validate email format
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showErrorDialog("Invalid Email", "Please enter a valid email address");
                inputEmail.requestFocus();
                return;
            }

            // Check credentials in database
            boolean isAuthenticated = databaseHelper.authenticateUser(email, password);

            if (isAuthenticated) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Get user info
                User user = databaseHelper.getUserByEmail(email);

                // Navigate to Restaurant List
                Intent restaurantIntent = new Intent(MainActivity.this, RestaurantListActivity.class);
                if (user != null) {
                    restaurantIntent.putExtra("user_name", user.getFullName());
                    restaurantIntent.putExtra("user_email", user.getEmail());
                }
                startActivity(restaurantIntent);
                finish();
            } else {
                // Check if email exists
                boolean emailExists = databaseHelper.checkEmailExists(email);
                if (emailExists) {
                    showErrorDialog("Login Failed", "Incorrect password. Please try again.");
                    inputPassword.setText("");
                    inputPassword.requestFocus();
                } else {
                    showErrorDialog("User Not Found",
                            "No account found with this email. Please sign up first.");
                    inputEmail.requestFocus();
                }
            }
        });

        // Sign Up text click
        signUp.setOnClickListener(v -> {
            Intent signupIntent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(signupIntent);
        });

        // Forgot Password text click - SIMPLIFIED
        forgotPassword.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            if (email.isEmpty()) {
                showErrorDialog("Email Required", "Please enter your email address first");
                inputEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showErrorDialog("Invalid Email", "Please enter a valid email address");
                inputEmail.requestFocus();
            } else if (databaseHelper.checkEmailExists(email)) {
                // In a real app, you would send a reset link here
                Toast.makeText(this,
                        "Password reset instructions sent to " + email,
                        Toast.LENGTH_LONG).show();
            } else {
                showErrorDialog("Email Not Found",
                        "No account found with this email. Please sign up first.");
            }
        });

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    // Method to show error dialog
    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}