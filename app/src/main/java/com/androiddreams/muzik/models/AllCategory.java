package com.androiddreams.muzik.models;

import java.util.List;

public class AllCategory {

    String categoryTitle;
    List<CategoryItemAA> categoryItemList;

    public AllCategory(String categoryTitle, List<CategoryItemAA> categoryItemList) {
        this.categoryTitle = categoryTitle;
        this.categoryItemList = categoryItemList;
    }

    public List<CategoryItemAA> getCategoryItemList() {
        return categoryItemList;
    }

    public void setCategoryItemList(List<CategoryItemAA> categoryItemList) {
        this.categoryItemList = categoryItemList;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
