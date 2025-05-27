package com.marin.OrderService.controller;

import com.marin.OrderService.dto.OrderRequestDTO;
import com.marin.OrderService.entities.Order;
import com.marin.OrderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;

    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO order){
        Order savedOrder = orderService.saveOrder(order);

        return ResponseEntity.ok(savedOrder);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable("id") int id){
        Order cancelledOrder = orderService.cancelOrder(id);

        return ResponseEntity.ok(cancelledOrder);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/process/{id}")
    public ResponseEntity<Order> processOrder(@PathVariable("id") int id){
        Order processedOrder = orderService.processOrder(id);

        return ResponseEntity.ok(processedOrder);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Order>> fetchAllOrders(){
        List<Order> orders = orderService.getOrders();

        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Order> fetchOrderById(@PathVariable("id") int id){
        Order order = orderService.getOrderById(id);

        return ResponseEntity.ok(order);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/user")
    public ResponseEntity<List<Order>> fetchOrderByAuthUser(){

        List<Order> userOrders = orderService.getOrdersByAuthUser();

        return ResponseEntity.ok(userOrders);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Order>> fetchOrdersByUserId(@PathVariable("id") int id){
        List<Order> userOrders = orderService.getOrdersByUserId(id);

        return ResponseEntity.ok(userOrders);
    }
}
