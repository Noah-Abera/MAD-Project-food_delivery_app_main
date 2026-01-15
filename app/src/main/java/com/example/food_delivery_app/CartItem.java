package com.example.food_delivery_app;

public class CartItem {
    private String name;
    private String price;
    private int quantity;
    private String imageUrl;

    public CartItem(String name, String price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = "";
    }

    public CartItem(String name, String price, int quantity, String imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getTotalPrice() {
        try {
            double priceValue = Double.parseDouble(price);
            return priceValue * quantity;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
        }
    }
}