package com.example.food_delivery_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private TextInputEditText searchInput;
    private LinearLayout restaurantsContainer;
    private List<RestaurantCard> restaurantCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);


        // Initialize back button
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> onBackPressed());
        }

        // Initialize views
        searchInput = findViewById(R.id.searchInput);
        restaurantsContainer = findViewById(R.id.restaurantsLayout);

        // Note: You need to change the restaurantsContainer reference
        // Since your layout doesn't have a restaurantsLayout LinearLayout,
        // you'll need to manage restaurant visibility differently.
        // For now, I'll show you how to filter by hiding/showing CardViews

        // Setup search functionality
        setupSearch();

        // Setup restaurant buttons
        setupRestaurantButtons();

        // Setup category filters
        setupCategoryFilters();

        // Initialize restaurant cards list
        initializeRestaurantCards();
    }

    private void initializeRestaurantCards() {
        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnSunny),
                findViewById(R.id.tvSunnyName),
                "Sunny Burger and Pizza",
                "sunny",
                "Pizza,Burger"
        ));
        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnRome),
                findViewById(R.id.tvRomeName),
                "Rome 1960 Chicken, Pizza and Burger",
                "rome",
                "Chicken,Pizza,Burger"
        ));
        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnHTown),
                findViewById(R.id.tvHTownName),
                "H Town Burger",
                "htown",
                "Burger"
        ));
        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnVenezia),
                findViewById(R.id.tvVeneziaName),
                "Venezia – Italian Restaurant",
                "venezia",
                "Italian,Pasta"
        ));
        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnTokyo),
                findViewById(R.id.tvTokyoName),
                "Tokyo Sushi House",
                "tokyo",
                "Sushi,Japanese"
        ));
        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnNapoli),
                findViewById(R.id.tvNapoliName),
                "Napoli Pizza House",
                "napoli",
                "Pizza,Italian"
        ));
        restaurantCards.add(new RestaurantCard(
                findViewById(R.id.btnJuice),
                findViewById(R.id.tvJuiceName),
                "Fresh Juice Bar",
                "juice",
                "Juice,Drinks"
        ));
    }

    private void setupSearch() {
        if (searchInput != null) {
            // Real-time search filtering
            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterRestaurants(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Handle search when pressing enter/action button
            searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String query = v.getText().toString().trim();
                    if (!TextUtils.isEmpty(query)) {
                        // Navigate to search results activity
                        navigateToSearchResults(query);
                    }
                    return true;
                }
            });

            // Add a clear button functionality
            searchInput.setOnTouchListener((v, event) -> {
                // You can add clear functionality here if needed
                return false;
            });
        }
    }

    private void setupRestaurantButtons() {
        // Setup buttons for each restaurant
        setupButton(R.id.btnSunny, "Sunny Burger and Pizza", "sunny");
        setupButton(R.id.btnRome, "Rome 1960 Chicken, Pizza and Burger", "rome");
        setupButton(R.id.btnHTown, "H Town Burger", "htown");
        setupButton(R.id.btnVenezia, "Venezia – Italian Restaurant", "venezia");
        setupButton(R.id.btnTokyo, "Tokyo Sushi House", "tokyo");
        setupButton(R.id.btnNapoli, "Napoli Pizza House", "napoli");
        setupButton(R.id.btnJuice, "Fresh Juice Bar", "juice");
    }

    private void setupCategoryFilters() {
        // Setup category click listeners
        setupCategory(R.id.categoryPizza, "Pizza");
        setupCategory(R.id.categoryBurger, "Burger");
        setupCategory(R.id.categoryDrinks, "Drinks");
        setupCategory(R.id.categorySushi, "Sushi");
        setupCategory(R.id.categoryDessert, "Dessert");
    }

    private void setupCategory(int categoryId, String category) {
        TextView categoryView = findViewById(categoryId);
        if (categoryView != null) {
            categoryView.setOnClickListener(v -> {
                // Highlight selected category
                resetCategoryBackgrounds();
                categoryView.setBackgroundResource(R.drawable.category_bg_selected);

                // Filter restaurants by category
                filterByCategory(category);
            });
        }
    }

    private void resetCategoryBackgrounds() {
        int[] categoryIds = {
                R.id.categoryPizza,
                R.id.categoryBurger,
                R.id.categoryDrinks,
                R.id.categorySushi,
                R.id.categoryDessert
        };

        for (int id : categoryIds) {
            TextView categoryView = findViewById(id);
            if (categoryView != null) {
                categoryView.setBackgroundResource(R.drawable.category_bg);
            }
        }
    }

    private void setupButton(int buttonId, String restaurantName, String restaurantId) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantMenuActivity.class);
                intent.putExtra("RESTAURANT_NAME", restaurantName);
                intent.putExtra("RESTAURANT_ID", restaurantId);
                startActivity(intent);
            });
        }
    }

    private void filterRestaurants(String query) {
        if (TextUtils.isEmpty(query)) {
            // Show all restaurants when query is empty
            showAllRestaurants();
            return;
        }

        query = query.toLowerCase().trim();

        for (RestaurantCard restaurant : restaurantCards) {
            boolean matches = restaurant.name.toLowerCase().contains(query) ||
                    restaurant.tags.toLowerCase().contains(query);

            // Get the parent CardView
            View cardView = (View) restaurant.button.getParent().getParent();
            cardView.setVisibility(matches ? View.VISIBLE : View.GONE);
        }
    }

    private void filterByCategory(String category) {
        category = category.toLowerCase();

        for (RestaurantCard restaurant : restaurantCards) {
            boolean matches = restaurant.tags.toLowerCase().contains(category);

            // Get the parent CardView
            View cardView = (View) restaurant.button.getParent().getParent();
            cardView.setVisibility(matches ? View.VISIBLE : View.GONE);
        }
    }

    private void showAllRestaurants() {
        for (RestaurantCard restaurant : restaurantCards) {
            View cardView = (View) restaurant.button.getParent().getParent();
            cardView.setVisibility(View.VISIBLE);
        }
    }

    private void navigateToSearchResults(String query) {
        Intent intent = new Intent(RestaurantListActivity.this, SearchResultsActivity.class);
        intent.putExtra("SEARCH_QUERY", query);
        startActivity(intent);
    }

    // Helper class to store restaurant card information
    static class RestaurantCard {
        Button button;
        TextView nameView;
        String name;
        String id;
        String tags;

        RestaurantCard(Button button, TextView nameView, String name, String id, String tags) {
            this.button = button;
            this.nameView = nameView;
            this.name = name;
            this.id = id;
            this.tags = tags;
        }
    }
}