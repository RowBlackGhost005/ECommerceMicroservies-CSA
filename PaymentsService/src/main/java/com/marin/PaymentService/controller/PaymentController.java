package com.marin.PaymentService.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.marin.PaymentService.dto.PaymentOrderRequestDTO;
import com.marin.PaymentService.dto.PaymentProcessDTO;
import com.marin.PaymentService.entities.Payment;
import com.marin.PaymentService.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/orders")
    public ResponseEntity<Payment> registerOrderPayment(@RequestBody PaymentOrderRequestDTO paymentOrderRequestDTO){
        Payment paymentDB = paymentService.registerOrderPayment(paymentOrderRequestDTO);

        return ResponseEntity.ok(paymentDB);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody PaymentProcessDTO payment){
        Payment paymentDB = paymentService.processPayment(payment);

        return ResponseEntity.ok(paymentDB);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<Payment> registerPayment(@RequestBody Payment payment){
        Payment paymentDB = paymentService.registerPayment(payment);

        return ResponseEntity.ok(paymentDB);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Payment>> fetchAllPayments(){
        List<Payment> payments = paymentService.fetchAllPayments();

        return ResponseEntity.ok(payments);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Payment> fetchPaymentByOrderId(@PathVariable("id") int id){
        Payment payment = paymentService.fetchPaymentOrderId(id);

        return ResponseEntity.ok(payment);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable("id") int id , @RequestBody JsonNode jsonNode){

        String status = jsonNode.get("status").asText();

        Payment paymentUpdated = paymentService.changePaymentStatusById(id , status);

        return ResponseEntity.ok(paymentUpdated);
    }
}
