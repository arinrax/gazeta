package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.exception.TechnicalException;
import ru.university.rahmanina15.model.Journal;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JournalDatabaseRepository implements JournalRepository {
    private final DataSource dataSource;

    public JournalDatabaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Journal create(Journal journal) {
        var sql = """
            INSERT INTO journal(id, name, number, rel_date, pages_num, quantity)
            VALUES (?::UUID, ?, ?, ?, ?, ?)
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, journal.id().toString());
            statement.setString(2, journal.name());
            statement.setInt(3, journal.number());
            statement.setObject(4, journal.rel_date());
            statement.setInt(5, journal.pages_num());
            statement.setInt(6, journal.quantity());
            statement.executeUpdate();
            return journal;
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    @Override
    public Journal update(Journal journal) {
        var sql = """
            UPDATE journal
            SET name = ?, number = ?, rel_date = ?, pages_num = ?, quantity = ?
            WHERE id = ?::UUID
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, journal.name());
            statement.setInt(2, journal.number());
            statement.setObject(3, journal.rel_date());
            statement.setInt(4, journal.pages_num());
            statement.setInt(5, journal.quantity());
            statement.setString(6, journal.id().toString());
            statement.executeUpdate();
            return journal;
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    @Override
    public boolean delete(UUID id) {
        var sql = """
            DELETE FROM journal
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

    @Override
    public List<Journal> list() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            var sql = """
                SELECT j.id, j.name, j.number, j.rel_date, j.pages_num, j.quantity
                FROM journal j
                """;
            ResultSet rs = statement.executeQuery(sql);
            List<Journal> journals = new ArrayList<>();
            while (rs.next()) {
                journals.add(mapRowToJournal(rs));
            }
            return journals;
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    @Override
    public Optional<Journal> getById(UUID id) {
        var sql = """
            SELECT j.id, j.name, j.number, j.rel_date, j.pages_num, j.quantity
            FROM journal j
            WHERE j.id = ?::UUID
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToJournal(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    private Journal mapRowToJournal(ResultSet rs) throws SQLException {
        return new Journal(
                rs.getObject("id", UUID.class),
                rs.getString("name"),
                rs.getInt("number"),
                rs.getObject("rel_date", LocalDate.class),
                rs.getInt("pages_num"),
                rs.getInt("quantity")
        );
    }
}
