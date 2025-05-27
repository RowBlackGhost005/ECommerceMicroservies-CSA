package com.marin.ECommerce.API.Gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping()
    public ResponseEntity<String> fallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is temporarily unavailable.");
    }


    @GetMapping("/orders")
    public String ordersFallback() {
        return "Orders service is temporarily unavailable, try again later.";
    }

    @GetMapping("/products")
    public String productsFallback() {
        return "Orders service is temporarily unavailable, try again later.";
    }

    @GetMapping("/users")
    public String usersFallback() {
        return "Orders service is temporarily unavailable, try again later.";
    }

    @GetMapping("/auth")
    public String authFallback() {
        return "Orders service is temporarily unavailable, try again later.";
    }

    @GetMapping("/payments")
    public String paymentsFallback() {
        return "Orders service is temporarily unavailable, try again later.";
    }

    @GetMapping("/serviceDown")
    public String serviceDownFallback(){
        return "The service you are trying to reach is unavailable, try again later.";
    }

}
