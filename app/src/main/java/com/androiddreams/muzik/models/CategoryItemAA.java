package com.androiddreams.muzik.models;

public class CategoryItemAA {
    Integer itemId;
    Integer imageUrl;

    public CategoryItemAA(Integer itemId, Integer imageUrl) {
        this.itemId = itemId;
        this.imageUrl = imageUrl;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }
}
