package api;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Reads customer data from a specified file and populates a collection
 * of Customer objects. Provides methods to retrieve and display the
 * loaded customer data.
 */
public class CustomersReader {
    private final HashMap<String, Customer> customers;

    /**
     * Constructs a CustomersReader instance and initializes the customers
     * collection by reading data from a file named "customers.txt".
     * Each customer's data includes:
     * - Username
     * - Password
     * - First name
     * - Surname
     *
     * @throws RuntimeException if there is an error reading the file.
     */
    public CustomersReader( String fileName ) {



        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            customers = new HashMap<>();

            while ((line = reader.readLine()) != null) {

                String username =(line.substring( line.indexOf(':') + 1 ).trim());
                line = reader.readLine();
                String password = (line.substring( line.indexOf(':') + 1 ).trim());
                line = reader.readLine();
                String firstName =(line.substring( line.indexOf(':') + 1 ).trim());
                line = reader.readLine();
                String surname = (line.substring( line.indexOf(':') + 1 ).trim());
                Customer customer = new Customer(username, password, firstName, surname);

                customers.put(username,customer);

            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retrieves the map of customers loaded from the file.
     *
     * @return a HashMap where the keys are customer usernames
     *         and the values are Customer objects.
     */
    public HashMap<String, Customer> getCustomers() {
        return customers;
    }

}
