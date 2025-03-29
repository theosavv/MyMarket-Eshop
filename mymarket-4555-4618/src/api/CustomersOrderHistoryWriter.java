package api;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Writes updated order history for all customers to their respective files.
 * Orders with a status of "Εκκρεμής" are marked as "Ολοκληρωμένη"
 * and appended to the customer's order history file.
 */
public class CustomersOrderHistoryWriter {

    /**
     * Constructs a CustomersOrderHistoryWriter instance and updates the order
     * history for all customers. For each customer, orders marked as
     * "Εκκρεμής" are updated to "Ολοκληρωμένη" and written to a file named
     * based on the customer's username in the "CustomersOrderHistory" directory.
     * The file format includes details for each completed order:
     * - Status (updated to "Ολοκληρωμένη")
     * - Date
     * - List of products (separated by "|")
     * - Total order cost
     *
     * @throws RuntimeException if there is an error writing to a file.
     */
    public CustomersOrderHistoryWriter() {


        Database database = Database.getInstance();
        HashMap<String, Customer> allCustomers = database.getAllCustomers();
        for (Customer customer : allCustomers.values()) {
            try (FileWriter writer = new FileWriter("src/api/textFiles/" + customer.getUsername() + ".txt", true)) {
                for (Order order : customer.getCustomerOrderHistory()) {
                    if (order.status().equals("Εκκρεμής")) {
                        writer.write("Status: Ολοκληρωμένη\n");
                        writer.write("Date: " + order.orderDate() + "\n");
                        writer.write("boughtProducts: ");
                        for (String product : order.boughtProducts())
                            writer.write(product + "|");
                        writer.write("\n");
                        writer.write("totalOrderCost: " + order.totalOrderCost() + "\n");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

