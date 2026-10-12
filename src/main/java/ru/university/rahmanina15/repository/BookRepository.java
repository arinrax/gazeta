package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {
    Book create(Book book);
    Book update(Book book);
    boolean delete(UUID id);
    List<Book> list();
    Optional<Book> getById(UUID id);
}
