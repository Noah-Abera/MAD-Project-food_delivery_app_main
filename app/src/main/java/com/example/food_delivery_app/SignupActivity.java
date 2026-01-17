package com.example.food_delivery_app;

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

public class SignupActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        databaseHelper = new DatabaseHelper(this);

        EditText fullName = findViewById(R.id.inputFullName);
        EditText email = findViewById(R.id.inputEmailPhone);
        EditText password = findViewById(R.id.inputSignupPassword);
        EditText confirm = findViewById(R.id.inputConfirmPassword);
        Button signUp = findViewById(R.id.btnSignUp);
        TextView loginLink = findViewById(R.id.loginRedirect);

        signUp.setOnClickListener(v -> {
            String name = fullName.getText().toString().trim();
            String identifier = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirmPass = confirm.getText().toString().trim();

            if (name.isEmpty() || identifier.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(identifier).matches();
            boolean isPhone = Patterns.PHONE.matcher(identifier).matches();

            if (!isEmail && !isPhone) {
                Toast.makeText(this, "Enter a valid email or phone number", Toast.LENGTH_SHORT).show();
                email.requestFocus();
                return;
            }

            if (pass.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                password.requestFocus();
                return;
            }

            if (!pass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                confirm.setText("");
                confirm.requestFocus();
                return;
            }

            if (databaseHelper.checkEmailExists(identifier)) {
                Toast.makeText(this,
                        "Account already exists. Please login or use a different email/phone.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            boolean isRegistered = databaseHelper.addUser(name, identifier, pass);

            if (isRegistered) {
                Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                intent.putExtra("email", identifier);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        View root = findViewById(R.id.signupMain);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
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
