package com.split.bills.calculator;

import com.split.bills.graph.PaymentGraph;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Slf4j
@Component
public class BillsCalculator {
  @Autowired private final PaymentGraph paymentGraph;

  public void registerTransaction(String paymentReceiver, String paymentSender,
                                  double amountToBePaid) {
    log.info("Person {} has to be pay {} to person {} ", paymentSender,
        amountToBePaid, paymentReceiver);

    paymentGraph.addPaymentEdges(paymentReceiver, paymentSender, amountToBePaid);
  }

  public String getAllTransactions() {
    paymentGraph.reduceTransactions();
    return paymentGraph.getTransactionSummary();
  }
}
