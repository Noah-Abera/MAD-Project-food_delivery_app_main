package com.example.food_delivery_app;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
        // Add some sample items
        cartItems.add(new CartItem("CheeseBurger", "350.00", 1));
        cartItems.add(new CartItem("Coca-cola", "50.00", 1));
        cartItems.add(new CartItem("Margherita Pizza", "420.00", 1));
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addItem(CartItem item) {
        // Check if item already exists in cart
        for (CartItem cartItem : cartItems) {
            if (cartItem.getName().equals(item.getName())) {
                cartItem.increaseQuantity();
                return;
            }
        }
        // If not found, add new item
        cartItems.add(item);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
        }
    }

    public void updateQuantity(int position, int quantity) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.get(position).setQuantity(quantity);
        }
    }

    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice();
        }
        return subtotal;
    }

    public double calculateTotal() {
        double subtotal = calculateSubtotal();
        double deliveryFee = 60.00;
        double tax = subtotal * 0.15; // 15% tax
        return subtotal + deliveryFee + tax;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public int getItemCount() {
        return cartItems.size();
    }
}