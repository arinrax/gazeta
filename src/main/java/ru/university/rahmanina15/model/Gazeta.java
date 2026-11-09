package ru.university.rahmanina15.model;

import java.time.LocalDate;
import java.util.UUID;

public record Gazeta(
        UUID id,
        String name,
        int number,
        LocalDate rel_date,
        int quantity
) {}

