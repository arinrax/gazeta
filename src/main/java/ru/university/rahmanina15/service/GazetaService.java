package ru.university.rahmanina15.service;

import ru.university.rahmanina15.exception.*;
import ru.university.rahmanina15.model.Gazeta;
import ru.university.rahmanina15.repository.GazetaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GazetaService {
    private final GazetaRepository repo;

    public GazetaService(GazetaRepository repo) {
        this.repo = repo;
    }

    public Gazeta create(String name, int number, LocalDate rel_date, int quantity) {
        if (name.isBlank()) {
            throw new EmptyNameException();
        }
        if (number <= 0) {
            throw new NegativeNumberException(number);
        }
        if (quantity < 0) {
            throw new NegativeQuantityException(quantity);
        }

        var id = UUID.randomUUID();
        var gazeta = new Gazeta(id, name, number, rel_date, quantity);
        repo.create(gazeta);
        return gazeta;
    }

    public Gazeta update(Gazeta newGazeta) {
        if (newGazeta.name().isBlank()) {
            throw new EmptyNameException();
        }
        if (newGazeta.number() <= 0) {
            throw new NegativeNumberException(newGazeta.number());
        }
        if (newGazeta.quantity() < 0) {
            throw new NegativeQuantityException(newGazeta.quantity());
        }

        repo.update(newGazeta);
        return newGazeta;
    }

    public boolean deleteById(UUID id) {
        if (!repo.delete(id)) {
            throw new AllNotFoundException(id);
        }
        return true;
    }

    public Optional<Gazeta> getById(UUID id) {
        return repo.getById(id);
    }

    public List<Gazeta> getAll() {
        return repo.list();
    }

    public Gazeta sell(UUID id, int requestedQuantity) {
        Gazeta gazeta = repo.getById(id)
                .orElseThrow(() -> new AllNotFoundException(id));

        if (gazeta.quantity() < requestedQuantity) {
            throw new NotInStockException(requestedQuantity, gazeta.quantity());
        }

        Gazeta updated = new Gazeta(
                gazeta.id(),
                gazeta.name(),
                gazeta.number(),
                gazeta.rel_date(),
                gazeta.quantity() - requestedQuantity
        );
        repo.update(updated);
        return updated;
    }

    public Gazeta receive(UUID id, int amount) {
        Gazeta gazeta = repo.getById(id)
                .orElseThrow(() -> new AllNotFoundException(id));

        if (amount <= 0) {
            throw new NegativeQuantityException(amount);
        }

        Gazeta updated = new Gazeta(
                gazeta.id(),
                gazeta.name(),
                gazeta.number(),
                gazeta.rel_date(),
                gazeta.quantity() + amount
        );
        repo.update(updated);
        return updated;
    }
}