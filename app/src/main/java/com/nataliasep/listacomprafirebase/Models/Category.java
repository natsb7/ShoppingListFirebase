package com.nataliasep.listacomprafirebase.Models;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String name;

    private String img;

    private List<Item> items;

    public Category() {
        }

    public Category(String name, String img){
        this.name = name;
        this.img = img;
    }


    public String getName(){
        return name;
    }

    public String getImg(){
        return img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
