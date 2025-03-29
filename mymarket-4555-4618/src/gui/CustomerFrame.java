package gui;

import api.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * The CustomerFrame class extends the UserFrame and represents the graphical user interface (GUI)
 * for customers in the application. It provides functionality such as viewing and managing
 * orders, adding products to the cart, and browsing available products.
 * This class interacts with the Database and the Customer object to manage product purchases
 * and orders.
 */
public class CustomerFrame extends UserFrame implements ActionListener {

    /** Height of the product panel displayed in the frame. */
    private static final int HEIGHT_PRODUCT_PANEL = 400;

    /** Font size for small text. */
    private static final int SMALL_FONTSIZE = 15;

    /** Font size for medium text. */
    private static final int MEDIUM_FONTSIZE = 25;

    /** Default padding used for panels. */
    private static final EmptyBorder DEFAULT_EMPTYBORDER = new EmptyBorder(10, 10, 10, 10);

    /** Singleton instance of the Database for accessing and managing product data. */
    Database database = Database.getInstance();

    /** Customer object representing the logged-in customer. */
    private final Customer customer;

    /**
     * Constructs a CustomerFrame for the specified customer. The frame allows the customer to
     * view their orders, manage their cart, and browse products.
     *
     * @param username The username of the customer.
     */
    CustomerFrame(String username) {
        super(username);
        this.customer = database.getAllCustomers().get(username);
        firstButton.setText("Orders");
        secondButton.setText("Cart");
    }

    /**
     * Creates a panel to represent a product and its details. This panel allows customers
     * to add products to their cart.
     *
     * @param product The product to display in the panel.
     * @return A JPanel displaying the product details and an "Add product" button.
     */
    @Override
    protected JPanel createProduct(Product product) {
        JPanel mainPanel = createPanel(new Dimension(500, HEIGHT_PRODUCT_PANEL), null, null);
        mainPanel.setLayout(new BorderLayout());

        JPanel productPanel = createPanel(new Dimension(0, 300), DEFAULT_EMPTYBORDER, Color.GRAY);
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

        JPanel addPanel = createPanel(new Dimension(0, 100), new EmptyBorder(25, 50, 20, 50), Color.LIGHT_GRAY);
        addPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JButton addButton = createButton("Add product", new Dimension(400, 50));

        JLabel title = createLabel(product.getProductTitle(), MEDIUM_FONTSIZE, new Color(30, 30, 30));
        JLabel description = createLabel("Description: " + product.getProductDescription(), SMALL_FONTSIZE, new Color(30, 30, 30));
        JLabel category = createLabel("Category: " + product.getProductCategory(), SMALL_FONTSIZE, new Color(30, 30, 30));
        JLabel subcategory = createLabel("Subcategory: " + product.getProductSubCategory(), SMALL_FONTSIZE, new Color(30, 30, 30));
        JLabel price = createLabel(String.format("Price: %.2f€", product.getProductPrice()).replace('.', ','), SMALL_FONTSIZE, new Color(30, 30, 30));

        JLabel message = createLabel("Product added successfully", SMALL_FONTSIZE, new Color(0, 255, 0));
        message.setVisible(false);

        addButton.addActionListener(_ -> {
            if (customer.addProductToCart(product, 1)) {
                message.setVisible(true);
                Timer timer = new Timer(2000, _ -> message.setVisible(false));
                timer.setRepeats(false);
                timer.start();
            } else {
                JOptionPane.showMessageDialog(this, "Product already in the cart, adjust the quantity there!");
            }
        });

        productPanel.add(title);
        productPanel.add(Box.createVerticalStrut(10));
        productPanel.add(description);
        productPanel.add(Box.createVerticalStrut(10));
        productPanel.add(category);
        productPanel.add(Box.createVerticalStrut(5));
        productPanel.add(subcategory);
        productPanel.add(Box.createVerticalStrut(10));
        productPanel.add(price);
        productPanel.add(Box.createVerticalStrut(5));

        addPanel.add(addButton);
        addPanel.add(message);

        mainPanel.add(productPanel, BorderLayout.NORTH);
        mainPanel.add(addPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Handles the "Orders" button functionality by opening the OrdersFrame.
     */
    @Override
    protected void handleFirstButton() {
        new OrdersFrame(customer);
        setVisible(false);
    }

    /**
     * Handles the "Cart" button functionality by opening the CartFrame.
     */
    @Override
    protected void handleSecondButton() {
        new CartFrame(customer);
        setVisible(false);
    }

    /**
     * Handles various button actions, including sign-out, viewing orders, managing the cart,
     * searching for products, and applying filters.
     *
     * @param e The ActionEvent triggered by button interactions.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signOutButton) {
            new LoginFrame();
            setVisible(false);
        }

        if (e.getSource() == firstButton) {
            handleFirstButton();
        }

        if (e.getSource() == secondButton) {
            handleSecondButton();
        }

        if (e.getSource() == submitSearchButton) {
            search();
        }

        if (e.getSource() == backToCategoriesButton) {
            appliedFilters(null);
        }

        if (e.getSource() instanceof JRadioButton selectedRadioButton) {
            appliedFilters(selectedRadioButton);
        }

        if (e.getSource() instanceof JCheckBox _) {
            applyCheckboxFilters();
        }
    }
}