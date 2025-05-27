package com.marin.OrderService.service;

import com.marin.OrderService.dto.OrderRequestDTO;
import com.marin.OrderService.dto.OrderResultDTO;
import com.marin.OrderService.dto.PaymentDTO;
import com.marin.OrderService.dto.ProductDTO;
import com.marin.OrderService.entities.Order;
import com.marin.OrderService.entities.OrderDetails;
import com.marin.OrderService.entities.OrderStatus;
import com.marin.OrderService.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;

    private final WebClient gateway;

    private final JwtService jwtService;

    @Autowired
    public OrderServiceImp(OrderRepository orderRepository, WebClient.Builder webClientBuilder, JwtService jwtService) {
        this.orderRepository = orderRepository;
        this.gateway = webClientBuilder.build();
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "orders", fallbackMethod = "fallbackSaveOrder")
    @Retry(name = "orders")
    public Order saveOrder(OrderRequestDTO order) {

        //Fetch Products data
        OrderResultDTO orderResult = fetchProductsFromOrderRequest(order);

        //Creates the order
        Order orderToSave = new Order();

        orderToSave.setUserId(order.userId());
        orderToSave.setOrderDate(order.orderDate());
        orderToSave.setTotal(orderResult.total());

        //Matches Products Data to OrderDetails
        for(ProductDTO product : order.products()){
            for(int i = 0 ; i < orderResult.productDetails().size() ; i++){
                if(orderResult.productDetails().get(i).id() == product.productId()){
                    OrderDetails orderDetails = new OrderDetails();

                    orderDetails.setProductId(product.productId());
                    orderDetails.setQuantity(product.quantity());
                    orderDetails.setUnitPrice(orderResult.productDetails().get(i).price());
                    orderDetails.setOrder(orderToSave);

                    orderToSave.getOrderDetails().add(orderDetails);
                    break;
                }
            }
        }

        //Saves the Order
        orderToSave = orderRepository.save(orderToSave);

        //Create the payment order
        postPaymentOfOrder(orderToSave);

        return orderToSave;
    }

    @Override
    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Order> getOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByUserId(int id){
        return orderRepository.getOrdersByUserId(id);
    }

    @Override
    public List<Order> getOrdersByAuthUser() {

        int authUserId = jwtService.extractAuthUserId(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());

        return orderRepository.getOrdersByUserId(authUserId);
    }

    @Override
    public Order updateOrder(Order order, int id) {
        return null;
    }

    @Override
    public Order processOrder(int id) {
        Order orderDB = orderRepository.findById(id).orElseThrow();

        switch (orderDB.getStatus()){
            case OrderStatus.ORDERED -> orderDB.setStatus(OrderStatus.PROCESSING);
            case OrderStatus.PROCESSING -> orderDB.setStatus(OrderStatus.SHIPPED);
            case OrderStatus.SHIPPED -> orderDB.setStatus(OrderStatus.DELIVERED);
        }

        return orderRepository.save(orderDB);
    }

    @Override
    public Order cancelOrder(int id) {
        Order orderDB = orderRepository.findById(id).orElseThrow();

        orderDB.setStatus(OrderStatus.CANCELLED);

        return orderRepository.save(orderDB);
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }


    public Order fallbackSaveOrder(OrderRequestDTO order, Exception e) {

        Order failedOrder = new Order();

        failedOrder.setUserId(order.userId());
        failedOrder.setStatus(OrderStatus.CANCELLED);
        failedOrder.setOrderDate(LocalDateTime.now());

        return failedOrder;
    }

    private OrderResultDTO fetchProductsFromOrderRequest(OrderRequestDTO order){
        return gateway.post().uri("http://localhost:8080/products/order")
                .headers(headers -> headers.setBearerAuth(jwtService.extractJwtFromRequest(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(order.products())
                .retrieve()
                .bodyToMono(OrderResultDTO.class)
                .block();
    }

    private void postPaymentOfOrder(Order order){
        gateway.post().uri("http://localhost:8080/payments/orders")
                .headers(headers -> headers.setBearerAuth(jwtService.extractJwtFromRequest(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())))
                .bodyValue(new PaymentDTO(order.getId()))
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }


}
