package ru.university.rahmanina15.model;

import java.util.UUID;

public record Book(
        UUID id,
        String name,
        String author,
        String izdat,
        int pages_num,
        int quantity
) {}

