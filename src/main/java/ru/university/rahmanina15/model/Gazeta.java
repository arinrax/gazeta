package ru.university.rahmanina15.model;

import java.time.LocalDate;
import java.util.UUID;


public class Gazeta {
    private UUID id;
    private String name;
    private int number;
    private LocalDate rel_date;
    private int quantity;

    public Gazeta(String name, int number, LocalDate rel_date, int quantity){
        this.id = UUID.randomUUID();
        this.name = name;
        this.number = number;
        this.rel_date = rel_date;
        this.quantity = quantity;
    }

    public String getName(){
        return name;
    }

    public int getNumber(){
        return number;
    }

    public LocalDate getRel_date(){
        return rel_date;
    }

    public UUID getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public void setRel_date(LocalDate rel_date){
        this.rel_date = rel_date;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
