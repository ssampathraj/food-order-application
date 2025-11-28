package com.MicroService.delivery.controller;




import com.MicroService.delivery.model.OrderDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery")
public class OrderController {

    @Value("${welcome.message}")
    private String message;

    @PostMapping("/order")
    public OrderDetail newOrderDetails(@RequestBody OrderDetail order){
        System.out.println("order received");
        return  order;
    }
    @GetMapping("/configfetch")
    public String fetch(){
        return "fetch from: "+ message;
    }
}
