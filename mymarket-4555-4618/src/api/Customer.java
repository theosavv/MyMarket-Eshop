package api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Represents a customer in the system, managing personal information,
 * a shopping cart, and an order history. The class provides functionality
 * to interact with a product database, modify the cart, and complete orders.
 */
public class Customer {
    private final String username;
    private final String password;
    private final String firstName;
    private final String surname;

    public ArrayList<Product> cart;
    private double totalCartCost;

    private ArrayList<Order> customerOrderHistory;

    /**
     * Constructs a Customer instance with the specified personal information.
     * Initializes the customer's order history and active cart by reading
     * data from corresponding text files.
     *
     * @param username the username of the customer.
     * @param password the password of the customer.
     * @param firstName the first name of the customer.
     * @param surname the surname of the customer.
     */
    public Customer(String username, String password, String firstName, String surname) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;

        CustomerOrderHistoryReader myHistoryReader = new CustomerOrderHistoryReader("src/api/textFiles/CustomersOrderHistory/" + username + ".txt");
        customerOrderHistory = myHistoryReader.getOrders();

        CustomerCartReader myReader = new CustomerCartReader("src/api/textFiles/CustomersActiveCarts/" + username + "_activeCart" + ".txt");
        totalCartCost = myReader.getTotalCartCost();
        this.setCart(myReader.getProducts());
    }

    /**
     * Retrieves the username of the customer.
     *
     * @return the username of the customer.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password of the customer.
     *
     * @return the password of the customer.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the first name of the customer.
     *
     * @return the first name of the customer.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the surname of the customer.
     *
     * @return the surname of the customer.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Adds a product to the customer's cart if it is not already present
     * and if the desired quantity is valid.
     *
     * @param p the product to add to the cart.
     * @param neededQuantity the quantity of the product to add.
     * @return true if the product was successfully added; false otherwise.
     */
    public boolean addProductToCart(Product p, int neededQuantity) {

        Product databaseProduct = Database.getInstance().getSpecificProduct(p);
        for(Product product : cart) {
            if(product.getProductTitle().equals(p.getProductTitle())) {
                return false;
            }
        }

        if (neededQuantity > databaseProduct.getProductQuantity() || neededQuantity <= 0) {
            return false;
        } else {
            Product newCartProduct = p.copy();
            newCartProduct.setProductQuantity(neededQuantity);
            cart.add(newCartProduct);
            totalCartCost += newCartProduct.getProductPrice() * neededQuantity;
            return true;
        }
    }

    /**
     * Removes a product from the customer's cart.
     *
     * @param p the product to remove from the cart.
     */
    public void removeProductFromCart(Product p) {
        totalCartCost -= p.getProductQuantity() * p.getProductPrice();
        cart.remove(p);
    }

    /**
     * Adjusts the quantity of a product in the customer's cart.
     *
     * @param p the product to adjust in the cart.
     * @param neededQuantity the new quantity for the product.
     * @return true if the quantity was successfully adjusted; false otherwise.
     */
    public boolean adjustProductQuantityInCart(Product p, int neededQuantity) {
        Product databaseProduct = Database.getInstance().getSpecificProduct(p);

        if (neededQuantity < 0)
            return false;
        else if (neededQuantity > databaseProduct.getProductQuantity()) {
            return false;
        } else if (neededQuantity > p.getProductQuantity()) {
            totalCartCost += (neededQuantity - p.getProductQuantity()) * p.getProductPrice();
            p.setProductQuantity(neededQuantity);
            return true;

        } else if (neededQuantity < p.getProductQuantity() && neededQuantity != 0) {
            totalCartCost -= (p.getProductQuantity() - neededQuantity) * p.getProductPrice();
            p.setProductQuantity(neededQuantity);
            return true;
        } else if (neededQuantity == p.getProductQuantity()) {
            return true;
        } else if (neededQuantity == 0) {
            this.removeProductFromCart(p);
            p.setProductQuantity(0);
            return true;
        }
        return false;
    }

    /**
     * Completes the current order by finalizing the cart contents,
     * updating the order history, and reducing product quantities in the database.
     * Clears the cart and resets the total cart cost to zero.
     */
    public void completeOrder() {

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = date.format(myFormatObj);
        ArrayList<String> products = new ArrayList<>();
        for (Product p : cart) {
            products.add(p.getProductTitle());
        }
        Order order = new Order("Εκκρεμής",formattedDate, products, Double.toString(totalCartCost).replace('.',',')+"€");
        customerOrderHistory.add(order);
        Database database = Database.getInstance();
        for (Product p : cart) {
            Product databaseProduct = database.getSpecificProduct(p);
            databaseProduct.setProductQuantity(databaseProduct.getProductQuantity() - p.getProductQuantity());
        }
        clearCart();
        totalCartCost = 0;
    }

    /**
     * Retrieves the total cost of the customer's cart.
     *
     * @return the total cost of the cart.
     */
    public double getTotalCartCost() {
        return Math.round(totalCartCost * 100.0) / 100.0;
    }


    /**
     * Retrieves the list of products in the customer's cart.
     *
     * @return an ArrayList of products in the cart.
     */
    public ArrayList<Product> getCart() {
        return cart;
    }

    /**
     * Sets the customer's cart with the specified list of products.
     *
     * @param cart an ArrayList of products to set as the cart.
     */
    public void setCart(ArrayList<Product> cart) {

            this.cart = cart;
    }

    /**
     * Retrieves the customer's order history.
     *
     * @return an ArrayList of orders representing the customer's order history.
     */
    public ArrayList<Order> getCustomerOrderHistory() {
        return customerOrderHistory;
    }

    /**
     * Sets the customer's order history with the specified list of orders.
     *
     * @param customerOrderHistory an ArrayList of orders to set as the order history.
     */
    public void setCustomerOrderHistory(ArrayList<Order> customerOrderHistory) {
        if (customerOrderHistory != null) {
            this.customerOrderHistory = customerOrderHistory;
        }
    }

    /**
     * Sets the total cost of the customer's cart.
     *
     * @param totalCartCost the new total cost of the cart.
     */
    public void setTotalCartCost(double totalCartCost) {
        this.totalCartCost = totalCartCost;
    }

    /**
     * Clears the customer's cart and resets the total cart cost to zero.
     */
    public void clearCart() {
        if (!cart.isEmpty())
            cart.clear();
        totalCartCost = 0;

    }


    /**
     * Finds the maximum number of products bought in a single order from the customer's order history.
     *
     * @return the maximum number of products bought in any order.
     */
    public int getMaxProductsBought() {
        int max = 0;
        for (Order order : customerOrderHistory) {
            if (max < order.boughtProducts().size())
                max = order.boughtProducts().size();
        }
        return max;
    }
}

