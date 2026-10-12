package ru.university.rahmanina15.service;

import ru.university.rahmanina15.model.Book;
import ru.university.rahmanina15.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo){
        this.repo = repo;
    }

    public Book create(String name, String author, String izdat, int pages_num, int quantity) {

    }
}
