package com.marin.OrderService.repository;

import com.marin.OrderService.entities.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails , Integer> {
}
