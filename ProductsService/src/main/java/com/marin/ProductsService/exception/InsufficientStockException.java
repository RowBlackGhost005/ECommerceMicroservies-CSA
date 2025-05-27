package com.marin.ProductsService.exception;

public class InsufficientStockException extends Exception{

    public InsufficientStockException(String message){
        super(message);
    }
}
