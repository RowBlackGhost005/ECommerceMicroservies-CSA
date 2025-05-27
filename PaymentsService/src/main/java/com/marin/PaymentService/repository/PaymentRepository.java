package com.marin.PaymentService.repository;


import com.marin.PaymentService.entities.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    @Query("SELECT p FROM Payment p WHERE p.orderId = :orderId")
    Payment fetchPaymentOrderId(@Param("orderId") int orderId);
}
