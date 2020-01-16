package com.abhishek.retrofitv2;

class ExpandedMenuModel {
    String name;
    int image = R.drawable.ic_navigate_next_black_24dp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
