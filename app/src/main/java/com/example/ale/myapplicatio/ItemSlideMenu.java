package com.example.ale.myapplicatio;

/**
 * Created by Paola on 21/11/2016.
 */

public class ItemSlideMenu {
    private int imgId;
    private String title;

    public ItemSlideMenu(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getImgId() {
        return imgId;
    }

}
