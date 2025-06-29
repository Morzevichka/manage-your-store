package com.morzevichka.manageyourstore.repository;

import com.morzevichka.manageyourstore.annotation.Query;
import com.morzevichka.manageyourstore.dto.ProductSalesDto;
import com.morzevichka.manageyourstore.entity.OrderItem;
import com.morzevichka.manageyourstore.entity.Product;
import com.morzevichka.manageyourstore.repository.impl.GenericRepository;
import com.morzevichka.manageyourstore.utils.HibernateUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class ProductRepository extends GenericRepository<Product, Long> {

    public ProductRepository() {
        super(Product.class);
    }

    @Override
    public void delete(Product product) {
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            Product product1 = findByIdWithOrderItems(product.getArticle()).orElse(null);

            if (product1 != null && !product1.getOrderItems().isEmpty()) {
                for (OrderItem orderItem : product1.getOrderItems()) {
                    orderItem.setProduct(null);
                    session.merge(orderItem);
                }
            }

            super.delete(product1);
        });
    }

    public int countProducts() {
        return((BigDecimal) HibernateUtil.getSessionFactory().fromSession(session ->
                session.createNativeQuery("SELECT COUNT(*) FROM PRODUCTS").uniqueResult()
        )).intValue();
    }

    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.orderItems", nativeQuery = false)
    public List<Product> findAllWithCategoryAndOrderItems() {
        return getResultListQuery();
    }

    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.category", nativeQuery = false)
    public List<Product> findAllWithCategory() {
        return getResultListQuery();
    }

    @Query(value = "SELECT * FROM PRODUCTS WHERE NAME = ?1")
    public Optional<Product> findByName(String name) {
        return getSingleResultQuery(name);
    }

    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.name = ?1", nativeQuery = false)
    public Optional<Product> findByNameWithCategory(String name) {
        return getSingleResultQuery(name);
    }

    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.orderItems WHERE p.article = ?1", nativeQuery = false)
    public Optional<Product> findByIdWithOrderItems(Long id) {
        return getSingleResultQuery(id);
    }

    public List<ProductSalesDto> findProductSalesBetween(Timestamp from, Timestamp to) {
        List<Object[]> result = HibernateUtil.getSessionFactory().fromSession(session ->
                session.createNativeQuery("""
                                                SELECT p.name, 
                                                       SUM(o.QUANTITY), 
                                                       SUM(o.QUANTITY) * p.COST 
                                                FROM ORDER_ITEMS o
                                                LEFT JOIN PRODUCTS p ON o.PRODUCT_ARTICLE = p.ARTICLE
                                                LEFT JOIN ORDERS o1 ON o.ORDER_ID = o1.ID
                                                WHERE o1.PURCHASE_TIME BETWEEN ?1 AND ?2
                                                GROUP BY p.name, p.COST
                                            """)
                        .setParameter(1, from)
                        .setParameter(2, to)
                        .getResultList()
        );

        List<ProductSalesDto> products = result
                .stream()
                .map(row -> new ProductSalesDto(
                        (String) row[0],
                        ((BigDecimal) row[1]).doubleValue(),
                        ((BigDecimal) row[2]).doubleValue()
                ))
                .toList();

        return products;
    }
}
