package com.morzevichka.manageyourstore.services;


import com.morzevichka.manageyourstore.entity.OrderItem;
import com.morzevichka.manageyourstore.repository.OrderItemRepository;
import com.morzevichka.manageyourstore.services.impl.OrderItemService;

import java.util.List;

public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository = new OrderItemRepository();

    @Override
    public OrderItem findOrderItem(Long id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    @Override
    public void saveOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteOrderItem(OrderItem orderitem) {
        orderItemRepository.delete(orderitem);
    }

    @Override
    public void updateOrderItem(OrderItem orderItem) {
        orderItemRepository.update(orderItem);
    }

    @Override
    public List<OrderItem> findAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public int totalProductQuantity() {
        return orderItemRepository.countTotalProductQuantity();
    }
}
