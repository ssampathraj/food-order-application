package com.MicroService.order.configuration;

import com.MicroService.order.decoder.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean
    public ErrorDecoder customErrorDecoder(){
        return new CustomErrorDecoder();
    }
}
