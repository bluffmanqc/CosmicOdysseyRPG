package com.cosmicodyssey.rpg.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Rule {
    @PrimaryKey

    @NonNull

    private String  id;;
    private String title;
    private String category;
    private String content;
    private String imageUrl;

    @Ignore
    private List<String> examples;
    private String source;
    private String authorId;
    private long createdAt;
    private boolean isOfficial;
    private int pageNumber;

    public Rule() {
        this.id = UUID.randomUUID().toString();
        this.examples = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.isOfficial = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<String> getExamples() { return examples; }
    public void setExamples(List<String> examples) { this.examples = examples; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public boolean isOfficial() { return isOfficial; }
    public void setOfficial(boolean official) { isOfficial = official; }
    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
}
