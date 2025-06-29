package com.morzevichka.manageyourstore.services.impl;

import com.morzevichka.manageyourstore.entity.Category;

import java.util.List;

public interface CategoryService {
    Category findCategory(Long id);

    void saveCategory(Category category);

    void deleteCategory(Category category);

    void updateCategory(Category category);

    List<Category> findAllCategories();

    Category findCategoryByName(String name);
}
