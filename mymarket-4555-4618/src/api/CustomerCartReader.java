package api;

import java.io.*;
import java.util.ArrayList;

/**
 * Reads and parses a customer's cart data from a specified file.
 * Populates the cart with products and calculates the total cost.
 */
public class CustomerCartReader {
    ArrayList<Product> products = new ArrayList<>();
    double totalCartCost =0;

    /**
     * Constructs a CustomerCartReader instance and initializes the cart
     * by reading product data from the specified file. If the file does
     * not exist, it creates a new empty file.
     *
     * @param fileName the path to the file containing the customer's cart data.
     * @throws RuntimeException if there is an error creating or reading the file.
     */
    CustomerCartReader(String fileName) {

        File file = new File(fileName);

        if (file.exists()) {


            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String productTitle = (line.substring(line.indexOf(':') + 1).trim());
                    line = reader.readLine();
                    String productDescription = (line.substring(line.indexOf(':') + 1).trim());
                    line = reader.readLine();
                    String productCategory = (line.substring(line.indexOf(':') + 1).trim());
                    line = reader.readLine();
                    String productSubCategory = (line.substring(line.indexOf(':') + 1).trim());
                    line = reader.readLine();
                    line = line.replace("€", "").replace(",", ".");
                    double productPrice = Double.parseDouble(line.substring(line.indexOf(':') + 1));
                    line = reader.readLine();
                    String productMeasurementUnit;
                    int productQuantity;
                    if (line.contains("kg")) {

                        line = line.replace("kg", "");
                        productQuantity = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());
                        productMeasurementUnit = "kg";
                    } else {
                        line = line.replace("τεμάχια", "");
                        productQuantity = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());
                        productMeasurementUnit = "τεμάχια";
                    }

                    Product product = new Product(productTitle, productDescription, productCategory, productSubCategory, productPrice, productQuantity, productMeasurementUnit);
                    products.add(product);
                    totalCartCost += productPrice * productQuantity;
                }


            } catch (IOException ex) {

                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Retrieves the total cost of the products in the customer's cart.
     *
     * @return the total cost of the cart as a double.
     */
    public double getTotalCartCost() {
        return totalCartCost;
    }

    /**
     * Retrieves the list of products in the customer's cart.
     *
     * @return an ArrayList of Product objects representing the cart's contents.
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

}
