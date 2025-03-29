package api;

import org.junit.Test;

import static org.junit.Assert.*;
public class CustomersReaderTest {

    String correctCustomersFilename = "test/api/TestTextFiles/correctCustomers.txt";
    String emptyCustomersFilename = "test/api/TestTextFiles/emptyCustomers.txt";
    @Test
    public void getCustomersEmpty() {
        CustomersReader reader = new CustomersReader(emptyCustomersFilename);
        assertEquals(reader.getCustomers().size(), 0);

    }
    @Test
    public void getCustomersCorrect() {
        CustomersReader reader = new CustomersReader(correctCustomersFilename);
        assertEquals(reader.getCustomers().size(), 2);

    }
}