package com.MicroService.order.controller;

import com.MicroService.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

   /* @PostMapping("/create")
    public ResponseEntity<Mono<CombinedResponse>> createOrder(){
        return new ResponseEntity<>(orderService.getCombinedData(), HttpStatus.OK);
    }*/
    @PostMapping("/createRestTemplate")
    public ResponseEntity<String> createOrderByRestTemplate(){
        return new ResponseEntity<>(orderService.callServiceByRestTemplate(),HttpStatus.OK);
    }

    @PostMapping("/createRestClient")
    public ResponseEntity<String> createOrderByRestClient(){
        return new ResponseEntity<>(orderService.callServiceByRestTemplate(),HttpStatus.OK);
    }

    @PostMapping("/rateLimiter")
    public ResponseEntity<String> createOrderByFeignClient(){
        return new ResponseEntity<>(orderService.callUserClientRateLimiter("test"),HttpStatus.OK);
    }
    @PostMapping("/bulkhead")
    public ResponseEntity<String> callUserClientBulkHead(){
        return new ResponseEntity<>(orderService.callUserClientBulkHead("test"),HttpStatus.OK);
    }
    @PostMapping("/circuitBreaker")
    public ResponseEntity<String> callUserCircuitBreaker(){
        return new ResponseEntity<>(orderService.callUserClientCircuitBreaker("test"),HttpStatus.OK);
    }

    @PostMapping("/retry")
    public ResponseEntity<String> callUserRetry(){
        return new ResponseEntity<>(orderService.callUserClientRetry("test"),HttpStatus.OK);
    }

    @PostMapping("/timeout")
    public ResponseEntity<CompletableFuture<String>> callUserTimeout(){
        return new ResponseEntity<>(orderService.callUserClientTimeLimiter("test"),HttpStatus.OK);
    }

}
