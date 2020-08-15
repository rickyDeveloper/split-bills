package com.split.bills.rest;

import com.split.bills.rest.dto.BillDTO;
import com.split.bills.service.BillService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BillsController {
  private BillService billService;

  @Autowired
  public BillsController(BillService billService) {
    this.billService  = billService;
  }

  @PostMapping("/bills")
  @ResponseBody
  public String addBill(@NonNull @RequestBody BillDTO billDTO) {
    log.info("Adding a new bill ");
    return billService.addBill(billDTO) ? "Successfully added bill"
                                        : "Failed to add bill";
  }

  @GetMapping("/summary")
  @ResponseBody
  public String getTransactionSummary() {
    log.info("Calculating bill split");
    return billService.getTransactionSummary();
  }
}
