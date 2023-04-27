package task.dao;

import task.entity.Blacklist;
import task.entity.Book;
import task.entity.Reader;
import task.exception.DaoException;
import task.util.ConnectionManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BlacklistDao implements Dao <Integer, Blacklist> {

    private static final BlacklistDao INSTANCE = new BlacklistDao();

    private static final String FIND_ALL_SQL = """
            SELECT id, reader_id
            FROM blacklist
            """;

    private static final String SAVE_SQL = """
            INSERT INTO blacklist (reader_id)
            VALUES (?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM blacklist
            WHERE id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE blacklist
            SET reader_id = ?
            WHERE id = ?
            """;

    private BlacklistDao() {
    }

    ReaderDao readerDao = ReaderDao.getInstance();
    public static BlacklistDao getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Blacklist> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Blacklist> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Blacklist> blacklists = new ArrayList<>();
            while(resultSet.next()) {
                blacklists.add(new Blacklist(
                        resultSet.getInt("id"),
                        readerDao.findById(resultSet.getInt("id")).orElse(null)
                ));
            }
            return blacklists;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Blacklist blacklist) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, blacklist.getReader().getId());
            preparedStatement.setInt(2, blacklist.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Blacklist save(Blacklist blacklist) {
        try(var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            Integer readerId = blacklist.getReader() != null ? blacklist.getReader().getId() : null;
            preparedStatement.setObject(1, readerId);
            var reader = readerDao.findById(readerId).orElse(null);
            if(reader != null) {
                reader.setStatus("blacklist");
                readerDao.update(reader);
            }
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                blacklist.setId(generatedKeys.getInt("id"));
            }

            return blacklist;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
