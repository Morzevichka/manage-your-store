package com.morzevichka.manageyourstore.repository;

import com.morzevichka.manageyourstore.annotation.Query;
import com.morzevichka.manageyourstore.entity.OrderItem;
import com.morzevichka.manageyourstore.repository.impl.GenericRepository;
import com.morzevichka.manageyourstore.utils.HibernateUtil;

import java.util.List;

public class OrderItemRepository extends GenericRepository<OrderItem, Long> {

    public OrderItemRepository() {
        super(OrderItem.class);
    }

    @Query(value = "SELECT * FROM ORDER_ITEMS WHERE ORDER_ID = ?1")
    public List<OrderItem> findByOrderId(Long id) {
        return getResultListQuery(id);
    }

    @Query(value = "SELECT * FROM ORDER_ITEMS WHERE PRODUCT_ID = ?1")
    public List<OrderItem> findByProductId(Long id) {
        return getResultListQuery(id);
    }

    public int countTotalProductQuantity() {
        return ((Number) HibernateUtil.getSessionFactory().fromSession(session ->
                session.createNativeQuery("SELECT SUM(QUANTITY) FROM ORDER_ITEMS").uniqueResult()
        )).intValue();
    }
}
