package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.model.Book;
import java.util.*;

public class BookInMemoryRepository {
    private final Map<UUID, Book> data;

    public BookInMemoryRepository(Map<UUID, Book> data) {
        this.data = data;
    }

    public BookInMemoryRepository() {
        this.data = new HashMap<>();
    }

    public Book create(Book book) {
        var id = book.getId();
        data.put(id, book);
        return book;
    }

    public Book update(Book book) {
        var id = book.getId();
        data.put(id, book);
        return book;
    }

    public Optional<Book> getById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    public List<Book> list() {
        return data.values().stream().toList();
    }
}
