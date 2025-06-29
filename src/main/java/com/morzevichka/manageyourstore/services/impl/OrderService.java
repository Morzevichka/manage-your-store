package com.morzevichka.manageyourstore.services.impl;

import com.morzevichka.manageyourstore.dto.OrderClientDto;
import com.morzevichka.manageyourstore.entity.Client;
import com.morzevichka.manageyourstore.entity.Order;
import com.morzevichka.manageyourstore.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    Order findOrder(Long id);

    void saveOrder(Order order);

    void deleteOrder(Order order);

    void updateOrder(Order order);

    List<Order> findAllOrders();

    int countOrders();

    void createPurchase(List<Product> products, Client client, BigDecimal totalPrice);

    List<OrderClientDto> ordersWithClientNameBetween(LocalDate var1, LocalDate var2);
}
