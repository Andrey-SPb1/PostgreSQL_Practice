package task.dao;

import task.dto.BookFilter;
import task.entity.Book;
import task.exception.DaoException;
import task.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao implements Dao<Integer, Book> {
    private static final String FIND_ALL_SQL = """
            SELECT id, name, author, year_of_publishing, status
            FROM book
            """;

    private static final String UPDATE_SQL = """
            UPDATE book
            SET name = ?,
            author = ?,
            year_of_publishing = ?,
            status = ?
            WHERE id = ?
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String FIND_BOOK_SQL = FIND_ALL_SQL + """
            WHERE name = ? AND author = ? AND status = 'available';
            """;

    private static final String SAVE_SQL = """
            INSERT INTO book (name, author, year_of_publishing, status)
            VALUES (?, ?, ?, ?)
            """;

    private static final String DELETE_SQL = """
            DELETE FROM book
            WHERE id = ?
            """;

    private static final String UPDATE_STATUS_SQL = """
            UPDATE book
            SET status = ?
            WHERE id = ?
            """;

    private static final BookDao INSTANCE = new BookDao();

    private BookDao() {
    }

    public static BookDao getInstance() {
        return INSTANCE;
    }

    public Optional<Book> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            Book book = null;
            if(resultSet.next()) {
                book = buildBook(resultSet);
            }
            return Optional.ofNullable(book);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            var resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();
            while(resultSet.next()) {
                books.add(buildBook(resultSet));
            }
            return books;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Book> getBook(BookFilter bookFilter) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BOOK_SQL)) {
            preparedStatement.setString(1, bookFilter.name());
            preparedStatement.setString(2, bookFilter.author());

            var resultSet = preparedStatement.executeQuery();
            Book book = null;
            if(resultSet.next()) {
                book = buildBook(resultSet);
                updateStatus(book, false);
            }
            return Optional.ofNullable(book);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Book buildBook(ResultSet resultSet) throws SQLException {
        Book book;
        book = new Book(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("author"),
                resultSet.getInt("year_of_publishing"),
                "not available"
        );
        return book;
    }

    @Override
    public void update(Book book) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getYearOfPublishing());
            preparedStatement.setString(4, book.getStatus());
            preparedStatement.setInt(5, book.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void updateStatus(Book book, boolean isAvailable) {
        try(var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(UPDATE_STATUS_SQL)) {
            String available = isAvailable ? "available" : "not available";
            preparedStatement.setString(1, available);
            preparedStatement.setInt(2, book.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Book save(Book book) {
        try(var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getYearOfPublishing());
            preparedStatement.setString(4, book.getStatus());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                book.setId(generatedKeys.getInt("id"));
            }

            return book;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
