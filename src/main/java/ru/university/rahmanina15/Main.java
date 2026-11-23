package ru.university.rahmanina15;

import org.postgresql.ds.PGSimpleDataSource;
import ru.university.rahmanina15.controller.AllCmdController;
import ru.university.rahmanina15.controller.BookController;
import ru.university.rahmanina15.controller.GazetaController;
import ru.university.rahmanina15.controller.JournalController;
import ru.university.rahmanina15.exception.TechnicalException;
import ru.university.rahmanina15.repository.*;
import ru.university.rahmanina15.service.BookService;
import ru.university.rahmanina15.service.GazetaService;
import ru.university.rahmanina15.service.JournalService;

import javax.sql.DataSource;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        У меня чёто с кодировкой в консоли
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        DataSource dataSource = connectDB();
        initDB(dataSource);

        BookRepository bookRepository = new BookDatabaseRepository(dataSource);
        GazetaRepository gazetaRepository = new GazetaDatabaseRepository(dataSource);
        JournalRepository journalRepository = new JournalDatabaseRepository(dataSource);

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

    public static DataSource connectDB() {
        var datasource = new PGSimpleDataSource();
        datasource.setServerNames(new String[]{"localhost"});
        datasource.setPortNumbers(new int[]{5432});
        datasource.setDatabaseName("gazeta");
        datasource.setUser("gazeta");
        datasource.setPassword("gazeta");
        return datasource;
    }

    public static void initDB(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            var sql = """
                CREATE TABLE IF NOT EXISTS book(
                    id UUID PRIMARY KEY,
                    name TEXT NOT NULL,
                    author TEXT NOT NULL,
                    izdat TEXT NOT NULL,
                    pages_num INT NOT NULL,
                    quantity INT NOT NULL
                );

                CREATE TABLE IF NOT EXISTS gazeta(
                    id UUID PRIMARY KEY,
                    name TEXT NOT NULL,
                    number INT NOT NULL,
                    rel_date DATE NOT NULL,
                    quantity INT NOT NULL
                );

                CREATE TABLE IF NOT EXISTS journal(
                    id UUID PRIMARY KEY,
                    name TEXT NOT NULL,
                    number INT NOT NULL,
                    rel_date DATE NOT NULL,
                    pages_num INT NOT NULL,
                    quantity INT NOT NULL
                );
                """;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при инициализации базы данных", e);
        }
    }
}