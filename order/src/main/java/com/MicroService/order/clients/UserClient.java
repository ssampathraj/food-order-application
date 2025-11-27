package com.MicroService.order.clients;

import com.MicroService.order.configuration.UserConfig;
import com.MicroService.order.model.OrderDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*  without service discovery
@FeignClient(name="USER-SERVICE",url = "${userService.url}")
*/
// with service discovery we don't need to specially call server with port just name is enough
@FeignClient(name = "user-service",path = "/api/user")
public interface UserClient {

    @PostMapping("/order")
    public String placeOrderToUser(@RequestBody OrderDetail orderDetail);
    // example for error handling no method in user service so it will throw resource not found exception (404)
    @PostMapping("/order1")
    public String placeOrderToUser1(@RequestBody OrderDetail orderDetail);
}
