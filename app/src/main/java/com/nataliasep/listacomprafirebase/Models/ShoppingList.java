package com.nataliasep.listacomprafirebase.Models;

import java.util.ArrayList;

public class ShoppingList {
    private String name;
    private String date;
    private ArrayList<ItemList> items;

    public ShoppingList() {
    }
    public ShoppingList(String name){
        this.name = name;
    }

    public ShoppingList(String name, String date){
        this.name = name;
        this.date = date;
    }

    public ShoppingList(String name, String date, ArrayList<ItemList> items){
        this.name = name;
        this.date = date;
        this.items = items;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ItemList> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemList> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "ShoppingList{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
