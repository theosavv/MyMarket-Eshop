package api;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
public class DatabaseTest {





    @Test
    public void getSpecificProduct() {
        Database database = Database.getInstance();
        Product correctProduct =  new Product("Τσίπουρο με Γλυκάνισο 200ml","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,97,"τεμάχια");
        database.addNewProduct(correctProduct);
        Product randomProduct = new Product("Τσίπουρο με Γλυκάνισο 200ml","ο.","Αλ","Τσίρο",0.5,1,"τεμάχια");
        Product product =database.getSpecificProduct(randomProduct);
        assertEquals(correctProduct, product);
    }

    @Test
    public void productExists() {
        Database database = Database.getInstance();
        Product correctProduct =  new Product("Τσίπουρο","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,97,"τεμάχια");
        database.productExists(correctProduct);
        assertFalse(database.productExists(correctProduct));
        database.addNewProduct(correctProduct);
        assertTrue(database.productExists(correctProduct));

    }

    @Test
    public void addNewProduct() {
        Database database = Database.getInstance();
        Product correctProduct =  new Product("Τσίπουρο με Γλυκάνισο 200ml","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,97,"τεμάχια");
        int initialAllProductsSize = database.getAllProducts().size();
        database.addNewProduct(correctProduct);
        assertEquals(initialAllProductsSize+1,database.getAllProducts().size());
    }





    @Test
    public void getSpecificCustomer() {
        Database database = Database.getInstance();

        Customer customer = new Customer("Sakis","sakis123","Athanasios","Giarlopoylos");
        Customer randomCustomer = new Customer("Sakis","is123","Atios","Giarllos");

        database.addCustomer("Sakis",randomCustomer);
        database.addCustomer("Sakis",customer);
        assertEquals(customer, database.getSpecificCustomer(randomCustomer));
    }

    @Test
    public void unavailableProducts() {
        Database database = Database.getInstance();
        Product productToAdd1 = new Product("Τσίπουρο Χωρίς Γλυκάνισο 200ml","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,0,"τεμάχια");
        Product productToAdd2 = new Product("Αναψυκτικό Coca Cola 1,5lt","Κλασικό αναψυκτικό Coca Cola με ζάχαρη.","Μη αλκοολούχα ποτά","Αναψυκτικά",1.3,0,"τεμάχια");
        Product productToAdd3 = new Product("Νερό Μεταλλικό 1,5lt","Φυσικό μεταλλικό νερό από ελληνικές πηγές.","Μη αλκοολούχα ποτά","Νερό",0.5,0,"τεμάχια");
        database.addNewProduct(productToAdd1);
        database.addNewProduct(productToAdd2);
        database.addNewProduct(productToAdd3);
        assertEquals(3,database.unavailableProducts().size());
    }

}