package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.model.Book;
import java.util.*;

public class BookRepo {
    private final Map<UUID, Book> data;

    public BookRepo(Map<UUID, Book> data) {
        this.data = data;
    }

    public BookRepo() {
        this.data = new HashMap<>();
    }

    public void create(Book book) {
        var id = book.getId();
        data.put(id, book);
    }

    public void update(Book book) {
        var id = book.getId();
        data.put(id, book);
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
