package ru.university.rahmanina15.service;

import ru.university.rahmanina15.exception.*;
import ru.university.rahmanina15.model.Book;
import ru.university.rahmanina15.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public Book create(String name, String author, String izdat, int pages_num, int quantity) {
        if (name.isBlank()) {
            throw new EmptyNameException();
        }
        if (author.isBlank()) {
            throw new EmptyAuthorException();
        }
        if (izdat.isBlank()) {
            throw new EmptyIzdatException();
        }
        if (pages_num <= 0) {
            throw new NegativePagesException(pages_num);
        }
        if (quantity < 0) {
            throw new NegativeQuantityException(quantity);
        }

        var id = UUID.randomUUID();
        var book = new Book(id, name, author, izdat, pages_num, quantity);
        repo.create(book);
        return book;
    }

    public Book update(Book newBook) {
        if (newBook.name().isBlank()) {
            throw new EmptyNameException();
        }
        if (newBook.author().isBlank()) {
            throw new EmptyAuthorException();
        }
        if (newBook.izdat().isBlank()) {
            throw new EmptyIzdatException();
        }
        if (newBook.pages_num() <= 0) {
            throw new NegativePagesException(newBook.pages_num());
        }
        if (newBook.quantity() < 0) {
            throw new NegativeQuantityException(newBook.quantity());
        }

        repo.update(newBook);
        return newBook;
    }

    public boolean deleteById(UUID id) {
        if (!repo.delete(id)) {
            throw new AllNotFoundException(id);
        }
        return true;
    }

    public Optional<Book> getById(UUID id) {
        return repo.getById(id);
    }

    public List<Book> getAll() {
        return repo.list();
    }

    public Book sell(UUID id, int requestedQuantity) {
        Book book = repo.getById(id)
                .orElseThrow(() -> new AllNotFoundException(id));

        if (book.quantity() < requestedQuantity) {
            throw new NotInStockException(requestedQuantity, book.quantity());
        }

        Book updated = new Book(
                book.id(),
                book.name(),
                book.author(),
                book.izdat(),
                book.pages_num(),
                book.quantity() - requestedQuantity
        );
        repo.update(updated);
        return updated;
    }

    public Book receive(UUID id, int amount) {
        Book book = repo.getById(id)
                .orElseThrow(() -> new AllNotFoundException(id));

        if (amount <= 0) {
            throw new NegativeQuantityException(amount);
        }

        Book updated = new Book(
                book.id(),
                book.name(),
                book.author(),
                book.izdat(),
                book.pages_num(),
                book.quantity() + amount
        );
        repo.update(updated);
        return updated;
    }
}
