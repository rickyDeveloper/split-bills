package com.split.bills.service;

import com.split.bills.rest.dto.BillDTO;

public interface BillService {
  boolean addBill(BillDTO billDTO);
  String getTransactionSummary();
}
