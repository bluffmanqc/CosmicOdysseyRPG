package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


@Entity
public class GameMessage {
    public enum Type {
        NARRATION, DIALOGUE, SYSTEM, COMBAT, DICE_ROLL, IMAGE, CHOICE, SHOP, ERROR
    }

    @PrimaryKey


    @NonNull


    private String  id;;
    @Ignore
    private Type type;
    private String content;
    private String sender;
    private String imageUrl;
    private long timestamp;
    private boolean isPlayer;
    private boolean isRead;
    private String voiceText;

    public GameMessage() {
        this.timestamp = System.currentTimeMillis();
        this.isRead = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public boolean isPlayer() { return isPlayer; }
    public void setPlayer(boolean player) { isPlayer = player; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public String getVoiceText() { return voiceText; }
    public void setVoiceText(String voiceText) { this.voiceText = voiceText; }
}
