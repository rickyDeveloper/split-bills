package com.split.bills.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.split.bills.calculator.BillsCalculator;
import com.split.bills.rest.BillsController;
import com.split.bills.rest.dto.BillDTO;
import com.split.bills.rest.dto.ContributionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillServiceImplTest {

  BillsCalculator billsCalculator;
  BillService billService;

  @BeforeEach
  void setUp() {
    billsCalculator = mock(BillsCalculator.class);
    billService = new BillServiceImpl(billsCalculator);
  }


  @Test
  void addBill() {
    ContributionDTO contributionDTO1 = ContributionDTO.builder().personName("A").liableAmount(20).paidAmount(0).build();
    ContributionDTO contributionDTO2 = ContributionDTO.builder().personName("B").liableAmount(0).paidAmount(20).build();
    ContributionDTO contributionDTO3 = ContributionDTO.builder().personName("C").liableAmount(0).paidAmount(0).build();
    ContributionDTO contributionDTO4 = ContributionDTO.builder().personName("D").liableAmount(0).paidAmount(0).build();

    BillDTO billDTO = BillDTO.builder().contributionDTO(contributionDTO1).contributionDTO(contributionDTO2).contributionDTO(contributionDTO3).contributionDTO(contributionDTO4).build();
    assertTrue(billService.addBill(billDTO));
  }
}