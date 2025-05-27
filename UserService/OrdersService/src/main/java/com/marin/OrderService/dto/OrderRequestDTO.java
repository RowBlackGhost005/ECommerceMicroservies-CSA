package com.marin.OrderService.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDTO (

        int userId,
        LocalDateTime orderDate,
        List<ProductDTO> products

) { }
