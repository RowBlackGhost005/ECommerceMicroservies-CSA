package com.marin.PaymentService.service;

import com.marin.PaymentService.dto.PaymentOrderRequestDTO;
import com.marin.PaymentService.dto.PaymentProcessDTO;
import com.marin.PaymentService.entities.Payment;

import java.util.List;

public interface PaymentService {

    Payment registerOrderPayment(PaymentOrderRequestDTO paymentOrderRequestDTO);

    Payment processPayment(PaymentProcessDTO paymentProcessDTO);

    Payment registerPayment(Payment payment);

    List<Payment> fetchAllPayments();

    Payment fetchPaymentOrderId(int orderId);

    Payment changePaymentStatusById(int paymentId, String newStatus);


}