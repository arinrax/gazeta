package ru.university.rahmanina15.controller;

import ru.university.rahmanina15.exception.BusinessException;
import ru.university.rahmanina15.model.Gazeta;
import ru.university.rahmanina15.service.GazetaService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class GazetaController {
    private final Scanner scanner;
    private final GazetaService gazetaService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public GazetaController(Scanner scanner, GazetaService gazetaService) {
        this.scanner = scanner;
        this.gazetaService = gazetaService;
    }

    public void run() {
        while (true) {
            System.out.println("\nГАЗЕТЫ");
            System.out.println("1. Показать все газеты");
            System.out.println("2. Найти газету по ID");
            System.out.println("3. Добавить газету");
            System.out.println("4. Редактировать газету");
            System.out.println("5. Удалить газету");
            System.out.println("6. Продать газету");
            System.out.println("7. Приёмка газеты");
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
        List<Gazeta> all = gazetaService.getAll();
        if (all.isEmpty()) {
            System.out.println("Газет нет");
            return;
        }
        all.stream()
                .map(this::formatGazeta)
                .forEach(System.out::println);
    }

    private void showById() {
        if (gazetaService.getAll().isEmpty()){
            System.out.println("Газет нет");
            return;
        }
        UUID id = getUUID();
        gazetaService.getById(id)
                .map(this::formatGazeta)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Газеты с заданным идентификатором не найдено")
                );
    }

    private void create() {
        System.out.print("Введите название газеты: ");
        String name = scanner.nextLine();

        int number = getPositiveInt("Введите номер выпуска: ");
        LocalDate rel_date = getDate("Введите дату выпуска (дд.мм.гггг): ");
        int quantity = getPositiveInt("Введите количество экземпляров: ");

        Gazeta created = gazetaService.create(name, number, rel_date, quantity);
        System.out.println("Успешно создано: " + formatGazeta(created));
    }

    private void update() {
        UUID id = getUUID();
        gazetaService.getById(id)
                .ifPresentOrElse(
                        gazeta -> {
                            System.out.print("Введите новое название: ");
                            String name = scanner.nextLine();

                            int number = getPositiveInt("Введите новый номер выпуска: ");
                            LocalDate rel_date = getDate("Введите новую дату выпуска (дд.мм.гггг): ");
                            int quantity = getPositiveInt("Введите новое количество экземпляров: ");

                            Gazeta updated = new Gazeta(id, name, number, rel_date, quantity);
                            Gazeta saved = gazetaService.update(updated);
                            System.out.println("Газета обновлена: " + formatGazeta(saved));
                        },
                        () -> System.out.println("Газеты с заданным идентификатором не найдено")
                );
    }

    private void delete() {
        UUID id = getUUID();
        gazetaService.deleteById(id);
        System.out.println("Успешно удалено с идентификатором = " + id);
    }

    private void sell() {
        UUID id = getUUID();
        int quantity = getPositiveInt("Введите количество для продажи: ");
        Gazeta sold = gazetaService.sell(id, quantity);
        System.out.println("Успешно продано. Осталось: " + sold.quantity());
    }

    private void receive() {
        UUID id = getUUID();
        int amount = getPositiveInt("Введите количество для приёмки: ");
        Gazeta received = gazetaService.receive(id, amount);
        System.out.println("Успешно принято. Стало: " + received.quantity());
    }

    private String formatGazeta(Gazeta gazeta) {
        return String.format("id=%s | Название: %s | Номер: %d | Дата: %s | Остаток: %d",
                gazeta.id(), gazeta.name(), gazeta.number(),
                gazeta.rel_date().format(formatter), gazeta.quantity());
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
                System.out.println("Неправильный формат даты. Используйте дд.мм.гггг");
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
}
