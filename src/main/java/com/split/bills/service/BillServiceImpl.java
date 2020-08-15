package com.split.bills.service;

import com.split.bills.calculator.BillsCalculator;
import com.split.bills.rest.dto.BillDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Service
@Slf4j
public class BillServiceImpl implements BillService {

  private BillsCalculator billsCalculator;

  @Autowired
  public BillServiceImpl(BillsCalculator billsCalculator) {
    this.billsCalculator = billsCalculator;
  }

  @Override
  public boolean addBill(BillDTO billDTO) {
    final Map<String, Double> toReceiveMap = new HashMap<>();
    final Map<String, Double> toPayMap = new HashMap<>();

    billDTO.getContributionDTOS().forEach(c -> {
      log.info("Adding payment for person {}", c.getPersonName());
      double diff = c.getPaidAmount() - c.getLiableAmount();
      if (diff > 0.0D) {
        toReceiveMap.put(c.getPersonName(), diff);
      } else if (diff < 0.0D) {
        toPayMap.put(c.getPersonName(), Math.abs(diff));
      }
    });

    processContributions(toReceiveMap, toPayMap);

    return true;
  }

  @Override
  public String getTransactionSummary() {
    return billsCalculator.getAllTransactions();
  }

  /**
   * This method take the receiver and payer and then calculates the net payable
   * In addition, it creates a payment graph with payment edges.
   *
   * @param toReceiveMap
   * @param toPayMap
   */
  private void processContributions(Map<String, Double> toReceiveMap, Map<String, Double> toPayMap) {
    for (Entry<String, Double> toReceiveEntry : toReceiveMap.entrySet()) {
      String paymentReceiver = toReceiveEntry.getKey();
      double amountToBeReceived = toReceiveEntry.getValue();

      for (Entry<String, Double> entry : toPayMap.entrySet())
        if (entry.getValue() > 0.0D) {
          String paymentSender = entry.getKey();
          double amountToBePaid;
          if (amountToBeReceived >= entry.getValue()) {
            amountToBePaid = entry.getValue();
          } else {
            amountToBePaid = amountToBeReceived;
            amountToBeReceived = 0.0D;
          }

          billsCalculator.registerTransaction(paymentReceiver, paymentSender, amountToBePaid);

          amountToBeReceived -= amountToBePaid;
          entry.setValue(entry.getValue() - amountToBePaid);

          if (amountToBeReceived <= 0) {
            break;
          }
        }
    }
  }
}
