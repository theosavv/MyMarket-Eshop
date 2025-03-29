package api;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
public class CustomerCartReaderTest {

    String correctCartFilename = "test/api/TestTextFiles/correctCart.txt";
    String emptyCartFilename = "test/api/TestTextFiles/emptyCart.txt";

    @Test
    public void getTotalCartCostNormalCart() {
        CustomerCartReader reader =new CustomerCartReader(correctCartFilename);
        double cost = reader.getTotalCartCost();
        assertEquals(cost,4,0.0001);
    }
    @Test
    public void getTotalCartCostEmptyCart() {
        CustomerCartReader reader =new CustomerCartReader(emptyCartFilename);
        double cost = reader.getTotalCartCost();
        assertEquals(cost,0,0.0001);
    }

    @Test
    public void getProductsFullCart() {
        CustomerCartReader reader =new CustomerCartReader(correctCartFilename);
        ArrayList<Product> products = reader.getProducts();
        assertEquals(products.getFirst().getProductTitle(),"Πορτοκάλια 1kg");
        assertEquals(products.size(),2);
    }

    @Test
    public void getProductsEmptyCart() {
        CustomerCartReader reader =new CustomerCartReader(emptyCartFilename);
        ArrayList<Product> products = reader.getProducts();
        assertTrue(products.isEmpty());
    }
}