package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.exception.TechnicalException;
import ru.university.rahmanina15.model.Book;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookDatabaseRepository implements BookRepository {
    private final DataSource dataSource;

    public BookDatabaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Book create(Book book) {
        var sql = """
            INSERT INTO book(id, name, author, izdat, pages_num, quantity)
            VALUES (?::UUID, ?, ?, ?, ?, ?)
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.id().toString());
            statement.setString(2, book.name());
            statement.setString(3, book.author());
            statement.setString(4, book.izdat());
            statement.setInt(5, book.pages_num());
            statement.setInt(6, book.quantity());
            statement.executeUpdate();
            return book;
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    public Book update(Book book) {
        var sql = """
            UPDATE book
            SET name = ?, author = ?, izdat = ?, pages_num = ?, quantity = ?
            WHERE id = ?::UUID
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.name());
            statement.setString(2, book.author());
            statement.setString(3, book.izdat());
            statement.setInt(4, book.pages_num());
            statement.setInt(5, book.quantity());
            statement.setString(6, book.id().toString());
            statement.executeUpdate();
            return book;
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    public boolean delete(UUID id) {
        var sql = """
            DELETE FROM book
            WHERE id = ?::UUID
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            int deletedRows = statement.executeUpdate();
            return deletedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Book> list() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            var sql = """
                SELECT b.id, b.name, b.author, b.izdat, b.pages_num, b.quantity
                FROM book b
                """;
            ResultSet rs = statement.executeQuery(sql);
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
            return books;
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    public Optional<Book> getById(UUID id) {
        var sql = """
            SELECT b.id, b.name, b.author, b.izdat, b.pages_num, b.quantity
            FROM book b
            WHERE b.id = ?::UUID
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToBook(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getObject("id", UUID.class),
                rs.getString("name"),
                rs.getString("author"),
                rs.getString("izdat"),
                rs.getInt("pages_num"),
                rs.getInt("quantity")
        );
    }
}
