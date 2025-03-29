package api;

import java.util.*;

/**
 * The {@code Database} class is a singleton responsible for managing and storing the product and customer data.
 * It handles product and customer retrieval, addition, and filtering by categories or subcategories.
 * This class ensures that only one instance of the database exists throughout the application.
 */

public class Database {


    private static Database instance;
    private ArrayList<Product> allProducts;
    private HashMap<String, Customer> allCustomers;
    private ArrayList<String > categories;
    private ArrayList<String > subCategories;

    /**
     * Private constructor for the {@code Database} class. Initializes the lists of products, customers, categories,
     * and subcategories. Loads product and customer data from external sources through the respective readers.
     * Initializes product and customer data through {@link ProductsReader} and {@link CustomersReader} classes.
     * Populates categories and subcategories to organize product types.
     */
    private Database() {

     /**
     * @param instance The single instance of the {@code Database} class.
     * @param allProducts A list that holds all the products in the database.
     * @param allCustomers A map that stores all customers, indexed by their usernames.
     * @param categories A list of product categories available in the database.
     * @param subCategories A list of product subcategories available in the database.
     */
        allProducts = new ArrayList<>();
        allCustomers = new HashMap<>();

        String fileName = "src/api/textFiles/products.txt";
        ProductsReader myProductsReader = new ProductsReader(fileName);
        allProducts = myProductsReader.getProducts();

        String filename = "src/api/textFiles/customers.txt";
        CustomersReader myCustomersReader = new CustomersReader(filename);
        allCustomers = myCustomersReader.getCustomers();

        this.categories = new ArrayList<>();
        this.subCategories = new ArrayList<>();
        Collections.addAll(categories,"Φρέσκα τρόφιμα","Κατεψυγμένα τρόφιμα","Προϊόντα ψυγείου"
                ,"Αλλαντικά","Αλκοολούχα ποτά","Μη αλκοολούχα ποτά","Καθαριστικά για το σπίτι"
                ,"Απορρυπαντικά ρούχων","Καλλυντικά","Προϊόντα στοματικής υγιεινής","Πάνες"
                ,"Δημητριακά","Ζυμαρικά","Σνακ","Έλαια","Κονσέρβες","Χαρτικά");
        Collections.addAll(subCategories, "Φρούτα", "Λαχανικά", "Ψάρια", "Κρέατα", "Κατεψυγμένα λαχανικά",
                "Κατεψυγμένα κρέατα", "Κατεψυγμένες πίτσες", "Κατεψυγμένα γεύματα", "Τυριά", "Γιαούρτια",
                "Γάλα", "Βούτυρο", "Ζαμπόν", "Σαλάμι", "Μπέικον", "Μπύρα", "Κρασί", "Ούζο", "Τσίπουρο",
                "Χυμοί", "Αναψυκτικά", "Νερό", "Ενεργειακά ποτά", "Καθαριστικά για το πάτωμα",
                "Καθαριστικά για τα τζάμια", "Καθαριστικά κουζίνας", "Σκόνες πλυντηρίου", "Υγρά πλυντηρίου",
                "Μαλακτικά", "Κρέμες προσώπου", "Μακιγιάζ", "Λοσιόν σώματος", "Οδοντόκρεμες",
                "Οδοντόβουρτσες", "Στοματικά διαλύματα", "Πάνες για μωρά", "Πάνες ενηλίκων",
                "Νιφάδες καλαμποκιού", "Μούσλι", "Βρώμη", "Μακαρόνια", "Κριθαράκι", "Ταλιατέλες",
                "Πατατάκια", "Κράκερς", "Μπάρες δημητριακών", "Ελαιόλαδο", "Ηλιέλαιο", "Σογιέλαιο",
                "Κονσέρβες ψαριών", "Κονσέρβες λαχανικών", "Κονσέρβες φρούτων", "Χαρτί υγείας",
                "Χαρτοπετσέτες", "Χαρτομάντηλα");


    }




    /**
     * Returns the singleton instance of the {@code Database} class.
     *
     * @return The single instance of the {@code Database}.
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Gets the list of all products in the database.
     *
     * @return A list of all {@link Product} objects stored in the database.
     */
    public ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Retrieves a specific product from the database by comparing titles.
     *
     * @param product The {@link Product} object to search for in the database.
     * @return The {@link Product} object if found, {@code null} if not found.
     */
    public Product getSpecificProduct(Product product) {
        for (Product p : allProducts) {
            if(product.getProductTitle().equals(p.getProductTitle())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Checks whether a product already exists in the database by comparing titles.
     *
     * @param product The {@link Product} object to check for existence in the database.
     * @return {@code true} if the product exists in the database, {@code false} otherwise.
     */

    public boolean productExists(Product product) {
        for (Product p : allProducts) {
            if(p.getProductTitle().equals(product.getProductTitle())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new product to the database.
     *
     * @param product The {@link Product} object to add to the database.
     */
    public void addNewProduct(Product product) {
        allProducts.add(product);
    }

    /**
     * Gets a list of all products in the specified category.
     *
     * @param category The category by which to filter the products.
     * @return A list of {@link Product} objects that belong to the specified category.
     */
    public ArrayList<Product> getAllProductsByCategory(String category) {
        ArrayList<Product> products = new ArrayList<>();
        for (Product p : allProducts) {
            if(p.getProductCategory().equals(category)) {
                products.add(p);
            }
        }
        return products;
    }

    /**
     * Gets a list of all products in the specified subcategory.
     *
     * @param subCategory The subcategory by which to filter the products.
     * @return A list of {@link Product} objects that belong to the specified subcategory.
     */
    public ArrayList<Product> getAllProductsBySubCategory(String subCategory) {
        ArrayList<Product> products = new ArrayList<>();
        for (Product p : allProducts) {
            if(p.getProductSubCategory().equals(subCategory)) {
                products.add(p);
            }
        }
        return products;
    }

    /**
     * Adds a new customer to the database.
     *
     * @param username The username of the customer.
     * @param customer The {@link Customer} object representing the customer to add.
     */
    public void addCustomer(String username, Customer customer) {
        allCustomers.put(username,customer);
    }

    /**
     * Gets a map of all customers in the database.
     *
     * @return A map of all {@link Customer} objects, where the key is the customer's username.
     */
    public HashMap<String, Customer> getAllCustomers() {
        return allCustomers;
    }

    /**
     * Retrieves a specific customer from the database by their username.
     *
     * @param customer The {@link Customer} object to search for by username.
     * @return The {@link Customer} object if found, {@code null} if not found.
     */
    public Customer getSpecificCustomer(Customer customer) {
        for (Customer customer1 : allCustomers.values()) {
            if(customer.getUsername().equals(customer1.getUsername())) {
                return customer1;
            }
        }
        return null;
    }


    /**
     * Retrieves the list of all product categories in the database.
     *
     * <p>This method returns an {@link ArrayList} containing all categories available in the database.
     * Categories represent the primary classification of products.</p>
     *
     * @return An {@link ArrayList} of product categories.
     */

    public ArrayList<String> getCategories() {
        return categories;
    }

    /**
     * Retrieves the subcategories for a given category.
     *
     * <p>This method returns an {@link ArrayList} of subcategories that belong to the specified category.
     * If the category is not recognized, an empty list is returned.</p>
     *
     * @param category The name of the category for which subcategories are requested.
     * @return An {@link ArrayList} of subcategories for the given category, or an empty list if the category is not found.
     */
    public ArrayList<String> getSubCategories(String category) {
        return switch (category) {
            case "Φρέσκα τρόφιμα" -> new ArrayList<>(Arrays.asList("Φρούτα", "Λαχανικά", "Ψάρια", "Κρέατα"));
            case "Κατεψυγμένα τρόφιμα" ->
                    new ArrayList<>(Arrays.asList("Κατεψυγμένα λαχανικά", "Κατεψυγμένα κρέατα", "Κατεψυγμένες πίτσες", "Κατεψυγμένα γεύματα"));
            case "Προϊόντα ψυγείου" -> new ArrayList<>(Arrays.asList("Τυριά", "Γιαούρτια", "Γάλα", "Βούτυρο"));
            case "Αλλαντικά" -> new ArrayList<>(Arrays.asList("Ζαμπόν", "Σαλάμι", "Μπέικον"));
            case "Αλκοολούχα ποτά" -> new ArrayList<>(Arrays.asList("Μπύρα", "Κρασί", "Ούζο", "Τσίπουρο"));
            case "Μη αλκοολούχα ποτά" ->
                    new ArrayList<>(Arrays.asList("Χυμοί", "Αναψυκτικά", "Νερό", "Ενεργειακά ποτά"));
            case "Καθαριστικά για το σπίτι" ->
                    new ArrayList<>(Arrays.asList("Καθαριστικά για το πάτωμα", "Καθαριστικά για τα τζάμια", "Καθαριστικά κουζίνας"));
            case "Απορρυπαντικά ρούχων" ->
                    new ArrayList<>(Arrays.asList("Σκόνες πλυντηρίου", "Υγρά πλυντηρίου", "Μαλακτικά"));
            case "Καλλυντικά" -> new ArrayList<>(Arrays.asList("Κρέμες προσώπου", "Μακιγιάζ", "Λοσιόν σώματος"));
            case "Προϊόντα στοματικής υγιεινής" ->
                    new ArrayList<>(Arrays.asList("Οδοντόκρεμες", "Οδοντόβουρτσες", "Στοματικά διαλύματα"));
            case "Πάνες" -> new ArrayList<>(Arrays.asList("Πάνες για μωρά", "Πάνες ενηλίκων"));
            case "Δημητριακά" -> new ArrayList<>(Arrays.asList("Νιφάδες καλαμποκιού", "Μούσλι", "Βρώμη"));
            case "Ζυμαρικά" -> new ArrayList<>(Arrays.asList("Μακαρόνια", "Κριθαράκι", "Ταλιατέλες"));
            case "Σνακ" -> new ArrayList<>(Arrays.asList("Πατατάκια", "Κράκερς", "Μπάρες δημητριακών"));
            case "Έλαια" -> new ArrayList<>(Arrays.asList("Ελαιόλαδο", "Ηλιέλαιο", "Σογιέλαιο"));
            case "Κονσέρβες" ->
                    new ArrayList<>(Arrays.asList("Κονσέρβες ψαριών", "Κονσέρβες λαχανικών", "Κονσέρβες φρούτων"));
            case "Χαρτικά" -> new ArrayList<>(Arrays.asList("Χαρτί υγείας", "Χαρτοπετσέτες", "Χαρτομάντηλα"));
            default -> new ArrayList<>();
        };
    }



    /**
     * Calls all writer classes to persist data to storage.
     *
     * <p>This method triggers the following writers:
     * <ul>
     *     <li>{@link CustomersWriter} - Saves customer data to storage.</li>
     *     <li>{@link ProductsWriter} - Saves product data to storage.</li>
     *     <li>{@link CustomerCartsWriter} - Saves shopping cart data for all customers to storage.</li>
     *     <li>{@link CustomersOrderHistoryWriter} - Saves order history for all customers to storage.</li>
     * </ul>
     * </p>
     */

    public void allWritersCall()
    {
        new CustomersWriter(allCustomers);
        new ProductsWriter();
        new CustomerCartsWriter();
        new CustomersOrderHistoryWriter();
    }

    /**
     * Retrieves a list of products that are currently out of stock (i.e., have a quantity of 0).
     *
     * @return A list of {@link Product} objects that have a quantity of 0, indicating they are unavailable.
     */
    public ArrayList<Product> unavailableProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (Product p : allProducts) {
            if(p.getProductQuantity()==0)
                products.add(p);
        }
        return products;
    }

    /**
     * Retrieves a list of the most frequently bought products from all customers' order history.
     * The method computes the frequency of each product and returns the top 'xTopProducts' most frequent products.
     *
     * @param xTopProducts The number of top frequently bought products to return.
     * @return A list of product titles representing the most frequently bought products.
     *         Returns {@code null} if no products were bought.
     */
    public ArrayList<String> frequentlyBoughtProducts(int xTopProducts) {
        ArrayList<String> allProductsBought = new ArrayList<>();
        for (Customer customer : allCustomers.values()) {
            for (Order order : customer.getCustomerOrderHistory()) {
                allProductsBought.addAll(order.boughtProducts());
            }
        }

        Map<String, Integer> productFrequency = new HashMap<>();
        for (String productTitle : allProductsBought) {
            productFrequency.put(productTitle, productFrequency.getOrDefault(productTitle, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productFrequency.entrySet());
        sortedProducts.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());

        ArrayList<String> topProducts = new ArrayList<>();
        int numberOfProductsToReturn = Math.min(xTopProducts, sortedProducts.size());

        for (int i = 0; i < numberOfProductsToReturn; i++) {
            topProducts.add(sortedProducts.get(i).getKey());
        }

        if (topProducts.isEmpty()) {
            return null;
        }

        return topProducts;
    }

}