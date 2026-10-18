package ru.university.rahmanina15.controller;

import ru.university.rahmanina15.exception.BusinessException;
import ru.university.rahmanina15.model.Journal;
import ru.university.rahmanina15.service.JournalService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class JournalController {
    private final Scanner scanner;
    private final JournalService journalService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public JournalController(Scanner scanner, JournalService journalService) {
        this.scanner = scanner;
        this.journalService = journalService;
    }

    public void run() {
        while (true) {
            System.out.println("\n--- ЖУРНАЛЫ ---");
            System.out.println("1. Показать все журналы");
            System.out.println("2. Найти журнал по ID");
            System.out.println("3. Добавить журнал");
            System.out.println("4. Редактировать журнал");
            System.out.println("5. Удалить журнал");
            System.out.println("6. Продать журнал");
            System.out.println("7. Приёмка журнала");
            System.out.println("0. Назад");
            System.out.print("Выберите команду: ");

            String command = scanner.nextLine();

            try {
                switch (command) {
                    case "1":
                        showAll();
                        break;
                    case "2":
                        showById();
                        break;
                    case "3":
                        create();
                        break;
                    case "4":
                        update();
                        break;
                    case "5":
                        delete();
                        break;
                    case "6":
                        sell();
                        break;
                    case "7":
                        receive();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("Неправильно введена команда");
                }
            } catch (BusinessException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Непредвиденная ошибка: " + e.getMessage());
            }
        }
    }

    private void showAll() {
        List<Journal> all = journalService.getAll();
        if (all.isEmpty()) {
            System.out.println("Журналов нет");
            return;
        }
        all.stream()
                .map(this::formatJournal)
                .forEach(System.out::println);
    }

    private void showById() {
        UUID id = getUUID();
        journalService.getById(id)
                .map(this::formatJournal)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Журнала с заданным идентификатором не найдено")
                );
    }

    private void create() {
        System.out.print("Введите название журнала: ");
        String name = scanner.nextLine();

        int number = getPositiveInt("Введите номер выпуска: ");
        LocalDate rel_date = getDate("Введите дату выпуска (дд.ММ.гггг): ");
        int pages_num = getPositiveInt("Введите количество страниц: ");
        int quantity = getNonNegativeInt("Введите количество экземпляров: ");

        Journal created = journalService.create(name, number, rel_date, pages_num, quantity);
        System.out.println("Успешно создано: " + formatJournal(created));
    }

    private void update() {
        UUID id = getUUID();
        journalService.getById(id)
                .ifPresentOrElse(
                        journal -> {
                            System.out.print("Введите новое название: ");
                            String name = scanner.nextLine();

                            int number = getPositiveInt("Введите новый номер выпуска: ");
                            LocalDate rel_date = getDate("Введите новую дату выпуска (дд.ММ.гггг): ");
                            int pages_num = getPositiveInt("Введите новое количество страниц: ");
                            int quantity = getNonNegativeInt("Введите новое количество экземпляров: ");

                            Journal updated = new Journal(id, name, number, rel_date, pages_num, quantity);
                            Journal saved = journalService.update(updated);
                            System.out.println("Журнал обновлен: " + formatJournal(saved));
                        },
                        () -> System.out.println("Журнала с заданным идентификатором не найдено")
                );
    }

    private void delete() {
        UUID id = getUUID();
        journalService.deleteById(id);
        System.out.println("Успешно удалено с идентификатором = " + id);
    }

    private void sell() {
        UUID id = getUUID();
        int quantity = getPositiveInt("Введите количество для продажи: ");
        Journal sold = journalService.sell(id, quantity);
        System.out.println("Успешно продано. Осталось: " + sold.quantity());
    }

    private void receive() {
        UUID id = getUUID();
        int amount = getPositiveInt("Введите количество для приёмки: ");
        Journal received = journalService.receive(id, amount);
        System.out.println("Успешно принято. Стало: " + received.quantity());
    }

    private String formatJournal(Journal journal) {
        return String.format("id=%s | Название: %s | Номер: %d | Дата: %s | Страниц: %d | Остаток: %d",
                journal.id(), journal.name(), journal.number(),
                journal.rel_date().format(formatter), journal.pages_num(), journal.quantity());
    }

    private UUID getUUID() {
        while (true) {
            System.out.print("Введите идентификатор: ");
            String id = scanner.nextLine();
            try {
                return UUID.fromString(id);
            } catch (Exception e) {
                System.out.println("Неправильно указан идентификатор");
            }
        }
    }

    private LocalDate getDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Неправильный формат даты. Используйте дд.ММ.гггг");
            }
        }
    }

    private int getPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("Значение должно быть положительным");
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число");
            }
        }
    }

    private int getNonNegativeInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value >= 0) {
                    return value;
                }
                System.out.println("Значение не может быть отрицательным");
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число");
            }
        }
    }
}
