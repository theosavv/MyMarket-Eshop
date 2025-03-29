package api;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Writes the active cart data of all customers to individual files.
 * Each file is created or overwritten with the current cart contents
 * for a specific customer.
 */
public class CustomerCartsWriter {

    /**
     * Constructs a CustomerCartsWriter instance and writes the active cart
     * data for all customers to their respective files. Each customer's cart
     * is saved to a file named based on their username, located in the
     * "CustomersActiveCarts" directory.
     * The file format includes details for each product in the cart:
     * - Title
     * - Description
     * - Category
     * - Subcategory
     * - Price (formatted with a comma as the decimal separator)
     * - Quantity (with appropriate measurement units)
     *
     * @throws RuntimeException if there is an error writing to a file.
     */
    public CustomerCartsWriter()
        {
             Database database = Database.getInstance();
             HashMap<String,Customer> allCustomers = database.getAllCustomers();
             for(Customer customer : allCustomers.values())
             {
                 try (FileWriter writer = new FileWriter("src/api/textFiles/" + customer.getUsername() +"_activeCart" + ".txt", false)) {
                     ArrayList<Product> products = customer.getCart();
                     for(Product product : products){
                         writer.write("Τίτλος: "+ product.getProductTitle() +"\n");
                         writer.write("Περιγραφή: "+ product.getProductDescription() +"\n");
                         writer.write("Κατηγορία: "+ product.getProductCategory() +"\n");
                         writer.write("Υποκατηγορία: "+ product.getProductSubCategory() +"\n");
                         writer.write("Τιμή: " + String.valueOf(product.getProductPrice()).replace('.', ',') + "€\n");
                         if(product.getProductMeasurementUnit().equals("kg"))
                             writer.write("Ποσότητα: " + product.getProductQuantity()  + product.getProductMeasurementUnit() +"\n");
                         else{
                             writer.write("Ποσότητα: " + product.getProductQuantity()+ " " + product.getProductMeasurementUnit() +"\n");
                         }

                     }
                 }
                 catch (IOException e) {
                        e.printStackTrace();
                 }

             }

        }
}

