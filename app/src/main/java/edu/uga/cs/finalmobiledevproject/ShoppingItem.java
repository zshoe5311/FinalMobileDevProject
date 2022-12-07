package edu.uga.cs.finalmobiledevproject;

public class ShoppingItem {
    //private String id; //will be int
    private String itemName;
    private String itemPrice; //will be float
    private String purchasedBasketId; //will be int
    private String inBasketOf; //will be String, or enum possibly
    private String description;
    private String key;

    public ShoppingItem() {
        this.key = null;
        //this.id = null;
        this.itemName = null;
        this.itemPrice = null;
        this.purchasedBasketId = null;
        this.description = null;
        this.inBasketOf = null;
    }

    /*public ShoppingItem (String ide, String n, String p, String i, String d, String b) {
        this.key = null;
        this.id = ide;
        this.itemName = n;
        this.itemPrice = p;
        this.purchasedBasketId = i;
        this.description = d;
        this.inBasketOf = b;
    }*/

    public ShoppingItem (String n, String p, String i, String d, String b) {
        this.key = null;
        //this.id = null;
        this.itemName = n;
        this.itemPrice = p;
        this.purchasedBasketId = i;
        this.description = d;
        this.inBasketOf = b;
    }

    /*public int getId() {
        return Integer.parseInt(id);
    }
    public void setId(int i) {
        id = i + "";
    }*/
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String in) {
        itemName = in;
    }
    public float getItemPrice() {
        return Float.parseFloat(itemPrice);
    }
    public void setItemPrice(float ip) {
        itemPrice = ip + "";
    }
    public int getPurchasedBasketId() { //might run into some problems here, remember to store this attr
        return Integer.parseInt(purchasedBasketId);//in the database this way
    }
    public void setPurchasedBasketId(int i) {
        purchasedBasketId = i + "";
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String d) {
        description = d;
    }
    public String getBasketingUser() {
        return inBasketOf;
    }
    public void setBasketingUser(String u) {
        inBasketOf = u;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String s) {
        key = s;
    }
}

