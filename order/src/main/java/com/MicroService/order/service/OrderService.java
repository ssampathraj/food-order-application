package com.MicroService.order.service;

import com.MicroService.order.clients.UserClient;
import com.MicroService.order.model.OrderDetail;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
/*mport org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;*/

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderService {

   /*
    @Autowired
    private WebClient webClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RestClient restClient;
    @Autowired
    private UserClient client;
    */
    // we can use @Autowired or use @RequiredArgsConstructor and  private final  that will inject as constructor injection
//    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private  final RestClient restClient;
    private final UserClient client;

    private final RateLimiterRegistry registry;
    private final BulkheadRegistry bulkheadRegistry;

    @PostConstruct
    public void testRateLimiter() {
        System.out.println("RateLimiters:");
        registry.getAllRateLimiters().forEach(System.out::println);
        System.out.println("bulk head:");
        bulkheadRegistry.getAllBulkheads().forEach(System.out::println);
    }

    /*
    Feign Client:
     1. used for Sync call between services it is more advanced level .
     2. Using Spring cloud Open Feign library
     3. easy way to call services and handling exceptions
     */
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "rateLimitFallback")
    public String callUserClientRateLimiter(String name){
        return client.placeOrderToUser(generateOrder());
    }

    public String rateLimitFallback(String name, Throwable t) {
        return "Rate limit exceeded, Try again later";
    }

    @Bulkhead(name = "userService",type = Bulkhead.Type.SEMAPHORE,fallbackMethod = "bulkFallBack")
    public String callUserClientBulkHead(String name){
               return client.placeOrderToUser(generateOrder());
    }
    public String bulkFallBack(String name,Throwable t) {
        return "Too many concurrent request, Try again later";
    }

    @CircuitBreaker(name = "userServiceCircuitBreaker", fallbackMethod = "cbFallbackMethod")
    public String callUserClientCircuitBreaker(String name){
        return client.placeOrderToUser(generateOrder());
    }
    // in circuit breaker if service fails always fall back method will call
    public String cbFallbackMethod(String name,Throwable t) {
        return "User Service Down — fallback executed!";
    }

    @Retry(name = "userServiceRetry", fallbackMethod = "retryFallbackMethod")
    public String callUserClientRetry(String name){
        return client.placeOrderToUser(generateOrder());
    }
    public String retryFallbackMethod(String name,Throwable t) {
        return "User Service Down — Re-try fallback executed!";
    }

    @TimeLimiter(name = "userService", fallbackMethod = "timeoutFallback")
    @Async
    public CompletableFuture<String> callUserClientTimeLimiter(String name) {
        return CompletableFuture.supplyAsync(() -> {
            return client.placeOrderToUser(generateOrder()); // some slow call
        });
    }

    public CompletableFuture<String> timeoutFallback(String name,Throwable t) {
        return CompletableFuture.completedFuture("Service timed out! Try again later");
    }


    /*
    Rest Client  -
     1. used for Sync call between services it is more advanced level .
     2. chained method calls easy exception handling
     */

    public String callServiceByRestClient(){
        OrderDetail orderDetail = generateOrder();
        return restClient.post()
                .uri("http://localhost:8081/api/delivery/order")
                .accept(MediaType.APPLICATION_JSON)
                .body(orderDetail)
                .retrieve()
              .toString();
        // .body(String.class);
    }
    public ResponseEntity<OrderDetail> callServiceByRestClientwithModel(){
        OrderDetail orderDetail = generateOrder();
        return restClient.post()
                .uri("http://localhost:8081/api/delivery/order")
                .accept(MediaType.APPLICATION_JSON)
                .body(orderDetail)
                .retrieve()
                .toEntity(OrderDetail.class);  //  this will return response entity
    }

    // Rest template - used for Sync call between services
    public String callServiceByRestTemplate(){
        OrderDetail order = generateOrder();
      return   restTemplate.postForObject("http://localhost:8081/api/delivery/order",order,String.class);
    }

    /*
    Web client(Web flux ) for reactive programming -  for async call
     */
    /*public Mono<CombinedResponse> getCombinedData() {

        OrderDetail order = generateOrder();
        Mono<OrderDetail> userMono = webClient.post()
                .uri("http://localhost:8082/api/user/order")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(OrderDetail.class);

        Mono<OrderDetail> deliveryMono = webClient.post()
                .uri("http://localhost:8081/api/delivery/order")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(OrderDetail.class);

        Mono<OrderDetail> emailMono = webClient.post()
                .uri("http://localhost:8083/api/email/order")
                .bodyValue(order)
                .retrieve()
                .bodyToMono(OrderDetail.class);

        return Mono.zip(userMono, deliveryMono, emailMono)
                .map(tuple -> {
                    CombinedResponse response = new CombinedResponse();
                    response.setUser(tuple.getT1());
                    response.setDelivery(tuple.getT2());
                    response.setEmail(tuple.getT3());
                    return response;
                });
    }*/

    private OrderDetail generateOrder() {
            OrderDetail order = new OrderDetail();
            Random random = new Random();

            order.setOrderId(1000 + random.nextInt(9000));
            order.setUserName("User_" + random.nextInt(100));
            order.setHotelName("Hotel_" + random.nextInt(50));
            order.setUserAddress(random.nextInt(100) + " Gandhi Road, Chennai");
            order.setHotelAddress("No." + random.nextInt(50) + ", Residency Road, Chennai");
            order.setUserContact("+91-9" + (100000000 + random.nextInt(900000000)));

            return order;
    }
}
