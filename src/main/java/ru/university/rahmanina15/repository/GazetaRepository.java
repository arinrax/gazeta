package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.model.Gazeta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface GazetaRepository {
    Gazeta create(Gazeta gazeta);
    Gazeta update(Gazeta gazeta);
    boolean delete(UUID id);
    List<Gazeta> list();
    Optional<Gazeta> getById(UUID id);
}
