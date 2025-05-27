package com.marin.ProductsService.repository;

import com.marin.ProductsService.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
