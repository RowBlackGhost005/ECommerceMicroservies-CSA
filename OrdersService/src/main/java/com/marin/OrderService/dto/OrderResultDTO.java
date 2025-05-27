package com.marin.OrderService.dto;

import java.util.List;

public record OrderResultDTO (
        float total,
        List<ProductDetailsDTO> productDetails
) { }
