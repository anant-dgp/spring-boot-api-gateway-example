package in.vaxa.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
  private int id;
  private String txnId;
  private Date txnDate;
  private double amount;

}
