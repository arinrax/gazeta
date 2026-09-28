package ru.university.rahmanina15.model;

import java.time.LocalDate;

public class Journal {
    private String name;
    private int number;
    private LocalDate rel_date;
    private int pages_num;

    public Journal(String name, int number, LocalDate rel_date, int pages_num){
        this.name = name;
        this.number = number;
        this.rel_date = rel_date;
        this.pages_num = pages_num;
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

    public int getPages_num(){
        return pages_num;
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

    public void setPages_num(int pages_num){
        this.pages_num = pages_num;
    }
}
