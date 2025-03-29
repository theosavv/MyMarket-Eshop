package gui;

import api.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * The AdminFrame class extends the UserFrame and represents the graphical user interface (GUI)
 * for administrators in the application. It provides functionality such as viewing
 * product statistics, adding new products, and managing existing products.
 * This class utilizes various Swing components and interacts with the Database
 * to display and manage products.
 */
public class AdminFrame extends UserFrame implements ActionListener {

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

    /**
     * Constructs an AdminFrame for the specified administrator.
     * The frame provides administrative functionalities like viewing statistics
     * and adding products.
     *
     * @param username The username of the administrator.
     */
    AdminFrame(String username) {
        super(username);
        firstButton.setText("Statistics");
        secondButton.setText("Add");
    }

    /**
     * Displays a dialog to gather the number of top products to display,
     * and then displays the statistics for those products.
     */
    private void statistics() {
        String choice = JOptionPane.showInputDialog(this, "Enter how many top products you want to see");
        while (choice != null && !choice.matches("^[1-9]\\d*$")) {
            choice = JOptionPane.showInputDialog(this, "Enter a valid number");
        }
        if (choice == null) return;

        int number = Integer.parseInt(choice);

        productsPanel.removeAll();

        DisplayTopProducts(number);
        DisplayUnavailableProducts();

        productsPanel.revalidate();
        productsPanel.repaint();
    }

    /**
     * Displays the top products that are most frequently purchased.
     *
     * @param number The number of top products to display.
     */
    private void DisplayTopProducts(int number) {
        JPanel panel = createPanel(new Dimension(800, (number * MEDIUM_FONTSIZE * 2) + MEDIUM_FONTSIZE + 10),
                DEFAULT_EMPTYBORDER, null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createLabel("Displaying the top " + number + " most frequently purchased products:",
                MEDIUM_FONTSIZE, new Color(255, 255, 255)));
        panel.add(Box.createVerticalStrut(10));
        int count = 0;
        for (String title : database.frequentlyBoughtProducts(number)) {
            count++;
            panel.add(createLabel(count + ". " + title, MEDIUM_FONTSIZE, new Color(255, 255, 255)));
        }
        panel.add(Box.createVerticalStrut(20));
        productsPanel.add(panel);
    }

    /**
     * Displays a list of all unavailable products (products with zero quantity).
     */
    private void DisplayUnavailableProducts() {
        JPanel panel = createPanel(new Dimension(800, (database.unavailableProducts().size() + 1) * MEDIUM_FONTSIZE * 2),
                DEFAULT_EMPTYBORDER, null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        if (database.unavailableProducts().isEmpty()) {
            panel.add(createLabel("No unavailable products.", MEDIUM_FONTSIZE, new Color(255, 255, 255)));
        } else {
            panel.add(createLabel("Displaying all unavailable products:", MEDIUM_FONTSIZE, new Color(255, 255, 255)));
            panel.add(Box.createVerticalStrut(10));
            for (Product unavailableProduct : database.unavailableProducts()) {
                panel.add(createLabel(unavailableProduct.getProductTitle(), MEDIUM_FONTSIZE, new Color(255, 255, 255)));
            }
        }
        productsPanel.add(panel);
    }

    /**
     * Creates a panel to represent a product and its details.
     * This panel allows administrators to process individual products.
     *
     * @param product The product to display in the panel.
     * @return A JPanel displaying the product details and a processing button.
     */
    @Override
    protected JPanel createProduct(Product product) {
        JPanel mainPanel = createPanel(new Dimension(500, HEIGHT_PRODUCT_PANEL), null, null);
        mainPanel.setLayout(new BorderLayout());

        JPanel productPanel = createPanel(new Dimension(0, 300), DEFAULT_EMPTYBORDER, Color.GRAY);
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

        JPanel addPanel = createPanel(new Dimension(0, 100), new EmptyBorder(25, 50, 20, 50), Color.LIGHT_GRAY);
        addPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JButton productProcessingButton = createButton("product processing", new Dimension(400, 50));

        JLabel title = createLabel(product.getProductTitle(), MEDIUM_FONTSIZE, new Color(30, 30, 30));
        JLabel description = createLabel("Description: " + product.getProductDescription(), SMALL_FONTSIZE, new Color(30, 30, 30));
        JLabel category = createLabel("Category: " + product.getProductCategory(), SMALL_FONTSIZE, new Color(30, 30, 30));
        JLabel subcategory = createLabel("Subcategory: " + product.getProductSubCategory(), SMALL_FONTSIZE, new Color(30, 30, 30));
        JLabel price = createLabel(String.format("Price: %.2f€", product.getProductPrice()).replace('.', ','),
                SMALL_FONTSIZE, new Color(30, 30, 30));
        JLabel availableQuantity = createLabel("Quantity: " + product.getProductQuantity(), SMALL_FONTSIZE, new Color(30, 30, 30));

        productProcessingButton.addActionListener(_ -> {
            new ProductFrame(product, username, true);
            this.setVisible(false);
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
        productPanel.add(availableQuantity);

        addPanel.add(productProcessingButton);

        mainPanel.add(productPanel, BorderLayout.NORTH);
        mainPanel.add(addPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Handles the "Statistics" button functionality by invoking the {@link #statistics()} method.
     */
    @Override
    protected void handleFirstButton() {
        statistics();
    }

    /**
     * Handles the "Add" button functionality, which opens a new ProductFrame for adding a product.
     */
    @Override
    protected void handleSecondButton() {
        new ProductFrame(null, username, false);
        this.setVisible(false);
    }

    /**
     * Handles button events for sign-out, statistics, product addition, search, and navigation.
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