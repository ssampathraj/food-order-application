package com.MicroService.user.controller;

import com.MicroService.user.model.OrderDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class OrderController {

    @PostMapping("/order")
    public ResponseEntity<OrderDetail> newOrderDetails(@RequestBody OrderDetail order) throws InterruptedException {
        System.out.println("order received");
        Thread.sleep(6000);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }


}