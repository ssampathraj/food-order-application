package com.MicroService.order.model;

import lombok.Data;

@Data
public class CombinedResponse {
    private OrderDetail user;
    private OrderDetail email;
    private OrderDetail delivery;
}
