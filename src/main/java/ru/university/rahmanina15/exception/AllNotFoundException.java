package ru.university.rahmanina15.exception;

import java.util.UUID;

public class AllNotFoundException extends BusinessException {
    public AllNotFoundException(UUID id) {
        super("Товар не найден: id = " + id);
    }
}
