package com.marin.OrderService.service;

import com.marin.OrderService.dto.OrderRequestDTO;
import com.marin.OrderService.entities.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    @Transactional
    Order saveOrder(OrderRequestDTO order);

    Order getOrderById(int id);

    List<Order> getOrders();

    List<Order> getOrdersByUserId(int id);

    List<Order> getOrdersByAuthUser();

    Order updateOrder(Order order, int id);

    Order processOrder(int id);

    Order cancelOrder(int id);

    void deleteOrder(int id);


}
