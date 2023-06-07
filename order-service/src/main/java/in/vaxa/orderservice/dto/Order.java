package in.vaxa.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
  private int id;
  private String name;
  private Date orderDate;
  private Payment paymentId;

}