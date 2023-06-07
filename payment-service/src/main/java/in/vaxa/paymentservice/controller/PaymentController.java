package in.vaxa.paymentservice.controller;

import in.vaxa.paymentservice.dto.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping("/all")
  public List<Payment> getAllPayments() {
    List<Payment> payments = new ArrayList<>();
    payments.add(new Payment(1, "MLF67", Date.from(Instant.now()),1999.0));
    payments.add(new Payment(2, "SAS67", Date.from(Instant.now()),4999.0));
    payments.add(new Payment(3, "XX123", Date.from(Instant.now()),599.0));

    return payments;
  }

  @GetMapping("/{id}")
  public Payment getPayment(@PathVariable int id) {
    return new Payment(id, "KLM12", Date.from(Instant.now()),1999.0);
  }

}
