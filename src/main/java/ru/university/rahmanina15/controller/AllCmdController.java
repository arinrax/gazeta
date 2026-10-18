package ru.university.rahmanina15.controller;

import ru.university.rahmanina15.service.BookService;
import ru.university.rahmanina15.service.GazetaService;
import ru.university.rahmanina15.service.JournalService;

import java.util.Scanner;

public class AllCmdController {
    private final Scanner scanner;
    private final BookController bookController;
    private final GazetaController gazetaController;
    private final JournalController journalController;

    public AllCmdController(Scanner scanner,
                            BookService bookService,
                            GazetaService gazetaService,
                            JournalService journalService) {
        this.scanner = scanner;
        this.bookController = new BookController(scanner, bookService);
        this.gazetaController = new GazetaController(scanner, gazetaService);
        this.journalController = new JournalController(scanner, journalService);
    }

    public void run() {
        while (true) {
            System.out.println("\nКИОСК");
            System.out.println("1. Книги");
            System.out.println("2. Газеты");
            System.out.println("3. Журналы");
            System.out.println("0. Выход");
            System.out.print("Выберите тип товара: ");

            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    bookController.run();
                    break;
                case "2":
                    gazetaController.run();
                    break;
                case "3":
                    journalController.run();
                    break;
                case "0":
                    System.out.println("Выход из программы");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неправильно введена команда");
            }
        }
    }
}