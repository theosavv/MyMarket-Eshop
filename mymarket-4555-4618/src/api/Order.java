package api;

import java.util.ArrayList;

/**
 * The Order record represents a customer order containing information
 * about the date of the order, the products purchased, and the total
 * cost of the order.
 *
 * @param status           the status of the order ("Ολοκληρωμένη" or"Εκκρεμής")
 * @param orderDate        the date the order was placed, in the format "DD/MM/YY" (e.g., "15/11/24")
 * @param boughtProducts   a list of product titles purchased in the order (e.g., ["Yogurt", "Orange Juice"])
 * @param totalOrderCost   the total cost of the order (e.g., "129,99€")
 */
public record Order(String status,String orderDate, ArrayList<String> boughtProducts, String totalOrderCost) {
}
