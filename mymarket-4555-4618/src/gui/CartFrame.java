package gui;

import api.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * The CartFrame class represents the user interface for a customer's shopping cart.
 * It allows the customer to view the items in their cart, adjust item quantities, remove products,
 * and proceed to checkout.
 * This frame interacts with the customer object to display and modify the contents of the cart.
 * It includes functionality to submit an order, clear the cart, and navigate to other parts of the application.
 */
public class CartFrame extends JFrame implements ActionListener {

    /**
     * Constant for the height of the product panel in the cart.
     */
    private static final int HEIGHT_PRODUCT_PANEL = 170;

    /**
     * Constant for the font size of small text labels.
     */
    private static final int SMALL_FONTSIZE = 15;

    /**
     * Constant for the font size of medium text labels.
     */
    private static final int MEDIUM_FONTSIZE = 25;

    /**
     * Constant for the font size of large text labels.
     */
    private static final int LARGE_FONTSIZE = 30;

    /**
     * Constant for the default size of buttons.
     */
    private static final Dimension NORMAL_BUTTON_DIMENSIONS = new Dimension(180,50);

    /**
     * Constant for the size of small buttons used in the cart.
     */
    private static final Dimension SMALL_BUTTON_DIMENSIONS = new Dimension(50,50);

    /**
     * Default empty border for UI components.
     */
    private static final EmptyBorder DEFAULT_EMPTYBORDER = new EmptyBorder(10,10,10,10);

    // UI components
    private JPanel northPanel;
    private JPanel centerPanel;
    private JPanel productsPanel;
    private JPanel cartProductPanel;
    private JPanel checkoutCenterPanel;
    private JButton backButton, ordersButton, clearCartButton, submitOrderButton;
    private JLabel emptyCartLabel;
    private JScrollPane scrollPane;
    private HashMap<String, JPanel> cartProductPanels;

    // Database and customer
    Database database = Database.getInstance();
    private final Customer customer;

    /**
     * Constructor that initializes the CartFrame for the given customer.
     *
     * @param customer The customer whose cart is being displayed.
     */
    public CartFrame(Customer customer) {
        this.customer = customer;

        initializeFrame();
        initializeMenu();
        initializeCenter();

        this.add(northPanel, BorderLayout.NORTH);
        this.add(scrollPane);

        this.setVisible(true);
    }

    /**
     * Initializes the JFrame settings.
     */
    private void initializeFrame() {
        this.setTitle(customer.getFirstName() + "'s cart");
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Handle window closing with a confirmation dialog
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the application?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (choice == JOptionPane.YES_OPTION) {
                    System.out.println("Application is closing...");
                    database.allWritersCall();
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    /**
     * Initializes the menu bar at the top of the cart frame.
     */
    private void initializeMenu() {
        northPanel = createPanel(new Dimension(0, 100), DEFAULT_EMPTYBORDER, null);
        northPanel.setLayout(new BorderLayout());

        JPanel northRightPanel = createPanel(new Dimension(570, 0), DEFAULT_EMPTYBORDER, null);
        northRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 10));

        JPanel northCenterPanel = createPanel(new Dimension(570, 0), DEFAULT_EMPTYBORDER, null);
        northCenterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));

        JPanel northLeftPanel = createPanel(new Dimension(250, 0), DEFAULT_EMPTYBORDER, null);
        northLeftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JLabel nameOfMarket = createLabel("MyMarket", LARGE_FONTSIZE, true);

        northLeftPanel.add(nameOfMarket);

        northCenterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));  // Center alignment
        JLabel cartLabel = createLabel("This is " + customer.getFirstName() + "'s cart", LARGE_FONTSIZE, true);
        cartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        northCenterPanel.add(cartLabel);

        backButton = createButton("Back", NORMAL_BUTTON_DIMENSIONS, MEDIUM_FONTSIZE);
        northRightPanel.add(backButton);
        clearCartButton = createButton("Empty Cart", NORMAL_BUTTON_DIMENSIONS, MEDIUM_FONTSIZE);
        northRightPanel.add(clearCartButton);
        ordersButton = createButton("Orders", NORMAL_BUTTON_DIMENSIONS, MEDIUM_FONTSIZE);
        northRightPanel.add(ordersButton);

        northPanel.add(northRightPanel, BorderLayout.EAST);
        northPanel.add(northCenterPanel, BorderLayout.CENTER);
        northPanel.add(northLeftPanel, BorderLayout.WEST);
    }

    /**
     * Initializes the main content area of the cart, including the product list and checkout section.
     */
    private void initializeCenter() {
        centerPanel = createPanel(new Dimension(0, getHeightCart()), DEFAULT_EMPTYBORDER, new Color(30,30,30));
        centerPanel.setLayout(new BorderLayout());

        productsPanel = createPanel(new Dimension(0, getHeightCart()), null, new Color(30,30,30));
        productsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));

        cartProductPanels = new HashMap<>();

        // Add each product in the cart
        for (Product product : customer.getCart()) {
            cartProductPanel = createCartProduct(product);
            cartProductPanels.put(product.getProductTitle(), cartProductPanel);
            productsPanel.add(cartProductPanel);
        }

        emptyCartLabel = createLabel("Your cart is empty!", 50, true);
        emptyCartLabel.setBorder(DEFAULT_EMPTYBORDER);
        emptyCartLabel.setForeground(new Color(255, 255, 255));
        emptyCartLabel.setVisible(false);

        productsPanel.add(emptyCartLabel);

        if (cartProductPanels.isEmpty()) {
            emptyCartLabel.setVisible(true);
        }

        JPanel checkoutPanel = createPanel(new Dimension(380, getHeightCart()), DEFAULT_EMPTYBORDER, Color.GRAY);
        checkoutPanel.setLayout(new BorderLayout());

        JLabel myCheckout = createLabel("My checkout", LARGE_FONTSIZE, true);
        myCheckout.setHorizontalAlignment(SwingConstants.CENTER);
        myCheckout.setBorder(new EmptyBorder(0, 0, 10, 0));
        myCheckout.setBackground(Color.green);

        checkoutCenterPanel = createPanel(new Dimension(450, 0), DEFAULT_EMPTYBORDER, Color.LIGHT_GRAY);
        checkoutCenterPanel.setLayout(new BoxLayout(checkoutCenterPanel, BoxLayout.Y_AXIS));

        // Add product details and total cost to check out panel
        for (Product product : customer.getCart()) {
            JLabel productTitle = createLabel((product.getProductTitle() + ": " + product.getProductQuantity()), SMALL_FONTSIZE, true);
            JLabel productPrice = createLabel(String.format("Price: %.2f€", product.getProductPrice() * product.getProductQuantity()), SMALL_FONTSIZE, false);
            checkoutCenterPanel.add(productTitle);
            checkoutCenterPanel.add(productPrice);
        }

        checkoutCenterPanel.add(Box.createVerticalStrut(25));
        checkoutCenterPanel.add(createLabel(String.format("Total cost: %.2f€", customer.getTotalCartCost()), MEDIUM_FONTSIZE, true));

        submitOrderButton = createButton("Submit", NORMAL_BUTTON_DIMENSIONS, MEDIUM_FONTSIZE);

        checkoutCenterPanel.add(Box.createVerticalStrut(MEDIUM_FONTSIZE));
        checkoutCenterPanel.add(submitOrderButton);

        checkoutPanel.add(myCheckout, BorderLayout.NORTH);
        checkoutPanel.add(checkoutCenterPanel, BorderLayout.CENTER);

        centerPanel.add(productsPanel, BorderLayout.CENTER);
        centerPanel.add(checkoutPanel, BorderLayout.EAST);

        scrollPane = new JScrollPane(centerPanel);
    }

    /**
     * Creates a JPanel with the specified dimensions, border, and background color.
     *
     * @param dimensions The preferred dimensions of the panel.
     * @param emptyBorder The border to set for the panel.
     * @param color The background color of the panel.
     * @return The created JPanel.
     */
    private JPanel createPanel(Dimension dimensions, EmptyBorder emptyBorder, Color color) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(dimensions);
        panel.setBorder(emptyBorder);
        panel.setBackground(color);
        return panel;
    }

    /**
     * Creates a JButton with the specified text, dimensions, and font size.
     * @param text The text to display on the button.
     * @param dimensions The dimensions of the button.
     * @param fontSize The font size of the text.
     * @return The created JButton.
     */
    private JButton createButton(String text, Dimension dimensions, int fontSize) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(dimensions));
        button.setForeground(new Color(255,255,255));
        button.setBackground(new Color(30,30,30));
        button.setFocusable(false);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.addActionListener(this);
        return button;
    }

    /**
     * Creates a JLabel with the specified text, font size, and centered alignment.
     *
     * @param text The text to display on the label.
     * @param fontSize The font size of the text.
     * @param isBold Whether the text should be bold.
     * @return The created JLabel.
     */
    private JLabel createLabel(String text, int fontSize, boolean isBold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    /**
     * Creates a panel to display a product in the cart.
     *
     * @param product The product to display.
     * @return The JPanel representing the product.
     */
    private JPanel createCartProduct(Product product) {
        JPanel mainPanel = createPanel(new Dimension(1100, 160), DEFAULT_EMPTYBORDER, null);
        mainPanel.setLayout(new BorderLayout());

        JPanel productInfoPanel = createPanel(new Dimension(700, 100), DEFAULT_EMPTYBORDER, Color.GRAY);
        productInfoPanel.setLayout(new BoxLayout(productInfoPanel, BoxLayout.Y_AXIS));

        JButton removeButton = createButton("Remove", new Dimension(100,50), SMALL_FONTSIZE);
        removeButton.addActionListener(_ -> updateFrame(product, true));


        JPanel  adjustQuantityPanel = createPanel(new Dimension(200,100), new EmptyBorder(15,0,0,0), Color.LIGHT_GRAY);
        adjustQuantityPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));

        JButton minusButton = createButton("-", SMALL_BUTTON_DIMENSIONS, MEDIUM_FONTSIZE);
        JTextField quantityText = new JTextField(Integer.toString(product.getProductQuantity()));
        quantityText.setFont(new Font("Arial", Font.BOLD, 25));
        quantityText.setPreferredSize(new Dimension(80,50));
        quantityText.setHorizontalAlignment(SwingConstants.CENTER);
        JButton plusButton = createButton("+", SMALL_BUTTON_DIMENSIONS, MEDIUM_FONTSIZE);
        JButton adjustQuantityButton = createButton("adjust quantity", NORMAL_BUTTON_DIMENSIONS, SMALL_FONTSIZE);

        quantityText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && !Character.isISOControl(c)) {
                    e.consume();
                }
            }
        });

        minusButton.addActionListener(_ -> {
            if (!quantityText.getText().isEmpty()) {
                int quantity = Integer.parseInt(quantityText.getText());
                if (quantity > 0) {
                    quantityText.setText(Integer.toString(--quantity));
                }
            }
        });

        plusButton.addActionListener(_ -> {
            if (quantityText.getText().isEmpty()) {
                quantityText.setText("1");
            } else {
                int quantity = Integer.parseInt(quantityText.getText());
                quantityText.setText(Integer.toString(++quantity));
            }
        });

        JLabel message = createLabel("Unavailable quantity", SMALL_FONTSIZE, false);
        message.setForeground(Color.red);
        message.setVisible(false);

        adjustQuantityButton.addActionListener(_ -> {
            String text = quantityText.getText().trim();
            if (!text.isEmpty() && text.matches("\\d+")) {
                int quantity = Integer.parseInt(text);
                if (quantity > 0) {
                    boolean isAdded = customer.adjustProductQuantityInCart(product, quantity);
                    if (isAdded) {
                        updateFrame(product, false);
                    } else {
                        message.setText("Unavailable quantity");
                        message.setVisible(true);
                    }
                } else {
                    updateFrame(product, true);
                }
            } else {
                message.setText("Invalid input, enter a number.");
                message.setVisible(true);
            }
        });

        adjustQuantityPanel.add(minusButton);
        adjustQuantityPanel.add(quantityText);
        adjustQuantityPanel.add(plusButton);
        adjustQuantityPanel.add(adjustQuantityButton);
        adjustQuantityPanel.add(message);

        productInfoPanel.add(createLabel(product.getProductTitle(), MEDIUM_FONTSIZE, true));
        productInfoPanel.add(createLabel("Description: " + product.getProductDescription(), SMALL_FONTSIZE, true));
        productInfoPanel.add(createLabel("Category: " + product.getProductCategory(), SMALL_FONTSIZE, true));
        productInfoPanel.add(createLabel("Subcategory: " + product.getProductSubCategory(), SMALL_FONTSIZE, true));
        productInfoPanel.add(createLabel(String.format("Price: %.2f€", product.getProductPrice()).replace('.', ','), SMALL_FONTSIZE, true));
        productInfoPanel.add(createLabel("Quantity: " + product.getProductQuantity(), SMALL_FONTSIZE, true));

        mainPanel.add(productInfoPanel, BorderLayout.WEST);
        mainPanel.add(adjustQuantityPanel, BorderLayout.CENTER);
        mainPanel.add(removeButton, BorderLayout.EAST);

        return mainPanel;
    }

    /**
     * Updates the cart display by re-rendering the product list and recalculating the total cost.
     * This method is invoked after a product is removed or its quantity is adjusted.
     * It also handles the visibility of a message when the cart is empty.
     *
     * @param product The product to update in the cart.
     * @param removeProduct Whether the product should be removed from the cart.
     */
    private void updateFrame(Product product, boolean removeProduct) {
        checkoutCenterPanel.removeAll();
        productsPanel.removeAll();

        if (removeProduct && product != null) {
            customer.removeProductFromCart(product);
        }

        for (Product cartProduct : customer.getCart()) {
            cartProductPanel = createCartProduct(cartProduct);
            cartProductPanels.put(cartProduct.getProductTitle(), cartProductPanel);
            productsPanel.add(cartProductPanel);

            JLabel productTitle = createLabel(cartProduct.getProductTitle() + ": " + cartProduct.getProductQuantity(), SMALL_FONTSIZE, true);
            JLabel productPrice = createLabel(String.format("Price: %.2f€", cartProduct.getProductPrice() * cartProduct.getProductQuantity()), SMALL_FONTSIZE, false);

            checkoutCenterPanel.add(productTitle);
            checkoutCenterPanel.add(productPrice);
        }

        checkoutCenterPanel.add(Box.createVerticalStrut(25));
        checkoutCenterPanel.add(createLabel(String.format("Total cost: %.2f€", customer.getTotalCartCost()), MEDIUM_FONTSIZE, true));
        checkoutCenterPanel.add(Box.createVerticalStrut(25));
        checkoutCenterPanel.add(submitOrderButton);

        emptyCartLabel.setVisible(customer.getCart().isEmpty());
        if (customer.getCart().isEmpty()) {
            productsPanel.add(emptyCartLabel);
        }

        productsPanel.setPreferredSize(new Dimension(0, getHeightCart()));
        centerPanel.setPreferredSize(new Dimension(0, getHeightCart()));

        productsPanel.revalidate();
        productsPanel.repaint();
        checkoutCenterPanel.revalidate();
        checkoutCenterPanel.repaint();
    }

    /**
     * Retrieves the total height of the cart section, used for adjusting the layout based on the number of products.
     *
     * @return The height of the cart section.
     */
    private int getHeightCart() {
        int height;
        if (customer.getCart().size() <= 9) {
            height = (9 * HEIGHT_PRODUCT_PANEL) + 40;
        } else {
            height = (customer.getCart().size() * HEIGHT_PRODUCT_PANEL) + 40;
        }
        return height;
    }

    /**
     * Handles various actions in the frame such as navigating to the customer frame, viewing orders, clearing the cart,
     * and submitting an order.
     *
     * @param e The action event triggered by a user action (e.g., button click).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            new CustomerFrame(customer.getUsername());
            setVisible(false);
        }

        if (e.getSource() == ordersButton) {
            new OrdersFrame(customer);
            setVisible(false);
        }

        if (e.getSource() == clearCartButton) {
            if (customer.getCart() == null || customer.getCart().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is already empty!");
            } else {
                customer.clearCart();
                updateFrame(null, false);
            }
        }

        if (e.getSource() == submitOrderButton) {
            if (customer.getCart().isEmpty() || customer.getCart() == null) {
                JOptionPane.showMessageDialog(this, "Can't proceed to payment if cart is empty!");
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to proceed with your order?", "Submit order", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    customer.completeOrder();
                    new CustomerFrame(customer.getUsername());
                } else if (option == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(this, "Order Canceled.");
                }
            }
        }
    }
}