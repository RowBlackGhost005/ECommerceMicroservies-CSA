package com.marin.ProductsService.dto;

import java.util.List;

public record OrderResultDTO (
        float total,
        List<ProductDetailsDTO> productDetails
) { }
