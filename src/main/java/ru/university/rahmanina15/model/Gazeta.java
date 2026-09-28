package ru.university.rahmanina15.model;

import java.time.LocalDate;


public class Gazeta {
    private String name;
    private int number;
    private LocalDate rel_date;

    public Gazeta(String name, int number, LocalDate rel_date){
        this.name = name;
        this.number = number;
        this.rel_date = rel_date;
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

    public void setName(String name){
        this.name = name;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public void setRel_date(LocalDate rel_date){
        this.rel_date = rel_date;
    }
}
