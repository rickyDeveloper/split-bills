package com.split.bills.rest.dto;

import lombok.Data;

@Data
public class ContributionDTO {
  private String personName;
  private double liableAmount;
  private double paidAmount;
}
