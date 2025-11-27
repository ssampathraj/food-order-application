package com.MicroService.delivery.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private int orderId;
    private String hotelAddress;
    private String userAddress;
    private String userName;
    private String hotelName;
    private  String userContact;

}
