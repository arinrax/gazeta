package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.model.Journal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface JournalRepository {
    Journal create(Journal journal);
    Journal update(Journal journal);
    boolean delete(UUID id);
    List<Journal> list();
    Optional<Journal> getById(UUID id);
}
