package api;

import org.junit.Test;

import static org.junit.Assert.*;
public class CustomerTest {


    @Test
    public void addProductToCart() {
        Customer customer = new Customer("Sakis","sakis123","Athanasios","Giarlopoylos");
        int initialCartSize = customer.getCart().size();
        double initialCartCost = customer.getTotalCartCost();
        Product productToAdd = new Product("Τσίπουρο Χωρίς Γλυκάνισο 200ml","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,97,"τεμάχια");
        customer.addProductToCart(productToAdd,2);
        assertEquals(initialCartSize+1,customer.getCart().size());
        assertEquals(initialCartCost+13,customer.getTotalCartCost(),0.001);
        assertFalse(customer.addProductToCart(productToAdd, 2));
    }


    @Test
    public void removeProductFromCart() {
        Customer customer = new Customer("Sakis","sakis123","Athanasios","Giarlopoylos");
        Product productToAdd = new Product("Τσίπουρο Χωρίς Γλυκάνισο 200ml","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,97,"τεμάχια");
        customer.addProductToCart(productToAdd,2);
        Product productInCart = customer.getCart().getFirst();
        customer.removeProductFromCart(productInCart);
        assertEquals(0,customer.getCart().size());
    }

    @Test
    public void adjustProductQuantityInCart() {
        Customer customer = new Customer("Sakis","sakis123","Athanasios","Giarlopoylos");
        Product productToAdd = new Product("Τσίπουρο Χωρίς Γλυκάνισο 200ml","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,97,"τεμάχια");
        customer.addProductToCart(productToAdd,2);
        Product productInCart = customer.getCart().getFirst();
        customer.adjustProductQuantityInCart(productInCart,1);
        assertEquals(1,customer.getCart().size());
        assertEquals(1,productInCart.getProductQuantity());
        customer.adjustProductQuantityInCart(productInCart,0);
        assertEquals(0,customer.getCart().size());
        assertEquals(0,productInCart.getProductQuantity());
    }

    @Test
    public void completeOrder() {
        Customer customer = new Customer("Sakis","sakis123","Athanasios","Giarlopoylos");
        Product productToAdd = new Product("Τσίπουρο Χωρίς Γλυκάνισο 200ml","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,97,"τεμάχια");
        customer.addProductToCart(productToAdd,2);
        assertEquals(1,customer.getCart().size());
        customer.completeOrder();
        assertEquals(0,customer.getCart().size());
        assertEquals(0,customer.getTotalCartCost(),0.001);
        assertEquals(1,customer.getCustomerOrderHistory().size());
    }




    @Test
    public void clearCart() {
        Customer customer = new Customer("Sakis","sakis123","Athanasios","Giarlopoylos");
        Product productToAdd = new Product("Τσίπουρο Χωρίς Γλυκάνισο 200ml","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,97,"τεμάχια");
        customer.addProductToCart(productToAdd,2);
        assertEquals(1,customer.getCart().size());
        customer.clearCart();
        assertEquals(0,customer.getCart().size());
    }

    @Test
    public void getMaxProductsBought() {
        Customer customer = new Customer("Sakis","sakis123","Athanasios","Giarlopoylos");
        Product productToAdd1 = new Product("Τσίπουρο Χωρίς Γλυκάνισο 200ml","Αυθεντικό τσίπουρο χωρίς γλυκάνισο.","Αλκοολούχα ποτά","Τσίπουρο",6.5,97,"τεμάχια");
        Product productToAdd2 = new Product("Αναψυκτικό Coca Cola 1,5lt","Κλασικό αναψυκτικό Coca Cola με ζάχαρη.","Μη αλκοολούχα ποτά","Αναψυκτικά",1.3,298,"τεμάχια");
        Product productToAdd3 = new Product("Νερό Μεταλλικό 1,5lt","Φυσικό μεταλλικό νερό από ελληνικές πηγές.","Μη αλκοολούχα ποτά","Νερό",0.5,498,"τεμάχια");
        customer.addProductToCart(productToAdd1,2);
        customer.addProductToCart(productToAdd2,10);
        customer.completeOrder();
        customer.addProductToCart(productToAdd1,1);
        customer.addProductToCart(productToAdd2,6);
        customer.addProductToCart(productToAdd3,1);
        customer.completeOrder();
        assertEquals(3,customer.getMaxProductsBought());
    }

}