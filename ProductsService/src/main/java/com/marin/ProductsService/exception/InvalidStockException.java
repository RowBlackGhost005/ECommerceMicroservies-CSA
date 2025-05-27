package com.marin.ProductsService.exception;

public class InvalidStockException extends Exception{

    public InvalidStockException(String message){
        super(message);
    }
}
