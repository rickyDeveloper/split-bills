package com.split.bills.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class BillDTO {
   private List<ContributionDTO> contributionDTOS;
}
