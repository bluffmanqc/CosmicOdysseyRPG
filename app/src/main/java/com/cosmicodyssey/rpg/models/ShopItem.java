package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


@Entity
public class ShopItem {
    @PrimaryKey

    @NonNull

    private String  id;;
    private String name;
    private String description;
    private String type;
    @Ignore
    private Rarity rarity;
    private int price;
    private String imageUrl;
    private String merchantName;
    private String merchantImageUrl;
    private String category;
    private boolean available;
    @Ignore
    private Object item;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Rarity getRarity() { return rarity; }
    public void setRarity(Rarity rarity) { this.rarity = rarity; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }
    public String getMerchantImageUrl() { return merchantImageUrl; }
    public void setMerchantImageUrl(String merchantImageUrl) { this.merchantImageUrl = merchantImageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public Object getItem() { return item; }
    public void setItem(Object item) { this.item = item; }
}
