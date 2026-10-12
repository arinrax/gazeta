package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.model.Journal;
import java.util.*;

public class JournalInMemoryRepository {
    private final Map<UUID, Journal> data;

    public JournalInMemoryRepository(Map<UUID, Journal> data) {
        this.data = data;
    }

    public JournalInMemoryRepository() {
        this.data = new HashMap<>();
    }

    public Journal create(Journal journal) {
        var id = journal.getId();
        data.put(id, journal);
        return journal;
    }

    public Journal update(Journal journal) {
        var id = journal.getId();
        data.put(id, journal);
        return journal;
    }

    public Optional<Journal> getById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    public List<Journal> list() {
        return data.values().stream().toList();
    }
}
