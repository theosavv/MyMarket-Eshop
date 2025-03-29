package api;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Writes customer data to a specified file. Overwrites the existing file
 * with the provided customer details, saving each customer's username,
 * password, first name, and surname.
 */
public class CustomersWriter {

    /**
     * Constructs a CustomersWriter instance and writes the provided customer
     * data to a file named "customers.txt". The file format includes:
     * - Username
     * - Password
     * - First name
     * - Surname
     *
     * @param customers a HashMap where the keys are customer usernames
     *                  and the values are Customer objects to be written to the file.
     * @throws RuntimeException if there is an error writing to the file.
     */
    public CustomersWriter(HashMap<String, Customer> customers)  {

        try (FileWriter writer = new FileWriter("src/api/textFiles/customers.txt", false)) {
                for (String key : customers.keySet())
                    writer.write(  "username: " + key + "\n" + "password: " + customers.get(key).getPassword()+"\n"+"firstName: "+customers.get(key).getFirstName()+"\n"+"surname: "+customers.get(key).getSurname()+"\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
