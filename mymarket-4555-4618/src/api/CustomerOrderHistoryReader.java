package api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads and parses a customer's order history from a specified file.
 * Creates a list of orders based on the data in the file.
 */
public class CustomerOrderHistoryReader {
    ArrayList<Order> orders = new ArrayList<>();

    /**
     * Constructs a CustomerOrderHistoryReader instance and initializes the
     * customer's order history by reading data from the specified file.
     * If the file does not exist, it creates a new empty file.
     * Each order is parsed with the following details:
     * - Status
     * - Date
     * - List of products
     * - Total cost
     *
     * @param fileName the path to the file containing the customer's order history.
     * @throws RuntimeException if there is an error creating or reading the file.
     */
    CustomerOrderHistoryReader(String fileName) {

        File file = new File(fileName);
        if (file.exists()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String status = (line.substring(line.indexOf(':') + 1).trim());
                    line = reader.readLine();
                    String date = (line.substring(line.indexOf(':') + 1).trim());
                    line = reader.readLine();

                    ArrayList<String> productsList = new ArrayList<>();

                    line = line.substring(line.indexOf(':') + 1).trim();
                    while (line.contains("|")) {
                        String productLine = line.substring(0, line.indexOf('|'));
                        productsList.add(productLine);
                        line = line.substring(line.indexOf('|') + 1).trim();
                    }

                    line = reader.readLine();
                    String totalCost = (line.substring(line.indexOf(':') + 1).trim());
                    Order newOrder = new Order(status, date, productsList, totalCost);
                    orders.add(newOrder);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Retrieves the list of orders from the customer's order history.
     *
     * @return an ArrayList of Order objects representing the order history.
     */
    public ArrayList<Order> getOrders() {
        return orders;
    }

}

