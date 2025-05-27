package com.marin.PaymentService.service;

import com.marin.PaymentService.dto.PaymentOrderRequestDTO;
import com.marin.PaymentService.dto.PaymentProcessDTO;
import com.marin.PaymentService.entities.OrderStatus;
import com.marin.PaymentService.entities.Payment;
import com.marin.PaymentService.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImp implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImp(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    @Override
    public Payment registerOrderPayment(PaymentOrderRequestDTO paymentOrderRequestDTO) {
        Payment orderPayment = new Payment();
        orderPayment.setOrderId(paymentOrderRequestDTO.orderId());
        orderPayment.setStatus(OrderStatus.PROCESSING);

        return paymentRepository.save(orderPayment);
    }

    @Override
    public Payment processPayment(PaymentProcessDTO paymentProcessDTO) {
        Payment paymentDB = paymentRepository.findById(paymentProcessDTO.id()).orElseThrow();

        if (paymentDB.getStatus() == OrderStatus.PROCESSING){
            paymentDB.setStatus(OrderStatus.CONFIRMED);
        }

        return paymentRepository.save(paymentDB);
    }

    @Override
    public Payment registerPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> fetchAllPayments() {
        return (List<Payment>) paymentRepository.findAll();
    }

    @Override
    public Payment fetchPaymentOrderId(int orderId){
        return paymentRepository.fetchPaymentOrderId(orderId);
    }

    @Override
    public Payment changePaymentStatusById(int paymentId, String newStatus) {
        Payment paymentDB = fetchPaymentOrderId(paymentId);

        OrderStatus status;

        switch(OrderStatus.valueOf(newStatus)){
            case OrderStatus.CONFIRMED -> status = OrderStatus.CONFIRMED;
            case OrderStatus.PROCESSING -> status = OrderStatus.PROCESSING;
            case OrderStatus.CANCELLED -> status = OrderStatus.CANCELLED;
            default -> status = paymentDB.getStatus();
        }

        paymentDB.setStatus(status);

        return paymentRepository.save(paymentDB);
    }
}
