package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.model.Gazeta;
import java.util.*;

public class GazetaInMemoryRepository {
    private final Map<UUID, Gazeta> data;

    public GazetaInMemoryRepository(Map<UUID, Gazeta> data) {
        this.data = data;
    }

    public GazetaInMemoryRepository() {
        this.data = new HashMap<>();
    }

    public Gazeta create(Gazeta gazeta) {
        var id = gazeta.getId();
        data.put(id, gazeta);
        return gazeta;
    }

    public Gazeta update(Gazeta gazeta) {
        var id = gazeta.getId();
        data.put(id, gazeta);
        return gazeta;
    }

    public Optional<Gazeta> getById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }

    public List<Gazeta> list() {
        return data.values().stream().toList();
    }
}
