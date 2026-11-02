package ru.university.rahmanina15;

import ru.university.rahmanina15.controller.AllCmdController;
import ru.university.rahmanina15.controller.BookController;
import ru.university.rahmanina15.controller.GazetaController;
import ru.university.rahmanina15.controller.JournalController;
import ru.university.rahmanina15.repository.BookInMemoryRepository;
import ru.university.rahmanina15.repository.BookRepository;
import ru.university.rahmanina15.repository.GazetaInMemoryRepository;
import ru.university.rahmanina15.repository.GazetaRepository;
import ru.university.rahmanina15.repository.JournalInMemoryRepository;
import ru.university.rahmanina15.repository.JournalRepository;
import ru.university.rahmanina15.service.BookService;
import ru.university.rahmanina15.service.GazetaService;
import ru.university.rahmanina15.service.JournalService;

import java.util.Scanner;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
//        У меня чёто с кодировкой в консоли
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        BookRepository bookRepository = new BookInMemoryRepository();
        GazetaRepository gazetaRepository = new GazetaInMemoryRepository();
        JournalRepository journalRepository = new JournalInMemoryRepository();

        BookService bookService = new BookService(bookRepository);
        GazetaService gazetaService = new GazetaService(gazetaRepository);
        JournalService journalService = new JournalService(journalRepository);

        Scanner scanner = new Scanner(System.in);
        BookController bookController = new BookController(scanner, bookService);
        GazetaController gazetaController = new GazetaController(scanner, gazetaService);
        JournalController journalController = new JournalController(scanner, journalService);
        AllCmdController controller = new AllCmdController(
                scanner, bookController, gazetaController, journalController
        );

        controller.run();
    }
}