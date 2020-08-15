package com.split.bills.graph;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.split.bills.calculator.BillsCalculator;
import com.split.bills.service.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class PaymentGraphTest {

  private PaymentGraph paymentGraph;

  String A_PERSON = "A";
  String B_PERSON = "B";
  String C_PERSON = "C";
  String D_PERSON = "D";


  @BeforeEach
  void setUp() {
    paymentGraph = new PaymentGraph();
  }


  @Test
  void addPaymentEdges() {
    paymentGraph.addPaymentEdges(B_PERSON, A_PERSON, 20D);

    Map<String, Double> payMap = paymentGraph.getAdjPaymentEdges().get(A_PERSON);
    assertNotNull(payMap);
    assertEquals(payMap.size(), 1);
    assertTrue(payMap.containsKey(B_PERSON));
    assertEquals(payMap.get(B_PERSON), -20D);

    payMap = paymentGraph.getAdjPaymentEdges().get(B_PERSON);
    assertNotNull(payMap);
    assertEquals(payMap.size(), 1);
    assertTrue(payMap.containsKey(A_PERSON));
    assertEquals(payMap.get(A_PERSON), 20D);
  }

  @Test
  void reduceTransactions() {
    paymentGraph.addPaymentEdges(B_PERSON, A_PERSON, 20D);
    paymentGraph.addPaymentEdges(A_PERSON, D_PERSON, 5D);
    paymentGraph.addPaymentEdges(C_PERSON, B_PERSON, 10D);
    paymentGraph.addPaymentEdges(D_PERSON, C_PERSON, 2D);

    paymentGraph.reduceTransactions();

    Map<String, Double> payMap = paymentGraph.getAdjPaymentEdges().get(A_PERSON);
    assertNotNull(payMap);
    assertTrue(payMap.containsKey(D_PERSON));
    assertEquals(payMap.get(D_PERSON), 3D);

    payMap = paymentGraph.getAdjPaymentEdges().get(B_PERSON);
    assertNotNull(payMap);
    assertTrue(payMap.containsKey(A_PERSON));
    assertEquals(payMap.get(A_PERSON), 18D);

    payMap = paymentGraph.getAdjPaymentEdges().get(C_PERSON);
    assertNotNull(payMap);
    assertTrue(payMap.containsKey(D_PERSON));
    assertEquals(payMap.get(D_PERSON), 0.0D);

    assertTrue(payMap.containsKey(B_PERSON));
    assertEquals(payMap.get(B_PERSON), 8D);
  }

  @Test
  void getTransactionSummary() {
    paymentGraph.addPaymentEdges(B_PERSON, A_PERSON, 20D);
    paymentGraph.addPaymentEdges(A_PERSON, D_PERSON, 5D);
    paymentGraph.addPaymentEdges(C_PERSON, B_PERSON, 10D);
    paymentGraph.addPaymentEdges(D_PERSON, C_PERSON, 2D);
    paymentGraph.reduceTransactions();
    String summary = paymentGraph.getTransactionSummary();
    assertEquals(summary, "D pays Rs. 3.00 to A, A pays Rs. 18.00 to B, B pays Rs. 8.00 to C, ");
  }
}