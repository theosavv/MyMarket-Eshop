package gui;

import api.Database;
import api.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The UserFrame class is an abstract class that provides the base structure for a GUI
 * application. It displays a user interface that includes a search bar, filter options,
 * and a product display area. It integrates functionality such as searching for products,
 * filtering based on categories and subcategories, and handling user interactions with buttons.
 * This class extends {@link JFrame} and implements {@link ActionListener}.
 * Subclasses must define the abstract methods for creating product panels and handling specific button actions.
 */
public abstract class UserFrame extends JFrame implements ActionListener {

    // Constants
    /** The height of the product panel. */
    protected static final int HEIGHT_PRODUCT_PANEL = 400;

    /** The font size for small text. */
    protected static final int SMALL_FONTSIZE = 15;

    /** The font size for medium text. */
    protected static final int MEDIUM_FONTSIZE = 25;

    /** The font size for large text. */
    protected static final int LARGE_FONTSIZE = 30;

    /** The font used throughout the application. */
    protected static final String ARIAL = "Arial";

    /** The default dimensions for buttons. */
    protected static final Dimension BUTTON_DIMENSIONS = new Dimension(150, 50);

    /** The default padding for panels. */
    protected static final EmptyBorder DEFAULT_EMPTYBORDER = new EmptyBorder(10, 10, 10, 10);

    // Instance variables
    protected JPanel northPanel;  // The top panel containing the menu and search bar.
    protected JPanel centerPanel;  // The center panel containing filters and product displays.
    protected JPanel filtersPanel;  // The panel for category and subcategory filters.
    protected JPanel productsPanel;  // The panel displaying the products.
    protected JPanel productPanel;  // Individual product panel.
    protected JButton submitSearchButton;  // Button to submit the search query.
    protected JButton signOutButton;  // Button to sign out of the application.
    protected JButton firstButton;  // First customizable button.
    protected JButton secondButton;  // Second customizable button.
    protected JButton backToCategoriesButton;  // Button to navigate back to the categories.
    protected JTextField searchBar;  // Text field for entering search queries.
    protected JRadioButton radioButton, lastSelectedRadioButton;  // Radio buttons for category selection.
    protected ButtonGroup radioButtonGroup;  // Group for radio buttons.
    protected HashMap<String, JPanel> productPanels;  // Map of product titles to their respective panels.
    protected HashSet<JCheckBox> checkboxButtons;  // Set of checkboxes for subcategory filters.
    protected final String username;  // Username of the currently logged-in user.

    // Database instance
    Database database = Database.getInstance();

    /**
     * Constructor for the UserFrame class.
     * Initializes the user interface components, sets up the layout, and configures listeners.
     *
     * @param username the username of the logged-in user.
     */
    UserFrame(String username) {
        this.username = username;

        initializeFrame(username);
        initializeMenu();
        initializeCenter();

        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    /**
     * Initializes the main frame properties such as title, size, layout, and window close behavior.
     *
     * @param username the username of the logged-in user.
     */
    private void initializeFrame(String username) {
        this.setTitle("Welcome, " + " " + username);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        lastSelectedRadioButton = null;

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
     * Initializes the top menu panel, including the search bar and action buttons.
     */
    private void initializeMenu() {
        northPanel = createPanel(new Dimension(0, 100), DEFAULT_EMPTYBORDER, null);
        northPanel.setLayout(new BorderLayout());

        JLabel nameOfMarket = createLabel("MyMarket", LARGE_FONTSIZE, new Color(30, 30, 30));

        JPanel northLeftPanel = createPanel(new Dimension(250, 100), DEFAULT_EMPTYBORDER, null);
        northLeftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JPanel northCenterPanel = createPanel((new Dimension(600, 100)), DEFAULT_EMPTYBORDER, null);
        northCenterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));

        JPanel northRightPanel = createPanel(new Dimension(480, 100), DEFAULT_EMPTYBORDER, null);
        northRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 10));

        signOutButton = createButton("Sign Out", BUTTON_DIMENSIONS);
        signOutButton.addActionListener(this);

        firstButton = createButton("", BUTTON_DIMENSIONS);
        firstButton.addActionListener(this);

        secondButton = createButton("", BUTTON_DIMENSIONS);
        secondButton.addActionListener(this);

        submitSearchButton = createButton("Submit", BUTTON_DIMENSIONS);
        submitSearchButton.addActionListener(this);

        searchBar = createTextField(new Font(ARIAL, Font.BOLD, MEDIUM_FONTSIZE), new Dimension(550, 50));
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (searchBar.getForeground().equals(Color.LIGHT_GRAY) && searchBar.getText().equals("Search MyMarket")) {
                    searchBar.setForeground(Color.BLACK);
                    searchBar.setText("");
                }
            }
        });

        northRightPanel.add(signOutButton);
        northRightPanel.add(firstButton);
        northRightPanel.add(secondButton);

        northCenterPanel.add(searchBar);
        northCenterPanel.add(submitSearchButton);

        northLeftPanel.add(nameOfMarket);

        northPanel.add(northRightPanel, BorderLayout.EAST);
        northPanel.add(northCenterPanel, BorderLayout.CENTER);
        northPanel.add(northLeftPanel, BorderLayout.WEST);
    }

    /**
     * Initializes the center panel, including filters and product displays.
     */
    private void initializeCenter() {
        centerPanel = createPanel(new Dimension(0, getHeightBasedOnProducts()), DEFAULT_EMPTYBORDER, new Color(30,30,30));
        centerPanel.setBorder(null);
        centerPanel.setLayout(new BorderLayout());

        filtersPanel = createPanel(new Dimension(500, getHeightBasedOnProducts()), null, Color.GRAY);
        filtersPanel.setBorder(null);
        filtersPanel.setLayout(new BoxLayout(filtersPanel, BoxLayout.Y_AXIS));

        radioButtonGroup = new ButtonGroup();

        filtersPanel.add(createLabel("Select Category", MEDIUM_FONTSIZE, new Color(30,30,30)));
        filtersPanel.add(Box.createVerticalStrut(10));

        for (String category : database.getCategories()) {
            radioButton = new JRadioButton(category);
            radioButton.setOpaque(false);
            radioButton.setFont(new Font(ARIAL, Font.BOLD, SMALL_FONTSIZE));
            radioButton.setFocusPainted(false);
            radioButton.setBorderPainted(false);
            radioButton.setContentAreaFilled(false);
            radioButtonGroup.add(radioButton);
            radioButton.addActionListener(this);
            filtersPanel.add(radioButton);
            filtersPanel.add(Box.createVerticalStrut(10));
        }

        filtersPanel.add(Box.createVerticalStrut(10));

        productsPanel = createPanel(new Dimension(0, getHeightBasedOnProducts()), null, new Color(30,30,30));
        productsPanel.setBorder(null);
        productsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        productPanels = new HashMap<>();

        for (Product product : database.getAllProducts()) {
            productPanel = createProduct(product);
            productPanels.put(product.getProductTitle(), productPanel);
            productsPanel.add(productPanel);
        }

        JScrollPane scrollFiltersPane = new JScrollPane(filtersPanel);
        JScrollPane scrollProductsPane = new JScrollPane(productsPanel);

        scrollFiltersPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        centerPanel.add(scrollFiltersPane, BorderLayout.WEST);
        centerPanel.add(scrollProductsPane, BorderLayout.CENTER);
    }

    /**
     * Returns the calculated height based on the number of products.
     *
     * @return the height for the product display panel.
     */
    private int getHeightBasedOnProducts() {
        int height;
        if ((database.getAllProducts().size())%2==0) {
            height = ((database.getAllProducts().size() * (HEIGHT_PRODUCT_PANEL + 5) / 2) + 5);
        } else {
            height = (((database.getAllProducts().size() + 1) * (HEIGHT_PRODUCT_PANEL + 5) / 2) + 5);
        }
        return height;
    }

    /**
     * Creates and returns a new panel with the specified properties.
     *
     * @param dimensions the dimensions of the panel.
     * @param emptyBorder the border for the panel.
     * @param color the background color of the panel.
     * @return the created panel.
     */
    protected JPanel createPanel(Dimension dimensions, EmptyBorder emptyBorder, Color color) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(dimensions));
        panel.setBorder(emptyBorder);
        panel.setBackground(color);
        return panel;
    }

    /**
     * Creates and returns a new button with the specified text and dimensions.
     *
     * @param text the text for the button.
     * @param dimensions the dimensions of the button.
     * @return the created button.
     */
    protected JButton createButton(String text, Dimension dimensions) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(dimensions));
        button.setFocusable(false);
        button.setBackground(new Color(30,30,30));
        button.setForeground(new Color(255,255,255));
        button.setFont(new Font("Arial", Font.BOLD, MEDIUM_FONTSIZE));
        return button;
    }

    /**
     * Creates and returns a new label with the specified text, font size, and color.
     *
     * @param text the text for the label.
     * @param fontSize the font size of the label.
     * @param color the color of the label.
     * @return the created label.
     */
    protected JLabel createLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(ARIAL, Font.BOLD, fontSize));
        label.setForeground(color);
        return label;
    }

    /**
     * Creates and returns a new text field with the specified font and dimensions.
     *
     * @param font the font for the text field.
     * @param dimensions the dimensions of the text field.
     * @return the created text field.
     */
    protected JTextField createTextField(Font font, Dimension dimensions) {
        JTextField textField = new JTextField("Search MyMarket");
        textField.setFont(font);
        textField.setPreferredSize(dimensions);
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        textField.setForeground(Color.LIGHT_GRAY);
        return textField;
    }

    /**
     * Performs a search for products based on the entered text in the search bar.
     * Updates the product display area with matching products.
     */
    void search() {
        productPanels.clear();
        productsPanel.removeAll();

        String searchingText = searchBar.getText().trim();

        boolean found = false;
        boolean defaultText = (searchBar.getForeground() != Color.LIGHT_GRAY && !searchingText.isEmpty());

        if (defaultText) {
            for (Product product : database.getAllProducts()) {
                if (product.getProductTitle().toLowerCase().contains(searchingText.toLowerCase()) || product.getProductCategory().toLowerCase().contains(searchingText.toLowerCase()) || product.getProductSubCategory().toLowerCase().contains(searchingText.toLowerCase())) {
                    productPanel = createProduct(product);
                    productPanels.put(product.getProductTitle(), productPanel);
                    productsPanel.add(productPanel);
                    found = true;
                }
            }
        }

        if (!defaultText || !found) {
            JOptionPane.showMessageDialog(this, "Product doesn't exist. Please try something else.", "Product Not Found", JOptionPane.INFORMATION_MESSAGE);
            searchBar.setText("");
            for (Product product : database.getAllProducts()) {
                productPanel = createProduct(product);
                productPanels.put(product.getProductTitle(), productPanel);
                productsPanel.add(productPanel);
            }
        }

        productsPanel.revalidate();
        productsPanel.repaint();
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    /**
     * Applies filters based on the selected radio button (category).
     * Updates the product and filter panels accordingly.
     *
     * @param selectedRadioButton the selected radio button for the category.
     */
    void appliedFilters(JRadioButton selectedRadioButton) {
        productPanels.clear();
        productsPanel.removeAll();
        filtersPanel.removeAll();

        if (selectedRadioButton == lastSelectedRadioButton || selectedRadioButton == null) {
            radioButtonGroup.clearSelection();

            filtersPanel.add(createLabel("Select Category", MEDIUM_FONTSIZE, new Color(30,30,30)));
            filtersPanel.add(Box.createVerticalStrut(10));

            for (String category : database.getCategories()) {
                radioButton = new JRadioButton(category);
                radioButton.setOpaque(false);
                radioButton.setFont(new Font(ARIAL, Font.BOLD, SMALL_FONTSIZE));
                radioButton.setFocusPainted(false);
                radioButton.setBorderPainted(false);
                radioButton.setContentAreaFilled(false);
                radioButton.addActionListener(this);
                radioButtonGroup.add(radioButton);
                filtersPanel.add(radioButton);
                filtersPanel.add(Box.createVerticalStrut(10));
            }

            for (Product product : database.getAllProducts()) {
                productPanel = createProduct(product);
                productPanels.put(product.getProductTitle(), productPanel);
                productsPanel.add(productPanel);
            }
        } else {
            lastSelectedRadioButton = selectedRadioButton;

            for (Product product : database.getAllProductsByCategory(selectedRadioButton.getText())) {
                productPanel = createProduct(product);
                productPanels.put(product.getProductTitle(), productPanel);
                productsPanel.add(productPanel);
            }

            filtersPanel.add(createLabel("Select subcategory", MEDIUM_FONTSIZE, new Color(30,30,30)));
            filtersPanel.add(Box.createVerticalStrut(10));

            checkboxButtons = new HashSet<>();
            for (String subcategory : database.getSubCategories(selectedRadioButton.getText())) {
                JCheckBox checkBox = new JCheckBox(subcategory);
                checkBox.setFont(new Font(ARIAL, Font.BOLD, SMALL_FONTSIZE));
                checkBox.setFocusPainted(false);
                checkBox.setBorderPainted(false);
                checkBox.setContentAreaFilled(false);
                checkBox.addActionListener(this);
                checkboxButtons.add(checkBox);
                filtersPanel.add(checkBox);
                filtersPanel.add(Box.createVerticalStrut(10));
            }

            backToCategoriesButton = createButton("Back to categories", BUTTON_DIMENSIONS);
            backToCategoriesButton.addActionListener(this);
            filtersPanel.add(backToCategoriesButton);
            filtersPanel.add(Box.createVerticalStrut(10));

            productsPanel.revalidate();
            productsPanel.repaint();
            filtersPanel.revalidate();
            filtersPanel.repaint();
        }

        productsPanel.revalidate();
        productsPanel.repaint();
        filtersPanel.revalidate();
        filtersPanel.repaint();
    }

    /**
     * Applies filters based on selected subcategories (checkboxes).
     * Updates the product display area with matching products.
     */
    void applyCheckboxFilters() {
        productPanels.clear();
        productsPanel.removeAll();

        HashSet<String> selectedSubcategories = new HashSet<>();
        for (JCheckBox checkBox : checkboxButtons) {
            if (checkBox.isSelected()) {
                selectedSubcategories.add(checkBox.getText());
            }
        }

        if (selectedSubcategories.isEmpty()) {
            for (Product product : database.getAllProductsByCategory(lastSelectedRadioButton.getText())) {
                productPanel = createProduct(product);
                productPanels.put(product.getProductTitle(), productPanel);
                productsPanel.add(productPanel);
            }
        } else {
            for (String subcategory : selectedSubcategories) {
                for (Product product : database.getAllProductsBySubCategory(subcategory)) {
                    productPanel = createProduct(product);
                    productPanels.put(product.getProductTitle(), productPanel);
                    productsPanel.add(productPanel);
                }
            }

            if (productPanels.isEmpty()) {
                productsPanel.add(createLabel("Unfortunately, there are no products available in this subcategory at the moment.", MEDIUM_FONTSIZE, new Color(255,255,255)));
            }

        }

        productsPanel.revalidate();
        productsPanel.repaint();
    }

    /**
     * Handles button click events and delegates actions based on the source of the event.
     *
     * @param e the action event triggered by a button click.
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

    /**
     * Abstract method to create a product panel. Must be implemented by subclasses.
     *
     * @param product the product for which the panel is created.
     * @return the created product panel.
     */
    abstract protected JPanel createProduct(Product product);

    /**
     * Abstract method to handle actions for the first button. Must be implemented by subclasses.
     */
    abstract protected void handleFirstButton();

    /**
     * Abstract method to handle actions for the second button. Must be implemented by subclasses.
     */
    abstract protected void handleSecondButton();
}
