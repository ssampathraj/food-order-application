package com.MicroService.email.controller;

import com.MicroService.email.model.OrderDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class OrderController {

    @PostMapping("/order")
    public ResponseEntity<OrderDetail> newOrderDetails(@RequestBody OrderDetail order){
        System.out.println("order received");
        return  new ResponseEntity<>(order, HttpStatus.OK) ;
    }


}
