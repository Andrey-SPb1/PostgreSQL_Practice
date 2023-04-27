package task;

import task.dao.BlacklistDao;
import task.dao.BookDao;
import task.dao.ReaderDao;
import task.dto.BookFilter;
import task.entity.Blacklist;
import task.entity.Reader;

import java.util.Optional;

/**
 * Система Библиотека. Читатель оформляет Заказ на Книгу. Система осуществляет поиск в Каталоге.
 * Библиотекарь выдает Читателю Книгу на абонемент или в читальный зал.
 * При невозвращении Книги Читателем он может быть занесен Администратором в “черный список”.
 */

public class LibraryRunner {

    public static void main(String[] args) {

        updateReader();

    }

    private static void updateReader() {
        var reader = ReaderDao.getInstance().findById(5).orElse(null);
        if(reader != null) {
            reader.setStatus("out");
            ReaderDao.getInstance().update(reader);
        }
    }

    private static boolean deleteReader() {
        return ReaderDao.getInstance().delete(8);
    }

    private static void saveBlacklist() {
        ReaderDao.getInstance().findById(9).ifPresent
                (reader -> BlacklistDao.getInstance().save(new Blacklist(reader)));
    }

    private static void saveReader() {
        Reader reader = new Reader("Елена Светлова", "out");
        ReaderDao.getInstance().save(reader);
    }

    private static void takeBook() {
        BookFilter bookFilter = new BookFilter("Руслан и Людмила", "Пушкин");
        Reader reader = ReaderDao.getInstance().findById(6).orElse(null);
        assert reader != null;
        ReaderDao.getInstance().takeBook(reader, bookFilter);
    }
}
