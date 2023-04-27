package task.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {

    Optional<E> findById (K id);

    List<E> findAll();
    void update(E ticket);

    E save(E ticket);

    boolean delete(K id);

}
