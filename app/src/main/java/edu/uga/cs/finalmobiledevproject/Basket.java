package edu.uga.cs.finalmobiledevproject;

import java.util.ArrayList;
import java.util.List;

public class Basket {
    private String user;
    private int id;
    private List<ShoppingItem> purchasedItems;
    private float totalPrice;

    public Basket(String u, int i, List<ShoppingItem> pi) {
        user = u;
        id = i;
        purchasedItems = pi;
        totalPrice = 0;
        if (!purchasedItems.isEmpty()) {
            for (ShoppingItem s : purchasedItems) {
                totalPrice = totalPrice + s.getItemPrice();
            }
        }
    }

    public Basket() {
        user = null;
        id = -1;
        purchasedItems = new ArrayList<ShoppingItem>();
        totalPrice = -1;
    }

    public void recalculateTotalPrice() {
        totalPrice = 0;
        if (!purchasedItems.isEmpty()) {
            for (ShoppingItem s : purchasedItems) {
                totalPrice = totalPrice + s.getItemPrice();
            }
        }
    }

    public void setTotalPrice(float f) {
        totalPrice = f;
    }
    public float getTotalPrice() {
        recalculateTotalPrice();
        return totalPrice;
    }
    public void cancelItem(String i) {
        ShoppingItem toCan = new ShoppingItem();
        for (ShoppingItem s : purchasedItems) {
            if (s.getKey().trim().equals(i.trim())) {
                toCan = s;
            }
        }
        purchasedItems.remove(toCan);
        recalculateTotalPrice();
    }
    public int getId() {
        return id;
    }
    public List<ShoppingItem> getItems() {
        return purchasedItems;
    }
    public void addItem(ShoppingItem s) {
        purchasedItems.add(s);
    }
    public void setId(int d) {
        id = d;
    }
    public void setUser(String u) {
        user = u;
    }
    public String getUser() {
        return user;
    }
    public void removeItem(int p) {
        purchasedItems.remove(p);
    }

}
