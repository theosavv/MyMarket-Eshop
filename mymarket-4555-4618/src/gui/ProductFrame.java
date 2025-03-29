package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The {@code ProductFrame} class provides a graphical user interface (GUI) for processing products.
 * It supports adding new products or editing existing products.
 * The GUI includes input fields for product title, description, category, subcategory, price, quantity,
 * and measurement unit.
 */
public class ProductFrame extends JFrame implements ActionListener {

    // Constants for UI component dimensions
    private static final Dimension PANEL_DIMENSIONS = new Dimension(600, 70);
    private static final Dimension LABEL_DIMENSIONS = new Dimension(150, 50);
    private static final Dimension NORMAL_BUTTON_DIMENSIONS = new Dimension(230, 50);

    // Dropdown menus for categories, subcategories, and measurement units
    private JComboBox<String> dropdownCategories;
    private JComboBox<String> measurementUnitDropdown;
    JComboBox<String> dropdownSubcategories;

    // Text fields for product details
    JTextField titleText, descriptionText, priceText, quantityText;

    // Submit button
    JButton submitButton;

    // Instance of the database for fetching categories, subcategories, and managing products
    Database database = Database.getInstance();

    // Product being processed (for edit mode), username of the admin, and mode flag
    private final Product product;
    private final String username;
    private final boolean mode;

    /**
     * Constructor for the {@code ProductFrame} class.
     * Initializes the GUI for product processing.
     *
     * @param product  The product to be edited, or {@code null} for adding a new product.
     * @param username The username of the admin using the frame.
     * @param mode     The mode of the frame: {@code true} for editing a product, {@code false} for adding a new product.
     */
    public ProductFrame(Product product, String username, boolean mode) {
        this.product = product;
        this.username = username;
        this.mode = mode;

        initializeFrame();

        if (mode) {
            fillProductDetails();
        }
    }

    /**
     * Creates and returns a {@code JPanel} with default layout and size.
     *
     * @return a new panel with {@code FlowLayout} and predefined size.
     */
    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(PANEL_DIMENSIONS);
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        return panel;
    }

    /**
     * Creates and returns a {@code JLabel} with the specified text and custom font properties.
     *
     * @param text      The text to display on the label.
     * @param dimension The preferred size of the label.
     * @return a label with the specified text and size.
     */
    private JLabel createLabel(String text, Dimension dimension) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 25));
        label.setForeground(new Color(30, 30, 30));
        label.setPreferredSize(dimension);
        return label;
    }

    /**
     * Creates and returns a {@code JTextField} for user input.
     *
     * @return a new text field with predefined font and size.
     */
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.BOLD, 30));
        textField.setPreferredSize(new Dimension(300, 50));
        textField.setForeground(new Color(30, 30, 30));
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        return textField;
    }

    /**
     * Initializes the frame properties, layouts, and components.
     */
    private void initializeFrame() {
        this.setTitle("PRODUCT PROCESSING");
        this.setSize(680, 700);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(200, 200, 200));
        this.setLocationRelativeTo(null);
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to exit? Your changes will not be saved.",
                        "Cancel",
                        JOptionPane.OK_CANCEL_OPTION
                );
                if (choice == JOptionPane.OK_OPTION) {
                    new AdminFrame(username);
                    setVisible(false);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        JPanel titlePanel = createPanel();
        JPanel descriptionPanel = createPanel();
        JPanel categorySubcategoryPanel = createPanel();
        JPanel pricePanel = createPanel();
        JPanel quantityPanel = createPanel();
        JPanel measurementPanel = createPanel();
        JPanel submitPanel = createPanel();

        titleText = createTextField();
        descriptionText = createTextField();
        priceText = createTextField();
        quantityText = createTextField();

        titlePanel.add(createLabel("Title", LABEL_DIMENSIONS));
        descriptionPanel.add(createLabel("Description", LABEL_DIMENSIONS));
        pricePanel.add(createLabel("Price", LABEL_DIMENSIONS));
        quantityPanel.add(createLabel("Quantity", LABEL_DIMENSIONS));
        measurementPanel.add(createLabel("Measurement unit", new Dimension(250, 50)));

        // Configure measurement dropdown
        measurementUnitDropdown = new JComboBox<>();
        measurementUnitDropdown.addItem("Select...");
        measurementUnitDropdown.addItem("kg");
        measurementUnitDropdown.addItem("τεμάχια");
        measurementUnitDropdown.setPreferredSize(new Dimension(200, 50));
        measurementUnitDropdown.setBackground(new Color(30, 30, 30));
        measurementUnitDropdown.setForeground(new Color(255, 255, 255));

        // Configure category dropdown
        dropdownCategories = new JComboBox<>();
        dropdownCategories.addItem("Select...");
        for (String category : database.getCategories()) {
            dropdownCategories.addItem(category);
        }
        dropdownCategories.setPreferredSize(NORMAL_BUTTON_DIMENSIONS);
        dropdownCategories.setBackground(new Color(30, 30, 30));
        dropdownCategories.setForeground(new Color(255, 255, 255));
        dropdownCategories.addActionListener(this);

        // Configure subcategory dropdown
        dropdownSubcategories = new JComboBox<>();
        dropdownSubcategories.addItem("Select...");
        dropdownSubcategories.setPreferredSize(NORMAL_BUTTON_DIMENSIONS);
        dropdownSubcategories.setBackground(new Color(30, 30, 30));
        dropdownSubcategories.setForeground(new Color(255, 255, 255));
        dropdownSubcategories.setEnabled(false);

        // Configure submit button
        submitButton = new JButton("Submit");
        submitButton.setPreferredSize(NORMAL_BUTTON_DIMENSIONS);
        submitButton.setForeground(new Color(255, 255, 255));
        submitButton.setBackground(new Color(30, 30, 30));
        submitButton.addActionListener(this);

        titlePanel.add(titleText);
        descriptionPanel.add(descriptionText);
        categorySubcategoryPanel.add(dropdownCategories);
        categorySubcategoryPanel.add(dropdownSubcategories);
        pricePanel.add(priceText);
        quantityPanel.add(quantityText);
        measurementPanel.add(measurementUnitDropdown);
        submitPanel.add(submitButton);

        this.add(Box.createVerticalStrut(5));
        this.add(titlePanel);
        this.add(Box.createVerticalStrut(5));
        this.add(descriptionPanel);
        this.add(Box.createVerticalStrut(5));
        this.add(categorySubcategoryPanel);
        this.add(Box.createVerticalStrut(5));
        this.add(pricePanel);
        this.add(Box.createVerticalStrut(5));
        this.add(quantityPanel);
        this.add(Box.createVerticalStrut(5));
        this.add(measurementPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(submitPanel);
        this.add(Box.createVerticalStrut(10));

        this.setVisible(true);
    }

    /**
     * Fills the input fields with the details of the product being edited.
     * This method is only used in edit mode.
     */
    private void fillProductDetails() {
        titleText.setText(product.getProductTitle());
        descriptionText.setText(product.getProductDescription());
        priceText.setText(String.valueOf(product.getProductPrice()));
        quantityText.setText(String.valueOf(product.getProductQuantity()));
        measurementUnitDropdown.setSelectedItem(product.getProductMeasurementUnit());
        dropdownCategories.setSelectedItem(product.getProductCategory());

        String[] subcategories = database.getSubCategories(product.getProductCategory()).toArray(new String[0]);
        dropdownSubcategories.setModel(new DefaultComboBoxModel<>(subcategories));
        dropdownSubcategories.setSelectedItem(product.getProductSubCategory());
        dropdownSubcategories.setEnabled(true);
    }

    /**
     * Handles the actions performed on the frame's components (e.g., category dropdown selection or submit button click).
     *
     * @param e The {@code ActionEvent} triggered by user interaction.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dropdownCategories) {
            // Update subcategories when a new category is selected
            String selectedCategory = (String) dropdownCategories.getSelectedItem();
            if (selectedCategory != null) {
                dropdownSubcategories.removeAllItems();
                for (String subcategory : database.getSubCategories(selectedCategory)) {
                    dropdownSubcategories.addItem(subcategory);
                }
                dropdownSubcategories.setEnabled(true);
            } else {
                dropdownSubcategories.setEnabled(false);
            }
        }

        if (e.getSource() == submitButton) {
            // Handle form submission
            if (titleText.getText().isEmpty() || descriptionText.getText().isEmpty() || priceText.getText().isEmpty()
                    || quantityText.getText().isEmpty() || dropdownCategories.getSelectedIndex() == 0
                    || dropdownSubcategories.getSelectedIndex() == 0 || measurementUnitDropdown.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "All fields are mandatory!", "EMPTY FIELDS", JOptionPane.WARNING_MESSAGE);
            } else {
                boolean errorPrice = false;
                boolean errorQuantity = false;
                boolean errorTitle = false;

                if (!priceText.getText().matches("^[1-9]\\d*(\\.\\d{0,2})?$") && !priceText.getText().matches("^0\\.(?!0$|00$)\\d{1,2}$")) {
                    priceText.setText("");
                    errorPrice = true;
                }

                if (!quantityText.getText().matches("^[1-9]\\d*")) {
                    quantityText.setText("");
                    errorQuantity = true;
                }

                if (!mode) {
                    for (Product checkProduct : database.getAllProducts()) {
                        if (checkProduct.getProductTitle().trim().equals(titleText.getText().trim())) {
                            titleText.setText("");
                            errorTitle = true;
                            break;
                        }
                    }
                }

                if (errorPrice && errorQuantity) {
                    JOptionPane.showMessageDialog(this, "Enter a valid price and quantity!", "EMPTY FIELDS", JOptionPane.WARNING_MESSAGE);
                } else if (errorPrice) {
                    JOptionPane.showMessageDialog(this, "Enter a valid price", "EMPTY FIELDS", JOptionPane.WARNING_MESSAGE);
                } else if (errorQuantity) {
                    JOptionPane.showMessageDialog(this, "Enter a valid quantity!", "EMPTY FIELDS", JOptionPane.WARNING_MESSAGE);
                } else if (errorTitle) {
                    JOptionPane.showMessageDialog(this, "Product with the same title already exists", "EMPTY FIELDS", JOptionPane.WARNING_MESSAGE);
                } else {
                    if (mode) {
                        product.setProductTitle(titleText.getText());
                        product.setProductDescription(descriptionText.getText());
                        product.setProductCategory((String) dropdownCategories.getSelectedItem());
                        product.setProductSubCategory((String) dropdownSubcategories.getSelectedItem());
                        product.setProductPrice(Double.parseDouble(priceText.getText()));
                        product.setProductQuantity(Integer.parseInt(quantityText.getText()));
                        product.setProductMeasurementUnit((String) measurementUnitDropdown.getSelectedItem());

                        JOptionPane.showMessageDialog(this, "Product processed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        database.addNewProduct(new Product(
                                titleText.getText(),
                                descriptionText.getText(),
                                (String) dropdownCategories.getSelectedItem(),
                                (String) dropdownSubcategories.getSelectedItem(),
                                Double.parseDouble(priceText.getText()),
                                Integer.parseInt(quantityText.getText()),
                                (String) measurementUnitDropdown.getSelectedItem()));
                        JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }

                    this.setVisible(false);
                    new AdminFrame(username);
                }
            }
        }
    }
}