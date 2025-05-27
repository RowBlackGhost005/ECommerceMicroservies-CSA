package com.marin.OrderService.dto;

public record ProductDetailsDTO (
        int id,
        String name,
        String description,
        float price
) { }
