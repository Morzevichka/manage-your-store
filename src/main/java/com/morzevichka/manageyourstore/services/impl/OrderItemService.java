package com.morzevichka.manageyourstore.services.impl;

import com.morzevichka.manageyourstore.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    OrderItem findOrderItem(Long id);

    void saveOrderItem(OrderItem orderItem);

    void deleteOrderItem(OrderItem orderitem);

    void updateOrderItem(OrderItem orderItem);

    List<OrderItem> findAllOrderItems();

    int totalProductQuantity();
}
