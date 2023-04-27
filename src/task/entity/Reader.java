package task.entity;

public class Reader {

    private Integer id;

    private String name;

    private Book book;

    private String status;

    public Reader(Integer id, String name, Book book, String status) {
        this.id = id;
        this.name = name;
        this.book = book;
        this.status = status;
    }

    public Reader(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reader{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", book=" + book +
               ", status='" + status + '\'' +
               '}';
    }
}
