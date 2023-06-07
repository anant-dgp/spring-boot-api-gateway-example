package in.vaxa.orderservice.controller;

import in.vaxa.orderservice.dto.Order;
import in.vaxa.orderservice.dto.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private final String PAYMENT_SERVICE_URL = "http://PAYMENT-SERVICE/payments";

  @Autowired
  private RestTemplate restTemplate;


  @GetMapping("/all")
  public List<Order> getAllOrders() {
    List<Order> orders = new ArrayList<>();
    Payment[] payments = restTemplate.getForObject(PAYMENT_SERVICE_URL + "/all", Payment[].class);
    orders.add(new Order(1,
                         "KeyBoard",
                         Date.from(Instant.now()),
                         payments[0]));
    orders.add(new Order(2,
                         "Monitor",
                         Date.from(Instant.now()),
                         payments[1]));
    orders.add(new Order(3,
                         "LED",
                         Date.from(Instant.now()),
                         payments[2]));
    return orders;
  }

  @GetMapping("/{id}")
  public Order getOrder(@PathVariable int id) {
    ResponseEntity<Payment> responseEntity = restTemplate.getForEntity(PAYMENT_SERVICE_URL + "/2", Payment.class);
    return new Order(id, "LED", Date.from(Instant.now()), responseEntity.getBody());
  }

  @GetMapping("exchange/{id}")
  public Order getOrderWithExchange(@PathVariable int id) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.set("X-COM-LOCATION", "INDIA");
    headers.set("traceId", "fsdh-1234-kf54-98ui");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<Payment> responseEntity = restTemplate.exchange(PAYMENT_SERVICE_URL + "/2", HttpMethod.GET, entity,
                                                                   Payment.class);

    return new Order(id, "LED", Date.from(Instant.now()), responseEntity.getBody());
  }

  @GetMapping("execute/{id}")
  public Order getOrderWithExecute(@PathVariable int id) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.set("X-COM-LOCATION", "INDIA");
    headers.set("traceId", "fsdh-1234-kf54-98ui");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<Payment> responseEntity = restTemplate.execute(PAYMENT_SERVICE_URL + "/2",
                                                                  HttpMethod.GET,
                                                                  new RequestCallback() {
                                                                    @Override
                                                                    public void doWithRequest(ClientHttpRequest request) throws
                                                                                                                         IOException {
                                                                    }
                                                                  },
                                                                  new ResponseExtractor<ResponseEntity<Payment>>() {
                                                                    @Override
                                                                    public ResponseEntity<Payment> extractData(
                                                                        ClientHttpResponse response) throws
                                                                                                     IOException {
                                                                      return new ResponseEntity<>(response.getStatusCode());
                                                                    }
                                                                  });

    return new Order(id, "LED", Date.from(Instant.now()), responseEntity.getBody());
  }

  @GetMapping("/flux/all")
  public List<Order> getAllOrdersWithFlux() {
    List<Order> orders = new ArrayList<>();
    List<Payment> payments = WebClient.create().get()
                                      .uri("http://localhost:9091/payments/all")
                                      .retrieve()
                                      .bodyToFlux(Payment.class)
                                      .collect(Collectors.toList())
                                      .block();
    orders.add(new Order(1,
                         "KeyBoard",
                         Date.from(Instant.now()),
                         payments.get(0)));
    orders.add(new Order(2,
                         "Monitor",
                         Date.from(Instant.now()),
                         payments.get(1)));
    orders.add(new Order(3,
                         "LED",
                         Date.from(Instant.now()),
                         payments.get(2)));
    return orders;
  }

  @GetMapping("/flux/{id}")
  public Order getOrderWithFlux(@PathVariable int id) {

    Payment payment =
        WebClient.builder().defaultHeader("shc","askha").baseUrl("http://localhost:9091/payments").build().get()
                                     .uri("/2")
                                     .retrieve()
                                     .bodyToMono(Payment.class)
                                     .block();
    return new Order(id, "LED", Date.from(Instant.now()), payment);
  }

  @GetMapping("/flux/entity/{id}")
  public Order getOrderWithFluxEntity(@PathVariable int id) {

    ResponseEntity<Payment> payment = WebClient.create("http://localhost:9091/payments")
        .get()
                                               .uri("/2")
                                               .retrieve()
                                               .toEntity(Payment.class)
                                               .block();

    return new Order(id, "LED", Date.from(Instant.now()), payment.getBody());

  }

}
