package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


import java.util.UUID;

@Entity
public class MarketListing {
    @PrimaryKey

    @NonNull

    private String  id;;
    private String itemId;
    private String itemName;
    private String itemType;
    private String itemImageUrl;
    @Ignore
    private Rarity rarity;
    private int price;
    private String sellerId;
    private String sellerName;
    private long createdAt;
    private boolean sold;
    @Ignore
    private Object item;

    public MarketListing() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.sold = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
    public String getItemImageUrl() { return itemImageUrl; }
    public void setItemImageUrl(String itemImageUrl) { this.itemImageUrl = itemImageUrl; }
    public Rarity getRarity() { return rarity; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public boolean isSold() { return sold; }
    public void setSold(boolean sold) { this.sold = sold; }
    public Object getItem() { return item; }
    public void setItem(Object item) { this.item = item; }
}
