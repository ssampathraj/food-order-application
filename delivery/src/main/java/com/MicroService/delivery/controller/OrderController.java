package com.MicroService.delivery.controller;




import com.MicroService.delivery.model.OrderDetail;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
public class OrderController {

    @PostMapping("/order")
    public OrderDetail newOrderDetails(@RequestBody OrderDetail order){
        System.out.println("order received");
        return  order;
    }
}
