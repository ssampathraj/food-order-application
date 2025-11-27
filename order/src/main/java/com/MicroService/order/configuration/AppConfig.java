package com.MicroService.order.configuration;

import com.MicroService.order.decoder.CustomErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
     /*   @Bean
        public WebClient webClient() {
            return WebClient.builder().build();
        }*/

        @Bean
    public RestTemplate restTemplate(){
            return new RestTemplate();
        }
        @Bean
    public RestClient restClient(){
            return RestClient.builder().build();
                    // or  either way you can create object
          //  return RestClient.create();
        }


}
