package com.morzevichka.manageyourstore.repository.impl;

import java.util.List;
import java.util.Optional;

public interface CrudRepository <T, ID> {
    Optional<T> findById(ID id);

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    List<T> findAll();

    Optional<T> getSingleResultQuery(Object... params);

    List<T> getResultListQuery(Object... params);
}
