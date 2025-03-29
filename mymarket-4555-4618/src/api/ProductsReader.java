package api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * This class handles reading product details from a text file and storing them in an {@link ArrayList} of {@link Product} objects.
 * It reads the file line by line, extracting relevant product data such as title, description, category, subcategory, price,
 * quantity, and measurement unit, and then stores the data in {@link Product} objects.
 * The data is assumed to be formatted in a specific way, with each product's details separated by lines, and the file contains
 * product information for multiple products.
 * <p>
 * The products are read from the file located at "src/api/textFiles/products.txt", and after reading the file, an {@link ArrayList}
 * of {@link Product} objects is created and populated with the data from the file.
 */
public class ProductsReader {

    private final ArrayList<Product> products;

    /**
     * Constructor for the ProductsReader class.
     * This constructor reads product data from the "products.txt" file, parsing the relevant information (title, description,
     * category, subcategory, price, quantity, and measurement unit) for each product, and stores the data in a list of
     * {@link Product} objects.
     * <p>
     * The file is read line by line. Empty lines are skipped, and the relevant product details are extracted and parsed. The data
     * is used to create {@link Product} instances, which are added to the {@link ArrayList} of products.
     *
     * @throws RuntimeException if an error occurs while reading the file or parsing its contents.
     */
    public ProductsReader(String fileName)  {


        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            products = new ArrayList<>();

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                    String productTitle =(line.substring( line.indexOf(':') + 1 ).trim());
                    line = reader.readLine();
                    String productDescription =(line.substring(line.indexOf(':') + 1).trim());
                    line = reader.readLine();
                    String productCategory =(line.substring(line.indexOf(':') + 1).trim());
                    line = reader.readLine();
                    String productSubCategory =(line.substring(line.indexOf(':') +1 ).trim());
                    line = reader.readLine();
                    line = line.replace("€", "").replace(",", ".");
                    double productPrice =Double.parseDouble(line.substring(line.indexOf(':') +1 ));
                    line = reader.readLine();
                    String productMeasurementUnit;
                    int productQuantity;
                    if(line.contains("kg")){

                        line = line.replace("kg","");
                        productQuantity = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());
                        productMeasurementUnit = "kg";
                    }
                    else{
                        line =line.replace("τεμάχια","");
                        productQuantity = Integer.parseInt(line.substring(line.indexOf(':') + 1).trim());
                        productMeasurementUnit = "τεμάχια";
                    }
                    Product product = new Product(productTitle,productDescription,productCategory,productSubCategory,productPrice,productQuantity,productMeasurementUnit);
                    products.add(product);


            }

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

    /**
     * Returns the list of products read from the file.
     *
     * @return an {@link ArrayList} of {@link Product} objects representing the products read from the file.
     */
    public ArrayList<Product> getProducts() {
        return products;
    }
}



