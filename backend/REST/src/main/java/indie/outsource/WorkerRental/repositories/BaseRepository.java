package indie.outsource.WorkerRental.repositories;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T,L> {
    List<T> findAll();

    Optional<T> findById(L id);

    T save(T t);

    void delete(T t);

    void deleteById(L t);

    void deleteAll();

}
