package com.marin.ProductsService.dto;

public record ProductDetailsDTO (
        int id,
        String name,
        String description,
        float price
) { }
