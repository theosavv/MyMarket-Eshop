package gui;

import api.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The OrdersFrame class represents a graphical user interface (GUI) window
 * that displays a customer's order history.
 * It provides functionalities such as viewing past orders, navigating back,
 * accessing the home screen, and viewing the cart.
 * This class extends JFrame and implements ActionListener to handle button events.
 */
public class OrdersFrame extends JFrame implements ActionListener {

    /** Font size constants for consistent text styling across the frame. */
    private static final int SMALL_FONTSIZE = 15;
    private static final int MEDIUM_FONTSIZE = 25;
    private static final int LARGE_FONTSIZE = 30;

    /** Default dimensions for buttons in the frame. */
    private static final Dimension NORMAL_BUTTON_DIMENSIONS = new Dimension(180, 50);

    /** Default padding for panels. */
    private static final EmptyBorder DEFAULT_EMPTYBORDER = new EmptyBorder(10, 10, 10, 10);

    /** Panel that contains the top navigation menu. */
    JPanel northPanel;

    /** Right section of the top navigation menu. */
    JPanel northRightPanel;

    /** Center section of the top navigation menu. */
    JPanel northCenterPanel;

    /** Left section of the top navigation menu. */
    JPanel northLeftPanel;

    /** Panel containing the main content of the frame. */
    JPanel centerPanel;

    /** Buttons for navigating back, home, and accessing the cart. */
    JButton backButton, cartButton, homeButton;

    /** Label displaying the market name. */
    JLabel nameOfMarket;

    /** Label showing the title of the frame with the customer's name. */
    JLabel cartLabel;

    /** Height of the main center panel, dynamically calculated based on the order history. */
    private int height;

    /** Scroll pane for displaying order history with vertical scrolling enabled. */
    JScrollPane scrollPane;

    /** Reference to the singleton Database instance for accessing order data. */
    Database database = Database.getInstance();

    /** The customer whose order history is displayed in the frame. */
    private final Customer customer;

    /**
     * Constructs an OrdersFrame instance for the specified customer.
     *
     * @param customer The customer whose order history is displayed.
     */
    public OrdersFrame(Customer customer) {
        this.customer = customer;

        initializeFrame();
        initializeMenu();
        initializeCenter();

        this.add(northPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    /**
     * Initializes the frame's properties, such as title, size, layout, and behavior on close.
     */
    private void initializeFrame() {
        this.setTitle(customer.getFirstName() + "'s history orders");
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Adds a confirmation dialog when closing the application.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to close the application?",
                        "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

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
     * Initializes the top navigation menu with buttons for back, home, and cart.
     */
    private void initializeMenu() {
        northPanel = createPanel(new Dimension(0, 100), null);
        northPanel.setLayout(new BorderLayout());

        northRightPanel = createPanel(new Dimension(570, 0), null);
        northRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 10));

        northCenterPanel = createPanel(new Dimension(570, 0), null);
        northCenterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));

        northLeftPanel = createPanel(new Dimension(250, 0), null);
        northLeftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        nameOfMarket = createLabel("MyMarket", LARGE_FONTSIZE);
        northLeftPanel.add(nameOfMarket);

        cartLabel = createLabel("This is " + customer.getFirstName() + "'s history orders", LARGE_FONTSIZE);
        cartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        northCenterPanel.add(cartLabel);

        backButton = createButton("Back");
        northRightPanel.add(backButton);

        homeButton = createButton("Home");
        northRightPanel.add(homeButton);

        cartButton = createButton("Cart");
        northRightPanel.add(cartButton);

        northPanel.add(northRightPanel, BorderLayout.EAST);
        northPanel.add(northCenterPanel, BorderLayout.CENTER);
        northPanel.add(northLeftPanel, BorderLayout.WEST);
    }

    /**
     * Initializes the center panel to display the customer's order history.
     */
    private void initializeCenter() {
        centerPanel = createPanel(new Dimension(0, getPanelHeight()), new Color(30, 30, 30));
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        height = getOrderHeight();
        if (customer.getCustomerOrderHistory().isEmpty()) {
            JLabel emptyHistory = createLabel("No orders have been placed yet.", LARGE_FONTSIZE);
            emptyHistory.setForeground(Color.WHITE);
            centerPanel.add(emptyHistory);
        } else {
            for (Order order : customer.getCustomerOrderHistory()) {
                centerPanel.add(createOrder(order));
            }
        }

        scrollPane = new JScrollPane(centerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * Creates a JPanel to represent a single order.
     *
     * @param order The order to be displayed.
     * @return A JPanel containing the order's details.
     */
    private JPanel createOrder(Order order) {
        JPanel mainPanel = createPanel(new Dimension(490, height), Color.GRAY);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel orderDate = createPanel(new Dimension(500, 40), Color.LIGHT_GRAY);
        JPanel totalCost = createPanel(new Dimension(500, 40), Color.LIGHT_GRAY);

        orderDate.setLayout(new FlowLayout(FlowLayout.LEFT));
        totalCost.setLayout(new FlowLayout(FlowLayout.LEFT));

        orderDate.add(createLabel("Date: " + order.orderDate(), SMALL_FONTSIZE));
        totalCost.add(createLabel("Total cost: " + order.totalOrderCost(), SMALL_FONTSIZE));
        JPanel boughtProducts = createPanel(new Dimension(500, height - 100), Color.LIGHT_GRAY);
        boughtProducts.setLayout(new FlowLayout(FlowLayout.LEFT));

        boughtProducts.add(createLabel("Bought Products: ", SMALL_FONTSIZE));
        for (String product : order.boughtProducts()) {
            boughtProducts.add(createLabel(" " + product + " |", SMALL_FONTSIZE));
        }

        mainPanel.add(orderDate);
        mainPanel.add(boughtProducts);
        mainPanel.add(totalCost);

        return mainPanel;
    }

    /**
     * Dynamically calculates the height of the center panel based on the order history size.
     *
     * @return The calculated panel height.
     */
    private int getPanelHeight() {
        if (customer.getCustomerOrderHistory().size() < 4) {
            return 3 * (getOrderHeight() + 10);
        } else if (customer.getCustomerOrderHistory().size() % 3 == 0) {
            return (customer.getCustomerOrderHistory().size() / 3) * (getOrderHeight() + 10);
        } else {
            return ((customer.getCustomerOrderHistory().size() / 3) + 1) * (getOrderHeight() + 10);
        }
    }

    /**
     * Dynamically calculates the height of an individual order panel.
     *
     * @return The calculated order height.
     */
    private int getOrderHeight() {
        return 500 * ((customer.getMaxProductsBought() / 25) + 1);
    }

    /**
     * Creates a button with consistent styling and adds an ActionListener.
     *
     * @param text The text displayed on the button.
     * @return A styled JButton instance.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(NORMAL_BUTTON_DIMENSIONS);
        button.setFocusable(false);
        button.setBackground(new Color(30, 30, 30));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, MEDIUM_FONTSIZE));
        button.addActionListener(this);
        return button;
    }

    /**
     * Creates a JLabel with consistent styling.
     *
     * @param text The text displayed on the label.
     * @param fontSize The font size of the label text.
     * @return A styled JLabel instance.
     */
    private JLabel createLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        return label;
    }

    /**
     * Creates a JPanel with the specified dimensions and background color.
     *
     * @param dimensions The dimensions of the panel.
     * @param color The background color of the panel.
     * @return A JPanel instance.
     */
    private JPanel createPanel(Dimension dimensions, Color color) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(dimensions);
        panel.setBorder(DEFAULT_EMPTYBORDER);
        panel.setBackground(color);
        return panel;
    }

    /**
     * Handles button click events for navigation and other actions.
     *
     * @param e The ActionEvent triggered by a button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton || e.getSource() == homeButton) {
            new CustomerFrame(customer.getUsername());
            this.setVisible(false);
        }

        if (e.getSource() == cartButton) {
            new CartFrame(customer);
            this.setVisible(false);
        }
    }
}