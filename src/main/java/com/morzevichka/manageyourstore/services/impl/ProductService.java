package com.morzevichka.manageyourstore.services.impl;


import com.morzevichka.manageyourstore.dto.ProductSalesDto;
import com.morzevichka.manageyourstore.entity.Product;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {
    Product findProduct(Long id);

    void saveProduct(Product product);

    void deleteProduct(Product product);

    void updateProduct(Product product);

    List<Product> findAllProducts();

    int countProducts();

    List<Product> findAllProductWithCategory();

    List<Product> findAllWithCategoryAndOrderItems();

    Product findProductByName(String name);

    List<ProductSalesDto> productSalesBetween(LocalDate var1, LocalDate var2);
}
