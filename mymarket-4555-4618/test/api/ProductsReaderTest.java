package api;

import org.junit.Test;

import static org.junit.Assert.*;
public class ProductsReaderTest {

    String correctProductsFilename = "test/api/TestTextFiles/correctProducts.txt";
    String emptyProductsFilename = "test/api/TestTextFiles/emptyProducts.txt";
    @Test
    public void getProductsEmpty() {
        ProductsReader reader = new ProductsReader(emptyProductsFilename);
        assertTrue(reader.getProducts().isEmpty());
    }
    @Test
    public void getProductsCorrect() {
        ProductsReader reader = new ProductsReader(correctProductsFilename);
        assertEquals(reader.getProducts().size(), 2);
    }
}