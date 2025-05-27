package com.marin.ProductsService.controller;

import com.marin.ProductsService.dto.OrderResultDTO;
import com.marin.ProductsService.dto.ProductDTO;
import com.marin.ProductsService.dto.StockProductDTO;
import com.marin.ProductsService.entities.Product;
import com.marin.ProductsService.exception.InsufficientStockException;
import com.marin.ProductsService.exception.InvalidStockException;
import com.marin.ProductsService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping
    public ResponseEntity<List<Product>> fetchProducts(){
        List<Product> products = productService.getProducts();

        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> fetchProductById(@PathVariable int id){
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> registerProduct(@RequestBody Product product){
        Product registeredProduct = productService.saveProduct(product);

        return ResponseEntity.ok(registeredProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id , @RequestBody Product product){
        Product updatedProduct = productService.updateProduct(product, id);

        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        productService.deleteProduct(id);

        return ResponseEntity.ok("Product deleted!");
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/order")
    public ResponseEntity<OrderResultDTO> orderProducts(@RequestBody List<ProductDTO> products) throws InsufficientStockException {
        OrderResultDTO orderResult = productService.orderProducts(products);

        return ResponseEntity.ok(orderResult);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/stock/add")
    public ResponseEntity<ProductDTO> stockProduct(@RequestBody StockProductDTO stockProductDTO) throws InvalidStockException {

        ProductDTO product = productService.stockProduct(stockProductDTO);

        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/stock/remove")
    public ResponseEntity<ProductDTO> unstockProduct(@RequestBody StockProductDTO stockProductDTO){

        ProductDTO product = productService.unstockProduct(stockProductDTO);

        return ResponseEntity.ok(product);
    }
}
