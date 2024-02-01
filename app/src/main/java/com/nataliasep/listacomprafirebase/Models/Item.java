package com.nataliasep.listacomprafirebase.Models;

public class Item {

    private String name;
    private String img;
    //private boolean isPurchased;

    public Item() {
    }

    public Item(String name, String img) {
        this.name = name;
        this.img = img;
    }

    /*public Item(String name, boolean isPurchased, String img) {
        this.name = name;
        this.isPurchased = isPurchased;
        this.img = img;
    }*/
    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
