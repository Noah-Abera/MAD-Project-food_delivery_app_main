package com.example.food_delivery_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderTrackingActivity extends AppCompatActivity {

    // Sample data
    private static final String DEFAULT_DRIVER_NAME = "Abebe Kebede";
    private static final String DEFAULT_DRIVER_PHONE = "+251911223344";
    private static final String DEFAULT_VEHICLE = "Dodai Model T6+ [electric]";
    private static final String DEFAULT_ORDER_NUMBER = "FD-2345-12";
    private static final String DEFAULT_ESTIMATED_TIME = "2:45 pm (15min)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_tracking);

        // Views
        TextView tvOrderNumber = findViewById(R.id.tvOrderNumber);
        TextView tvDriverName = findViewById(R.id.tvDriverName);
        TextView tvVehicle = findViewById(R.id.tvVehicle);
        TextView tvEstimatedTime = findViewById(R.id.tvEstimatedTime);

        Button btnCall = findViewById(R.id.btnCall);
        Button btnMessage = findViewById(R.id.btnMessage);
        Button btnReturnHome = findViewById(R.id.btnReturnHome);
        Button btnViewOrderDetails = findViewById(R.id.btnViewOrderDetails);

        ImageButton backButton = findViewById(R.id.backButton);

        // Intent data
        Intent intent = getIntent();

        String orderNumber = intent.getStringExtra("ORDER_NUMBER");
        String estimatedTime = intent.getStringExtra("ESTIMATED_TIME");
        String driverName = intent.getStringExtra("DRIVER_NAME");
        String vehicle = intent.getStringExtra("VEHICLE");
        String driverPhone = intent.getStringExtra("DRIVER_PHONE");

        if (orderNumber == null) orderNumber = DEFAULT_ORDER_NUMBER;
        if (estimatedTime == null) estimatedTime = DEFAULT_ESTIMATED_TIME;
        if (driverName == null) driverName = DEFAULT_DRIVER_NAME;
        if (vehicle == null) vehicle = DEFAULT_VEHICLE;
        if (driverPhone == null) driverPhone = DEFAULT_DRIVER_PHONE;

        // Set data
        tvOrderNumber.setText("Order #" + orderNumber);
        tvDriverName.setText(driverName);
        tvVehicle.setText(vehicle);
        tvEstimatedTime.setText(estimatedTime);

        // ================= BUTTON LISTENERS =================

        String finalDriverPhone1 = driverPhone;
        String finalDriverName = driverName;
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall(finalDriverPhone1, finalDriverName);
            }
        });

        String finalDriverPhone = driverPhone;
        String finalOrderNumber = orderNumber;
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(finalDriverPhone, finalOrderNumber);
            }
        });

        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToHome();
            }
        });

        String finalOrderNumber1 = orderNumber;
        btnViewOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDetails(finalOrderNumber1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack();
            }
        });

        // Window insets
        handleWindowInsets();
    }

    private void makePhoneCall(String phoneNumber, String driverName) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
            Toast.makeText(this, "Calling " + driverName + "...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Cannot make call", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage(String phoneNumber, String orderNumber) {
        try {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setData(Uri.parse("smsto:" + phoneNumber));
            smsIntent.putExtra("sms_body", "Hello! Regarding my order #" + orderNumber);
            startActivity(smsIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Cannot send message", Toast.LENGTH_SHORT).show();
        }
    }

    private void returnToHome() {
        Intent intent = new Intent(this, RestaurantListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showOrderDetails(String orderNumber) {
        Toast.makeText(
                this,
                "Order details for #" + orderNumber + "\nFeature coming soon!",
                Toast.LENGTH_LONG
        ).show();
    }

    private void navigateBack() {
        finish();
    }

    private void handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.root),
                new androidx.core.view.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                        v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                        return insets;
                    }
                }
        );
    }
//    @Override
//    public void onBackPressed() {
//        navigateBack();
//    }

}