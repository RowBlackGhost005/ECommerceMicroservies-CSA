package com.marin.ProductsService.service;

import com.marin.ProductsService.dto.OrderResultDTO;
import com.marin.ProductsService.dto.ProductDTO;
import com.marin.ProductsService.dto.ProductDetailsDTO;
import com.marin.ProductsService.dto.StockProductDTO;
import com.marin.ProductsService.entities.Product;
import com.marin.ProductsService.exception.InsufficientStockException;
import com.marin.ProductsService.exception.InvalidStockException;
import com.marin.ProductsService.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImp(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Product> getProducts() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Product updateProduct(Product product, int id) {
        Product productDB = productRepository.findById(id).orElseThrow();

        if(product.getName() != null && !product.getName().isBlank()){
            productDB.setName(product.getName());
        }

        if(product.getDescription() != null && !product.getDescription().isBlank()){
            productDB.setDescription(product.getDescription());
        }

        if(product.getPrice() > 0){
            productDB.setPrice(product.getPrice());
        }

        if(product.getStock() > 0){
            productDB.setStock(product.getStock());
        }

        return productRepository.save(productDB);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDTO unstockProduct(StockProductDTO productStock) {

        if(productStock.stock() <= 0){
            //throw new InvalidParameterException("Invalid stock quantity to remove");
        }

        Product productDB = productRepository.findById(productStock.productId()).orElseThrow();

        if(productStock.stock() > productDB.getStock()){
            //throw new InsufficientStockException("Insufficient stock");
        }

        productDB.setStock(productDB.getStock() - productStock.stock());

        productDB = productRepository.save(productDB);

        return new ProductDTO(productDB.getId() , productDB.getStock());
    }

    @Override
    public ProductDTO stockProduct(StockProductDTO productStock) throws InvalidStockException {

        if(productStock.stock() <= 0){
            throw new InvalidStockException("Invalid stock quantity to remove");
        }

        Product productDB = productRepository.findById(productStock.productId()).orElseThrow();

        productDB.setStock(productDB.getStock() + productStock.stock());

        productDB = productRepository.save(productDB);

        return new ProductDTO(productDB.getId() , productDB.getStock());
    }

    @Override
    @Transactional
    public OrderResultDTO orderProducts(List<ProductDTO> products) throws InsufficientStockException {
        float total = 0;
        List<ProductDetailsDTO> productDetailsDTOs = new ArrayList<>(products.size());

        for(ProductDTO productDTO : products){
            Product product = productRepository.findById(productDTO.productId()).orElseThrow();

            if(product.getStock() >= productDTO.quantity()){
                product.setStock(product.getStock() - productDTO.quantity());
                productRepository.save(product);

                total += productDTO.quantity() * product.getPrice();
                productDetailsDTOs.add(new ProductDetailsDTO(product.getId() , product.getName() , product.getDescription() , product.getPrice()));
            }else{
                throw new InsufficientStockException("Not enough stock for product " + product.getId());
            }
        }
        return new OrderResultDTO(total , productDetailsDTOs);
    }
}
