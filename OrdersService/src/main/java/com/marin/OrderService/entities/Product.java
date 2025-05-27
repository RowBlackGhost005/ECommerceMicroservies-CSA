package com.marin.OrderService.entities;



public record Product (
        int id,
        String name,
        String description,
        float price,
        int stock
) { }
