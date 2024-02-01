package com.nataliasep.listacomprafirebase.Models;

public class ItemList {
    private String itemName;
    private String img;

    public ItemList() {
    }

    public ItemList(String itemName, String img) {
        this.itemName = itemName;
        this.img = img;
    }


    public String getItem() {
        return itemName;
    }

    public String getImg() {
        return img;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setImg(String img) {
        this.img = img;
    }


    @Override
    public String toString() {
        return "ItemList{" +
                "itemName='" + itemName + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
