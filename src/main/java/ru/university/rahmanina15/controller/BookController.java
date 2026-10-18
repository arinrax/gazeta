package ru.university.rahmanina15.controller;

import ru.university.rahmanina15.exception.BusinessException;
import ru.university.rahmanina15.model.Book;
import ru.university.rahmanina15.service.BookService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BookController {
    private final Scanner scanner;
    private final BookService bookService;

    public BookController(Scanner scanner, BookService bookService) {
        this.scanner = scanner;
        this.bookService = bookService;
    }

    public void run() {
        while (true) {
            System.out.println("\nКНИГИ");
            System.out.println("1. Показать все книги");
            System.out.println("2. Найти книгу по id");
            System.out.println("3. Добавить книгу");
            System.out.println("4. Редактировать книгу");
            System.out.println("5. Удалить книгу");
            System.out.println("6. Продать книгу");
            System.out.println("7. Приёмка книги");
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
        List<Book> all = bookService.getAll();
        if (all.isEmpty()) {
            System.out.println("Книг нет");
            return;
        }
        all.stream()
                .map(this::formatBook)
                .forEach(System.out::println);
    }

    private void showById() {
        UUID id = getUUID();
        bookService.getById(id)
                .map(this::formatBook)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Книги с данным id не найдено")
                );
    }

    private void create() {
        System.out.print("Введите название книги: ");
        String name = scanner.nextLine();

        System.out.print("Введите автора: ");
        String author = scanner.nextLine();

        System.out.print("Введите издательство: ");
        String izdat = scanner.nextLine();

        int pages_num = getPositiveInt("Введите количество страниц: ");
        int quantity = getNonNegativeInt("Введите количество экземпляторв: ");

        Book created = bookService.create(name, author, izdat, pages_num, quantity);
        System.out.println("Успешно создано: " + formatBook(created));
    }

    private void update() {
        UUID id = getUUID();
        bookService.getById(id)
                .ifPresentOrElse(
                        book -> {
                            System.out.print("Введите новое название: ");
                            String name = scanner.nextLine();

                            System.out.print("Введите нового автора: ");
                            String author = scanner.nextLine();

                            System.out.print("Введите новое издательство: ");
                            String izdat = scanner.nextLine();

                            int pages_num = getPositiveInt("Введите новое количество страниц: ");
                            int quantity = getNonNegativeInt("Введите новое количество экземпляров: ");

                            Book updated = new Book(id, name, author, izdat, pages_num, quantity);
                            Book saved = bookService.update(updated);
                            System.out.println("Книга обновлена: " + formatBook(saved));
                        },
                        () -> System.out.println("Книги с данным id не найдено")
                );
    }

    private void delete() {
        UUID id = getUUID();
        bookService.deleteById(id);
        System.out.println("Удалено с id = " + id);
    }

    private void sell() {
        UUID id = getUUID();
        int quantity = getPositiveInt("Введите количество для продажи: ");
        Book sold = bookService.sell(id, quantity);
        System.out.println("Продано, осталось: " + sold.quantity());
    }

    private void receive() {
        UUID id = getUUID();
        int amount = getPositiveInt("Введите количество для приёмки: ");
        Book received = bookService.receive(id, amount);
        System.out.println("Принято, стало: " + received.quantity());
    }

    private String formatBook(Book book) {
        return String.format("id=%s | Название: %s | Автор: %s | Издательство: %s | Страниц: %d | Остаток: %d",
                book.id(), book.name(), book.author(), book.izdat(), book.pages_num(), book.quantity());
    }

    private UUID getUUID() {
        while (true) {
            System.out.print("Введите id: ");
            String id = scanner.nextLine();
            try {
                return UUID.fromString(id);
            } catch (Exception e) {
                System.out.println("Неправильно указан id");
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
                System.out.println("Значение должно быть положиельным");
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
