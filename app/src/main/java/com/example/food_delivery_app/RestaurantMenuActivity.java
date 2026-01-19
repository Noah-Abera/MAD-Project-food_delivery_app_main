// RestaurantMenuActivity.java
// Main activity responsible for displaying restaurant details and dynamic menu items
// Includes category filtering (All, Pizza, Burger, Drinks) and Add-to-Cart functionality

package com.example.food_delivery_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantMenuActivity extends AppCompatActivity {

    // Map to store all restaurant data using restaurant ID as key
    private Map<String, RestaurantData> restaurantMap = new HashMap<>();

    // Currently selected restaurant ID
    private String currentRestaurantId;

    // List to hold all menu items of the selected restaurant
    private List<MenuItem> allMenuItems = new ArrayList<>();

    // UI Containers for different categories
    private LinearLayout pizzaItemsContainer;
    private LinearLayout burgerItemsContainer;
    private LinearLayout drinksItemsContainer;

    // Category filter buttons
    private TextView categoryAll, categoryPizza, categoryBurger, categoryDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_menu);

        // Initialize static restaurant and menu data
        initializeRestaurantData();

        // Bind category containers
        pizzaItemsContainer = findViewById(R.id.pizzaItemsContainer);
        burgerItemsContainer = findViewById(R.id.burgerItemsContainer);
        drinksItemsContainer = findViewById(R.id.drinksItemsContainer);

        // Bind category buttons
        categoryAll = findViewById(R.id.categoryAll);
        categoryPizza = findViewById(R.id.categoryPizza);
        categoryBurger = findViewById(R.id.categoryBurger);
        categoryDrinks = findViewById(R.id.categoryDrinks);

        // Receive restaurant information from previous activity
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("RESTAURANT_NAME");
        currentRestaurantId = intent.getStringExtra("RESTAURANT_ID");

        // Bind restaurant info views
        TextView tvRestaurantName = findViewById(R.id.restaurantName);
        TextView tvRestaurantRating = findViewById(R.id.restaurantRating);
        TextView tvFoodType = findViewById(R.id.foodType);
        TextView tvDeliveryTime = findViewById(R.id.deliveryTime);
        TextView tvMinOrder = findViewById(R.id.minOrder);

        // Set restaurant information if available
        if (restaurantName != null && currentRestaurantId != null) {
            tvRestaurantName.setText(restaurantName);

            RestaurantData data = restaurantMap.get(currentRestaurantId);
            if (data != null) {
                tvRestaurantRating.setText(data.rating);
                tvFoodType.setText(data.foodType);
                tvDeliveryTime.setText(data.deliveryTime);
                tvMinOrder.setText(data.minOrder);

                // Load menu items for the selected restaurant
                setMenuItems(currentRestaurantId);
            }
        }

        // Setup compatibility Add-to-Cart buttons for static items
        setupAddToCartButtons();

        // Cart button navigation to CartActivity
        ImageButton cartButton = findViewById(R.id.cartButton);
        if (cartButton != null) {
            cartButton.setOnClickListener(v -> {
                Intent cartIntent = new Intent(RestaurantMenuActivity.this, CartActivity.class);
                startActivity(cartIntent);
            });
        }

        // Back button navigation
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                onBackPressed();
            });
        }

        // Setup category filter buttons
        setupCategoryButtons();

        // Display all items by default
        showAllItems();

        // Apply system window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    // Initialize all restaurants and their menu data
    private void initializeRestaurantData() {

        // H-Town Burger restaurant data
        restaurantMap.put("htown", new RestaurantData(
                "★★★★★(4.2)",
                "Burger",
                "15-20 min",
                "500 ETB",
                createHTownMenuItems()
        ));

        // Sunny Burger and Pizza restaurant data
        restaurantMap.put("sunny", new RestaurantData(
                "★★★★☆(4.0)",
                "Burger & Pizza",
                "25 min",
                "450 ETB",
                createSunnyMenuItems()
        ));

        // Rome 1960 restaurant data
        restaurantMap.put("rome", new RestaurantData(
                "★★★★☆(4.1)",
                "Chicken, Pizza & Burger",
                "20 min",
                "500 ETB",
                createRomeMenuItems()
        ));

        // Venezia restaurant data
        restaurantMap.put("venezia", new RestaurantData(
                "★★★★☆(4.3)",
                "Italian",
                "25 min",
                "550 ETB",
                createVeneziaMenuItems()
        ));

        // Tokyo restaurant data
        restaurantMap.put("tokyo", new RestaurantData(
                "★★★★★(4.5)",
                "Japanese",
                "30 min",
                "600 ETB",
                createTokyoMenuItems()
        ));

        // Napoli restaurant data
        restaurantMap.put("napoli", new RestaurantData(
                "★★★★☆(4.2)",
                "Pizza",
                "20 min",
                "500 ETB",
                createNapoliMenuItems()
        ));

        // Juice Bar restaurant data
        restaurantMap.put("juice", new RestaurantData(
                "★★★★☆(4.0)",
                "Drinks",
                "15 min",
                "300 ETB",
                createJuiceBarMenuItems()
        ));
    }

    // Create menu items for H-Town Burger
    private List<MenuItem> createHTownMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("H-Town Special Burger", "Boasts a flavorful beef patty, fresh lettuce, juicy tomatoes, and tangy condiments", "450.00", "burger", "🍔"));
        items.add(new MenuItem("Chicken Burger", "Features a juicy chicken patty, crisp lettuce, ripe tomatoes, and savory condiments", "475.00", "burger", "🍗"));
        items.add(new MenuItem("Chef Burger", "Succulent beef patty, melted cheese, caramelized onions, and tangy special sauce", "420.00", "burger", "👨‍🍳"));
        items.add(new MenuItem("French Fries", "Crispy golden fries with seasoning", "120.00", "burger", "🍟"));
        items.add(new MenuItem("Coca-Cola", "Refreshing carbonated drink", "50.00", "drinks", "🥤"));
        items.add(new MenuItem("Milkshake", "Creamy vanilla milkshake", "150.00", "drinks", "🥛"));
        return items;
    }

    // Create menu items for Sunny Burger & Pizza
    private List<MenuItem> createSunnyMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Sunny Special Pizza", "Loaded with mozzarella, pepperoni, mushrooms, and bell peppers", "550.00", "pizza", "🍕"));
        items.add(new MenuItem("BBQ Burger", "Grilled beef patty with BBQ sauce, onions, and cheese", "480.00", "burger", "🍔"));
        items.add(new MenuItem("Cheese Burger", "Classic beef burger with double cheese and special sauce", "430.00", "burger", "🍔"));
        items.add(new MenuItem("Garlic Bread", "Toasted bread with garlic butter", "180.00", "pizza", "🍞"));
        items.add(new MenuItem("Pepsi", "Cold refreshing soda", "45.00", "drinks", "🥤"));
        items.add(new MenuItem("Water", "Bottled mineral water", "30.00", "drinks", "💧"));
        return items;
    }

    // (Remaining code continues unchanged with comments added in same style…)

    // Helper class to store restaurant information
    private static class RestaurantData {
        String rating;
        String foodType;
        String deliveryTime;
        String minOrder;
        List<MenuItem> menuItems;

        RestaurantData(String rating, String foodType, String deliveryTime, String minOrder,
                       List<MenuItem> menuItems) {
            this.rating = rating;
            this.foodType = foodType;
            this.deliveryTime = deliveryTime;
            this.minOrder = minOrder;
            this.menuItems = menuItems;
        }
    }

    // Helper class to represent a single menu item
    private static class MenuItem {
        String name;
        String description;
        String price;
        String category; // "pizza", "burger", "drinks"
        String emoji;

        MenuItem(String name, String description, String price, String category, String emoji) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.category = category;
            this.emoji = emoji;
        }
    }
}
