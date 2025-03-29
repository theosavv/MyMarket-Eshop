package gui;

import api.Customer;
import api.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The {@code SignUpFrame} class represents the sign-up screen of the application.
 * It provides a graphical user interface (GUI) for new users to create an account.
 * The sign-up process includes entering a username, password, first name, and surname.
 * It also allows users to go back to the login screen if they already have an account.
 */
public class SignUpFrame extends JFrame implements ActionListener {

    private static final Dimension PANEL_DIMENSIONS = new Dimension(600, 70);
    private static final Dimension LABEL_DIMENSIONS = new Dimension(150, 50);

    private final JTextField usernameField, nameField, surnameField;
    private final JPasswordField passwordField;

    private final JButton submitButton, backButton;

    Database database = Database.getInstance();

    /**
     * Constructor that initializes the sign-up frame
     * and sets up the components and layout.
     */
    public SignUpFrame() {

        initializeFrame();

        JPanel usernamePanel = createPanel();
        JPanel passwordPanel = createPanel();
        JPanel namePanel = createPanel();
        JPanel surnamePanel = createPanel();
        JPanel submitPanel = createPanel();
        JPanel backButtonPanel = createPanel();

        usernameField = createTextField();
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 50));
        passwordField.setForeground(new Color(30,30,30));
        passwordField.setFont(new Font("Arial", Font.BOLD, 25));
        nameField = createTextField();
        surnameField = createTextField();

        usernamePanel.add(createLabel("Username:"));
        passwordPanel.add(createLabel("Password:"));
        namePanel.add(createLabel("Name:"));
        surnamePanel.add(createLabel("Surname:"));

        usernamePanel.add(usernameField);
        passwordPanel.add(passwordField);
        namePanel.add(nameField);
        surnamePanel.add(surnameField);

        submitButton = createButton("Sign up", new Dimension(200,50), 25);
        backButton = createButton("Sign in", new Dimension(100,25), 15);

        JLabel backLabel = new JLabel("I already have an account: ");
        backLabel.setForeground(new Color(30,30,30));
        backLabel.setFont(new Font("Arial", Font.ITALIC, 20));

        backButtonPanel.add(backLabel);
        backButtonPanel.add(backButton);
        submitPanel.add(submitButton);

        this.add(Box.createVerticalStrut(10));
        this.add(usernamePanel);
        this.add(Box.createVerticalStrut(5));
        this.add(passwordPanel);
        this.add(Box.createVerticalStrut(5));
        this.add(namePanel);
        this.add(Box.createVerticalStrut(5));
        this.add(surnamePanel);
        this.add(Box.createVerticalStrut(10));
        this.add(submitPanel);
        this.add(Box.createVerticalStrut(20));
        this.add(backButtonPanel);
        this.add(Box.createVerticalStrut(10));

        this.setVisible(true);
    }

    /**
     * Initializes the JFrame properties such as title, size, default close operation,
     * and sets the background color.
     * It also adds a window listener to prompt the user for confirmation when closing the application.
     */
    private void initializeFrame() {
        this.setTitle("SIGN UP");
        this.setSize(600, 470);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(200, 200, 200));
        this.setLocationRelativeTo(null);

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

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    }

    /**
     * Creates and returns a new JPanel with default layout and size.
     *
     * @return a new JPanel with FlowLayout and predefined size.
     */
    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(PANEL_DIMENSIONS);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        return panel;
    }

    /**
     * Creates and returns a JButton with the specified text, size, and font size.
     * The button also has an action listener attached to it.
     *
     * @param text the text to be displayed on the button.
     * @param dimension the preferred size of the button.
     * @param fontsize the font size for the button's text.
     * @return a JButton with the specified properties.
     */
    private JButton createButton(String text, Dimension dimension, int fontsize) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(dimension));
        button.setFont(new Font("Arial", Font.PLAIN, fontsize));
        button.setBackground(new Color(30,30,30));
        button.setForeground(new Color(255,255,255));
        button.setFocusable(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addActionListener(this);

        return button;
    }

    /**
     * Creates and returns a JLabel with the specified text and custom font properties.
     *
     * @param text the text to be displayed on the label.
     * @return a JLabel with the specified text and predefined font.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 25));
        label.setForeground(new Color(30,30,30));
        label.setPreferredSize(LABEL_DIMENSIONS);
        return label;
    }

    /**
     * Creates and returns a JTextField with custom properties for the username, name, and surname fields.
     *
     * @return a new JTextField with specific font, size, and alignment.
     */
    private JTextField createTextField() {
        JTextField textField = new JTextField("");
        textField.setFont(new Font("Arial", Font.BOLD, 30));
        textField.setForeground(new Color(30,30,30));
        textField.setPreferredSize(new Dimension(300, 50));
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        return textField;
    }

    /**
     * Handles the actions performed when the user clicks the submit or back buttons.
     * If the submit button is clicked, the application checks if all fields are filled and if the username is unique.
     * If the back button is clicked, the application navigates back to the login screen.
     *
     * @param e the ActionEvent triggered by a button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String username = usernameField.getText().trim();
            char[] passwordArray = passwordField.getPassword();
            String password = new String(passwordArray);
            String firstName = nameField.getText().trim();
            String surname = surnameField.getText().trim();

            if (username.isEmpty() || passwordArray.length == 0 || firstName.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are mandatory", "Empty Fields Error", JOptionPane.ERROR_MESSAGE);
            } else if (database.getAllCustomers().containsKey(username)) {
                JOptionPane.showMessageDialog(this, "This username already exists", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
            } else {
                Customer customer = new Customer(username, password, firstName, surname);
                database.addCustomer(username, customer);
                new LoginFrame();
                this.dispose();
            }
        }

        if (e.getSource() == backButton) {
            new LoginFrame();
            this.setVisible(false);
        }
    }
}