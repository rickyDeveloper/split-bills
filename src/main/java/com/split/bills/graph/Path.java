package com.split.bills.graph;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Path {
  String name;
  double value;
  Direction direction;
  String pathNodes;
}
