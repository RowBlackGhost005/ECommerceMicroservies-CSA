package com.marin.OrderService.repository;

import com.marin.OrderService.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.userId = :id")
    List<Order> getOrdersByUserId(@Param("id") int id);

}
