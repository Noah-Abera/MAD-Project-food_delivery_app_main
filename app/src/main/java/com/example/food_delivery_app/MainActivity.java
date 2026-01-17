package com.example.food_delivery_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView signUp = findViewById(R.id.signUp);
        Button btnLogin = findViewById(R.id.btnLogin);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("email")) {
            inputEmail.setText(intent.getStringExtra("email"));
            inputPassword.requestFocus();
        }

        btnLogin.setOnClickListener(v -> {
            String identifier = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (identifier.isEmpty() || password.isEmpty()) {
                showErrorDialog("Fields Required", "Please enter email/phone and password");
                return;
            }

            boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(identifier).matches();
            boolean isPhone = Patterns.PHONE.matcher(identifier).matches();

            if (!isEmail && !isPhone) {
                showErrorDialog("Invalid Input", "Please enter a valid email or phone number");
                inputEmail.requestFocus();
                return;
            }

            boolean isAuthenticated = databaseHelper.authenticateUser(identifier, password);

            if (isAuthenticated) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                User user = databaseHelper.getUserByEmail(identifier);

                Intent restaurantIntent =
                        new Intent(MainActivity.this, RestaurantListActivity.class);

                if (user != null) {
                    restaurantIntent.putExtra("user_name", user.getFullName());
                    restaurantIntent.putExtra("user_email", user.getEmail());
                }

                startActivity(restaurantIntent);
                finish();
            } else {
                boolean exists = databaseHelper.checkEmailExists(identifier);

                if (exists) {
                    showErrorDialog("Login Failed", "Incorrect password. Please try again.");
                    inputPassword.setText("");
                    inputPassword.requestFocus();
                } else {
                    showErrorDialog("User Not Found",
                            "No account found. Please sign up first.");
                    inputEmail.requestFocus();
                }
            }
        });

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        });

        forgotPassword.setOnClickListener(v -> {
            String identifier = inputEmail.getText().toString().trim();

            if (identifier.isEmpty()) {
                showErrorDialog("Required", "Please enter email or phone number first");
                inputEmail.requestFocus();
                return;
            }

            boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(identifier).matches();
            boolean isPhone = Patterns.PHONE.matcher(identifier).matches();

            if (!isEmail && !isPhone) {
                showErrorDialog("Invalid Input", "Enter a valid email or phone number");
                inputEmail.requestFocus();
                return;
            }

            if (databaseHelper.checkEmailExists(identifier)) {
                Toast.makeText(this,
                        "Password recovery instructions sent",
                        Toast.LENGTH_LONG).show();
            } else {
                showErrorDialog("Not Found",
                        "No account found. Please sign up first.");
            }
        });

        View root = findViewById(R.id.main);
        if (root == null) {
            root = findViewById(android.R.id.content);
        }

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
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
