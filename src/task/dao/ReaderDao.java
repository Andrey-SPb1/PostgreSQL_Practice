package task.dao;

import task.dto.BookFilter;
import task.entity.Book;
import task.entity.Reader;
import task.exception.DaoException;
import task.util.ConnectionManager;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ReaderDao implements Dao<Integer, Reader> {

    private static final String FIND_BY_ID_SQL = """
            SELECT id, name, book_id, status
            FROM reader
            WHERE id = ?;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO reader (name, book_id, status)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE reader
            SET name = ?,
            book_id = ?,
            status = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM reader
            WHERE id = ?
            """;

    private static final String UPDATE_BOOK_SQL = """
            UPDATE reader
            SET book_id = ?
            WHERE id = ?
            """;

    private final BookDao bookDao = BookDao.getInstance();

    private static final ReaderDao INSTANCE = new ReaderDao();

    private ReaderDao() {
    }

    public static ReaderDao getInstance() {
        return INSTANCE;
    }

    public void takeBook(Reader reader, BookFilter bookFilter) {
        if(reader.getBook() == null && !reader.getStatus().equals("blacklist")) {
            Book book = bookDao.getBook(bookFilter).orElse(null);
            reader.setBook(book);
            if(book != null) {
                updateBook(reader, book);
            }
        }
    }

    @Override
    public Optional<Reader> findById(Integer id) {
        try(var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            Reader reader = null;
            if(resultSet.next()) {
                reader = new Reader(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        bookDao.findById(resultSet.getInt("book_id")).orElse(null),
                        resultSet.getString("status")
                );
            }
            return Optional.ofNullable(reader);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Reader> findAll() {
        return null;
    }

    @Override
    public void update(Reader reader) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, reader.getName());
            Integer bookId = reader.getBook() != null ? reader.getBook().getId() : null;
            preparedStatement.setObject(2, bookId);
            preparedStatement.setString(3, reader.getStatus());
            preparedStatement.setInt(4, reader.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void updateBook(Reader reader, Book book) {
        try(var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(UPDATE_BOOK_SQL)) {
            preparedStatement.setInt(1, book.getId());
            preparedStatement.setInt(2, reader.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Reader save(Reader reader) {
        try(var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, reader.getName());
            Integer bookId = reader.getBook() != null ? reader.getBook().getId() : null;
            preparedStatement.setObject(2, bookId);
            preparedStatement.setString(3, reader.getStatus());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                reader.setId(generatedKeys.getInt("id"));
            }

            return reader;
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
