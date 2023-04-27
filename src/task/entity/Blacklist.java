package task.entity;

public class Blacklist {

    private Integer id;

    private Reader reader;

    public Blacklist(Integer id, Reader reader) {
        this.id = id;
        this.reader = reader;
    }

    public Blacklist(Reader reader) {
        this.reader = reader;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public String toString() {
        return "Blacklist{" +
               "id=" + id +
               ", reader=" + reader +
               '}';
    }
}
