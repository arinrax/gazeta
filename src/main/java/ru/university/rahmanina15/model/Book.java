package ru.university.rahmanina15.model;

import java.util.UUID;

public class Book {
    private UUID id;
    private String name;
    private String author;
    private String izdat;
    private int pages_num;
    private int quantity;

    public Book(String name, String author, String izdat, int pages_num, int quantity){
        this.id = UUID.randomUUID();
        this.name = name;
        this.author = author;
        this.izdat = izdat;
        this.pages_num = pages_num;
        this.quantity = quantity;
    }

    public String getName(){
        return name;
    }

    public String getAuthor(){
        return author;
    }

    public String getIzdat(){
        return izdat;
    }

    public int getPages_num(){
        return pages_num;
    }

    public UUID getId(){
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setIzdat(String izdat){
        this.izdat = izdat;
    }

    public void setPages_num(int pages_num){
        this.pages_num = pages_num;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
