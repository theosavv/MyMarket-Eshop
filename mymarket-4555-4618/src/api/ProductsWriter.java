package api;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class handles writing the details of products from the database to a text file.
 * It retrieves a list of all products from the database and writes their details (title, description, category,
 * subcategory, price, and quantity) into a file named "products.txt".
 * Each product's details are written in a readable format, ensuring that units for quantity are properly handled (e.g., kg).
 * <p>
 * This class depends on a singleton instance of the {@link Database} class to fetch all products.
 * The {@link Product} class is used to represent the product data.
 */
public class ProductsWriter {

    /**
     * Constructor for the ProductsWriter class.
     * Retrieves all products from the database and writes their details to a text file.
     * The details include product title, description, category, subcategory, price, and quantity with its measurement unit.
     * If the measurement unit is "kg", the quantity is appended with "kg". For other units, it is written as the quantity followed by the unit.
     * <p>
     * The products are retrieved from the {@link Database} singleton instance.
     *
     */
    public ProductsWriter() {
        Database database = Database.getInstance();
        ArrayList<Product> allProducts = database.getAllProducts();
        try (FileWriter writer = new FileWriter("src/api/textFiles/products.txt", false)) {
            for (Product product : allProducts) {
                writer.write("Τίτλος: "+ product.getProductTitle() +"\n");
                writer.write("Περιγραφή: "+ product.getProductDescription() +"\n");
                writer.write("Κατηγορία: "+ product.getProductCategory() +"\n");
                writer.write("Υποκατηγορία: "+ product.getProductSubCategory() +"\n");
                writer.write("Τιμή: "+ product.getProductPrice() +"€"+"\n");
                if(product.getProductMeasurementUnit().equals("kg"))
                    writer.write("Ποσότητα: " + product.getProductQuantity()  + product.getProductMeasurementUnit() +"\n\n");
                else
                    writer.write("Ποσότητα: " + product.getProductQuantity()+ " " + product.getProductMeasurementUnit() +"\n\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
