package com.morzevichka.manageyourstore.repository;

import com.morzevichka.manageyourstore.annotation.Query;
import com.morzevichka.manageyourstore.entity.Category;
import com.morzevichka.manageyourstore.repository.impl.GenericRepository;

import java.util.Optional;

public class CategoryRepository extends GenericRepository<Category, Long> {

    public CategoryRepository() {
        super(Category.class);
    }

    @Query(value = "SELECT * FROM CATEGORIES WHERE NAME = ?1")
    public Optional<Category> findByName(String name) {
        return getSingleResultQuery(name);
    }
}
