package com.marin.ProductsService.service;

import com.marin.ProductsService.dto.OrderResultDTO;
import com.marin.ProductsService.dto.ProductDTO;
import com.marin.ProductsService.dto.StockProductDTO;
import com.marin.ProductsService.entities.Product;
import com.marin.ProductsService.exception.InsufficientStockException;
import com.marin.ProductsService.exception.InvalidStockException;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product);

    Product getProductById(int id);

    List<Product> getProducts();

    Product updateProduct(Product product , int id);

    void deleteProduct(int id);

    ProductDTO unstockProduct(StockProductDTO productStock);

    ProductDTO stockProduct(StockProductDTO productStock) throws InvalidStockException;

    OrderResultDTO orderProducts(List<ProductDTO> products) throws InsufficientStockException;
}
