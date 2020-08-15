package com.split.bills.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.split.bills.rest.dto.BillDTO;
import com.split.bills.rest.dto.ContributionDTO;
import com.split.bills.service.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class BillsControllerTest {
  BillService billService;
  BillsController billsController;

  @BeforeEach
  void setUp() {
     billService = mock(BillService.class);
     billsController = new BillsController(billService);
  }

  @Test
  public void testAddBill(){
    ContributionDTO contributionDTO1 = ContributionDTO.builder().personName("A").liableAmount(20).paidAmount(0).build();
    ContributionDTO contributionDTO2 = ContributionDTO.builder().personName("B").liableAmount(0).paidAmount(20).build();
    ContributionDTO contributionDTO3 = ContributionDTO.builder().personName("C").liableAmount(0).paidAmount(0).build();
    ContributionDTO contributionDTO4 = ContributionDTO.builder().personName("D").liableAmount(0).paidAmount(0).build();

    BillDTO billDTO = BillDTO.builder().contributionDTO(contributionDTO1).contributionDTO(contributionDTO2).contributionDTO(contributionDTO3).contributionDTO(contributionDTO4).build();

    when(billService.addBill(billDTO)).thenReturn(Boolean.TRUE);
    assertEquals(billsController.addBill(billDTO), "Successfully added bill");


    when(billService.addBill(billDTO)).thenReturn(Boolean.FALSE);
    assertEquals(billsController.addBill(billDTO), "Failed to add bill");
  }


  @Test
  public void getTransactionSummary(){
    ContributionDTO contributionDTO1 = ContributionDTO.builder().personName("A").liableAmount(20).paidAmount(0).build();
    ContributionDTO contributionDTO2 = ContributionDTO.builder().personName("B").liableAmount(0).paidAmount(20).build();
    ContributionDTO contributionDTO3 = ContributionDTO.builder().personName("C").liableAmount(0).paidAmount(0).build();
    ContributionDTO contributionDTO4 = ContributionDTO.builder().personName("D").liableAmount(0).paidAmount(0).build();

    BillDTO billDTO = BillDTO.builder().contributionDTO(contributionDTO1).contributionDTO(contributionDTO2).contributionDTO(contributionDTO3).contributionDTO(contributionDTO4).build();

    when(billService.addBill(billDTO)).thenReturn(Boolean.TRUE);
    assertEquals(billsController.addBill(billDTO), "Successfully added bill");


    when(billService.getTransactionSummary()).thenReturn(String.format("%s pays Rs. %.2f to %s, ", "B", 20.0D, "A"));
    assertEquals(billsController.getTransactionSummary(), "B pays Rs. 20.00 to A, ");
  }
}