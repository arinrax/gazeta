package ru.university.rahmanina15.service;

import ru.university.rahmanina15.exception.*;
import ru.university.rahmanina15.model.Journal;
import ru.university.rahmanina15.repository.JournalRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JournalService {
    private final JournalRepository repo;

    public JournalService(JournalRepository repo) {
        this.repo = repo;
    }

    public Journal create(String name, int number, LocalDate rel_date, int pages_num, int quantity) {
        if (name.isBlank()) {
            throw new EmptyNameException();
        }
        if (number <= 0) {
            throw new NegativeNumberException(number);
        }
        if (pages_num <= 0) {
            throw new NegativePagesException(pages_num);
        }
        if (quantity < 0) {
            throw new NegativeQuantityException(quantity);
        }

        var id = UUID.randomUUID();
        var journal = new Journal(id, name, number, rel_date, pages_num, quantity);
        repo.create(journal);
        return journal;
    }

    public Journal update(Journal newJournal) {
        if (newJournal.name().isBlank()) {
            throw new EmptyNameException();
        }
        if (newJournal.number() <= 0) {
            throw new NegativeNumberException(newJournal.number());
        }
        if (newJournal.pages_num() <= 0) {
            throw new NegativePagesException(newJournal.pages_num());
        }
        if (newJournal.quantity() < 0) {
            throw new NegativeQuantityException(newJournal.quantity());
        }

        repo.update(newJournal);
        return newJournal;
    }

    public boolean deleteById(UUID id) {
        if (!repo.delete(id)) {
            throw new AllNotFoundException(id);
        }
        return true;
    }

    public Optional<Journal> getById(UUID id) {
        return repo.getById(id);
    }

    public List<Journal> getAll() {
        return repo.list();
    }

    public Journal sell(UUID id, int requestedQuantity) {
        Journal journal = repo.getById(id)
                .orElseThrow(() -> new AllNotFoundException(id));

        if (journal.quantity() < requestedQuantity) {
            throw new NotInStockException(requestedQuantity, journal.quantity());
        }

        Journal updated = new Journal(
                journal.id(),
                journal.name(),
                journal.number(),
                journal.rel_date(),
                journal.pages_num(),
                journal.quantity() - requestedQuantity
        );
        repo.update(updated);
        return updated;
    }

    public Journal receive(UUID id, int amount) {
        Journal journal = repo.getById(id)
                .orElseThrow(() -> new AllNotFoundException(id));

        if (amount <= 0) {
            throw new NegativeQuantityException(amount);
        }

        Journal updated = new Journal(
                journal.id(),
                journal.name(),
                journal.number(),
                journal.rel_date(),
                journal.pages_num(),
                journal.quantity() + amount
        );
        repo.update(updated);
        return updated;
    }
}
