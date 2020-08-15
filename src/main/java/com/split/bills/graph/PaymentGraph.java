package com.split.bills.graph;

import static com.split.bills.graph.Direction.INCOMING;
import static com.split.bills.graph.Direction.OUTGOING;
import static com.split.bills.graph.Direction.ROOT;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This is a payment graph with details of who needs to how much amount
 * To normalize/reduce the transaction we will do a Breadth First Search
 * and find a route from one node to the target node and accordingly update
 * the amount to be paid/received.
 */
@Data
@Component
public class PaymentGraph {
  private Map<String, Map<String, Double>> adjPaymentEdges;

  public PaymentGraph() { adjPaymentEdges = new HashMap<>(); }

  public void addPaymentEdges(String paymentReceiver, String paymentSender,
                              double amount) {
    addOrUpdatePaymentEdge(paymentReceiver, paymentSender, amount);
    addOrUpdatePaymentEdge(paymentSender, paymentReceiver, -amount);
  }

  private void addOrUpdatePaymentEdge(String fromNode, String toNode,
      double amount) {
    Map<String, Double> paymentEdges;
    if (adjPaymentEdges.containsKey(fromNode)) {
      paymentEdges = adjPaymentEdges.get(fromNode);
      if (paymentEdges.containsKey(toNode)) {
        amount += paymentEdges.get(toNode);
      }
    } else {
      paymentEdges = new HashMap<>();
      adjPaymentEdges.put(fromNode, paymentEdges);
    }
    paymentEdges.put(toNode, amount);
  }

  /**
   * This method will traverse the graph and then update the
   * transaction amount.
   */
  public void reduceTransactions() {
    adjPaymentEdges.entrySet().forEach(root -> {
      Map<String, Double> paymentMap = root.getValue();

      paymentMap.entrySet()
          .stream()
          .filter(p -> p.getValue() > 0.0D)
          .forEach(p -> {
            traverseGraphAndUpdateTransaction(root.getKey(), p.getKey());
          });
    });
  }

  /**
   * This method is for the transaction summary
   * @return
   */
  public String getTransactionSummary() {
    final StringBuilder summary = new StringBuilder();

    adjPaymentEdges.entrySet().stream().forEach(e -> {
      e.getValue()
          .entrySet()
          .stream()
          .filter(n -> n.getValue() > 0.0d)
          .forEach(n
              -> summary.append(String.format("%s pays Rs. %.2f to %s, ",
              n.getKey(), n.getValue(),
              e.getKey())));
    });

    adjPaymentEdges = new HashMap<>(); // I am doing this only for testing purpose. Want to avoid restart.
    return summary.toString();
  }


  /**
   * Once we find a path whether transaction amount can be reduced we
   * will udpate the amount for all the nodes which comes in the traversed path.
   * @param startNode
   * @param targetNode
   * @param pathNodes
   * @param amount
   */
  private void applyDiscount(String startNode, String targetNode,
                             String pathNodes, double amount) {
    Double currentValue = adjPaymentEdges.get(startNode).get(targetNode);
    adjPaymentEdges.get(startNode).put(targetNode, currentValue - amount);

    currentValue = adjPaymentEdges.get(targetNode).get(startNode);
    adjPaymentEdges.get(targetNode).put(startNode, currentValue + amount);

    String[] nodes = pathNodes.split("#");

    for (int i = 0; i < nodes.length - 1; i++) {
      currentValue = adjPaymentEdges.get(nodes[i]).get(nodes[i + 1]);
      adjPaymentEdges.get(nodes[i]).put(nodes[i + 1], currentValue - amount);

      currentValue = adjPaymentEdges.get(nodes[i + 1]).get(nodes[i]);
      adjPaymentEdges.get(nodes[i + 1]).put(nodes[i], currentValue + amount);
    }
  }


  /**
   * In this method we are doing a Breadth First Traversal and then updating the edges
   * if there is a payment path from currentNode to rootNode
   * @param rootNode
   * @param currentNode
   */
  private void traverseGraphAndUpdateTransaction(String rootNode, String currentNode) {
    Set<String> visited = new HashSet<>();
    LinkedList<Path> queue = new LinkedList<>();

    queue.add(Path.builder()
        .name(currentNode)
        .value(0.0D)
        .direction(ROOT)
        .pathNodes(currentNode)
        .build());
    visited.add(currentNode);

    while (queue.size() != 0) {
      Path path = queue.poll();

      Iterator<Entry<String, Double>> it =
          adjPaymentEdges.get(path.name).entrySet().iterator();

      while (it.hasNext()) {
        Entry<String, Double> adjNode = it.next();

        if (path.name.equals(currentNode) &&
            adjNode.getKey().equals(rootNode)) {
          continue;
        }

        if (!visited.contains(adjNode.getKey()) &&
            adjNode.getValue() != 0.0D) {
          if (adjNode.getKey().equals(rootNode)) {
            applyDiscount(rootNode, currentNode,
                path.pathNodes + "#" + rootNode,
                path.value);
            break;
          } else {
            processPaymentEdge(path, visited, queue, adjNode);
          }
        }
      }
    }
  }

  /**
   * Process the current payment edge and decide whether we need to traverse further or not.
   * @param path
   * @param visited
   * @param queue
   * @param adjNode
   */
  private void processPaymentEdge(Path path, Set<String> visited, LinkedList<Path> queue,  Entry<String, Double> adjNode) {
    switch (path.direction) {
      case ROOT:
        visited.add(adjNode.getKey());
        queue.add(Path.builder()
            .name(adjNode.getKey())
            .value(adjNode.getValue())
            .direction(adjNode.getValue() > 0.0D
                ? INCOMING
                : OUTGOING)
            .pathNodes(path.pathNodes + "#" +
                adjNode.getKey())
            .build());
        break;
      case INCOMING:
        if (adjNode.getValue() > 0.0D) {
          visited.add(adjNode.getKey());
          queue.add(
              Path.builder()
                  .name(adjNode.getKey())
                  .value(Math.min(path.value, adjNode.getValue()))
                  .direction(INCOMING)
                  .pathNodes(path.pathNodes + "#" +
                      adjNode.getKey())
                  .build());
        }
        break;
      case OUTGOING:
        if (adjNode.getValue() < 0.0D) {
          visited.add(adjNode.getKey());
          queue.add(
              Path.builder()
                  .name(adjNode.getKey())
                  .value(Math.max(path.value, adjNode.getValue()))
                  .direction(OUTGOING)
                  .pathNodes(path.pathNodes + "#" +
                      adjNode.getKey())
                  .build());
        }
        break;
    }
  }
}
