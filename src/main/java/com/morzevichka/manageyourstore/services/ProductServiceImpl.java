package com.morzevichka.manageyourstore.services;

import com.morzevichka.manageyourstore.dto.ProductSalesDto;
import com.morzevichka.manageyourstore.entity.Product;
import com.morzevichka.manageyourstore.repository.ProductRepository;
import com.morzevichka.manageyourstore.services.impl.ProductService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository = new ProductRepository();

    @Override
    public Product findProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.update(product);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllProductWithCategory() {
        return productRepository.findAllWithCategory();
    }

    @Override
    public List<Product> findAllWithCategoryAndOrderItems() {
        return productRepository.findAllWithCategoryAndOrderItems();
    }

    @Override
    public int countProducts() {
        return productRepository.countProducts();
    }

    @Override
    public Product findProductByName(String name) {
        return productRepository.findByName(name).orElse(null);
    }

    @Override
    public List<ProductSalesDto> productSalesBetween(LocalDate var1, LocalDate var2) {
        Timestamp from = var1 != null ? Timestamp.valueOf(LocalDateTime.of(var1, LocalTime.MIDNIGHT)) : Timestamp.from(Instant.EPOCH);
        Timestamp to = var2 != null ? Timestamp.valueOf(LocalDateTime.of(var2, LocalTime.MIDNIGHT)) : Timestamp.valueOf(LocalDateTime.now());

        return productRepository.findProductSalesBetween(from, to);
    }
}
