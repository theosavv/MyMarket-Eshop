package api;

import org.junit.Test;

import static org.junit.Assert.*;
public class CustomerOrderHistoryReaderTest {


    String correctOrderHistoryFilename = "test/api/TestTextFiles/correctOrderHistory.txt";
    String emptyOrderHistoryFilename = "test/api/TestTextFiles/emptyOrderHistory.txt";
    @Test
    public void getOrdersEmptyOrderHistory() {
        CustomerOrderHistoryReader reader = new CustomerOrderHistoryReader(correctOrderHistoryFilename);
        assertEquals(reader.getOrders().size() , 2);
    }
    @Test
    public void getOrdersCorrectOrderHistory() {
        CustomerOrderHistoryReader reader = new CustomerOrderHistoryReader(emptyOrderHistoryFilename);
        assertTrue(reader.getOrders().isEmpty());

    }
}