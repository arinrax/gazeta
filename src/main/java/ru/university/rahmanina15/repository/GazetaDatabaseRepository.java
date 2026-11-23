package ru.university.rahmanina15.repository;

import ru.university.rahmanina15.exception.TechnicalException;
import ru.university.rahmanina15.model.Gazeta;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GazetaDatabaseRepository implements GazetaRepository {
    private final DataSource dataSource;

    public GazetaDatabaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Gazeta create(Gazeta gazeta) {
        var sql = """
            INSERT INTO gazeta(id, name, number, rel_date, quantity)
            VALUES (?::UUID, ?, ?, ?, ?)
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, gazeta.id().toString());
            statement.setString(2, gazeta.name());
            statement.setInt(3, gazeta.number());
            statement.setObject(4, gazeta.rel_date());
            statement.setInt(5, gazeta.quantity());
            statement.executeUpdate();
            return gazeta;
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    @Override
    public Gazeta update(Gazeta gazeta) {
        var sql = """
            UPDATE gazeta
            SET name = ?, number = ?, rel_date = ?, quantity = ?
            WHERE id = ?::UUID
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, gazeta.name());
            statement.setInt(2, gazeta.number());
            statement.setObject(3, gazeta.rel_date());
            statement.setInt(4, gazeta.quantity());
            statement.setString(5, gazeta.id().toString());
            statement.executeUpdate();
            return gazeta;
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    @Override
    public boolean delete(UUID id) {
        var sql = """
            DELETE FROM gazeta
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
    public List<Gazeta> list() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            var sql = """
                SELECT g.id, g.name, g.number, g.rel_date, g.quantity
                FROM gazeta g
                """;
            ResultSet rs = statement.executeQuery(sql);
            List<Gazeta> gazetas = new ArrayList<>();
            while (rs.next()) {
                gazetas.add(mapRowToGazeta(rs));
            }
            return gazetas;
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    @Override
    public Optional<Gazeta> getById(UUID id) {
        var sql = """
            SELECT g.id, g.name, g.number, g.rel_date, g.quantity
            FROM gazeta g
            WHERE g.id = ?::UUID
            """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToGazeta(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new TechnicalException("Ошибка при работе с базой данных", e);
        }
    }

    private Gazeta mapRowToGazeta(ResultSet rs) throws SQLException {
        return new Gazeta(
                rs.getObject("id", UUID.class),
                rs.getString("name"),
                rs.getInt("number"),
                rs.getObject("rel_date", LocalDate.class),
                rs.getInt("quantity")
        );
    }
}
