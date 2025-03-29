package gui;

import api.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The {@code LoginFrame} class represents the login screen of the application.
 * It provides a graphical user interface (GUI) for the user to enter their credentials.
 * The login process checks if the user is an admin or a customer and redirects accordingly.
 * It also includes an option for new users to sign up.
 */
public class LoginFrame extends JFrame implements ActionListener {

    /**
     * The dimensions of each panel in the login frame.
     */
    private static final Dimension PANEL_DIMENSIONS = new Dimension(600, 70);

    /**
     * The dimensions of each label in the login frame.
     */
    private static final Dimension LABEL_DIMENSIONS = new Dimension(150, 50);

    /**
     * The text field for entering the username.
     */
    private final JTextField usernameField;

    /**
     * The password field for entering the password.
     */
    private final JPasswordField passwordField;

    /**
     * The button to submit login credentials.
     */
    private final JButton submitButton;

    /**
     * The button to redirect to the sign-up screen.
     */
    private final JButton signUpButton;

    /**
     * The instance of the {@code Database} class used to validate user credentials.
     */
    Database database = Database.getInstance();

    /**
     * Constructor that initializes the login frame
     * and sets up the components and layout.
     */
    public LoginFrame() {
        initializeFrame();

        JPanel usernamePanel = createPanel();
        JPanel passwordPanel = createPanel();
        JPanel submitPanel = createPanel();
        JPanel signUpPanel = createPanel();

        JLabel signUpMessage = new JLabel("I don't have an account: ");
        signUpMessage.setFont(new Font("Arial", Font.ITALIC, 20));
        signUpMessage.setForeground(new Color(30, 30, 30));

        usernameField = new JTextField("");
        usernameField.setFont(new Font("Arial", Font.BOLD, 30));
        usernameField.setPreferredSize(new Dimension(300, 50));
        usernameField.setHorizontalAlignment(SwingConstants.LEFT);
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 50));
        passwordField.setFont(new Font("Arial", Font.BOLD, 25));

        submitButton = createButton("Login", new Dimension(200, 50), 25);
        signUpButton = createButton("Sign Up", new Dimension(100, 25), 15);

        usernamePanel.add(createLabel("Username:"));
        usernamePanel.add(usernameField);
        passwordPanel.add(createLabel("Password:"));
        passwordPanel.add(passwordField);
        submitPanel.add(submitButton);
        signUpPanel.add(signUpMessage);
        signUpPanel.add(signUpButton);

        this.add(Box.createVerticalStrut(10));
        this.add(usernamePanel);
        this.add(Box.createVerticalStrut(5));
        this.add(passwordPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(submitPanel);
        this.add(Box.createVerticalStrut(20));
        this.add(signUpPanel);
        this.add(Box.createVerticalStrut(10));

        this.setVisible(true);
    }

    /**
     * Initializes the {@code JFrame} properties such as title, size, default close operation,
     * and sets the background color.
     * It also adds a window listener to prompt the user for confirmation when closing the application.
     */
    private void initializeFrame() {
        this.setTitle("LOGIN");
        this.setSize(600, 370);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(200, 200, 200));
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to close the application?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

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
     * Creates and returns a new {@code JPanel} with default layout and size.
     *
     * @return a new panel with {@code FlowLayout} and predefined size.
     */
    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(PANEL_DIMENSIONS);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        return panel;
    }

    /**
     * Creates and returns a {@code JLabel} with the specified text and custom font properties.
     *
     * @param text the text to be displayed on the label.
     * @return a label with the specified text and predefined font.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 25));
        label.setForeground(new Color(30, 30, 30));
        label.setPreferredSize(LABEL_DIMENSIONS);
        return label;
    }

    /**
     * Creates and returns a {@code JButton} with the specified text, size, and font size.
     * The button also has an {@code ActionListener} attached to it.
     *
     * @param text     the text to be displayed on the button.
     * @param size     the preferred size of the button.
     * @param fontsize the font size for the button's text.
     * @return a button with the specified properties.
     */
    private JButton createButton(String text, Dimension size, int fontsize) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setBackground(new Color(30, 30, 30));
        button.setForeground(new Color(255, 255, 255));
        button.setFont(new Font("Arial", Font.PLAIN, fontsize));
        button.setFocusable(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addActionListener(this);
        return button;
    }

    /**
     * Handles the actions performed when the user clicks the login or sign-up buttons.
     * If the login button is pressed, it checks if the credentials match either an admin or a customer.
     * If the sign-up button is pressed, it transitions to the sign-up screen.
     *
     * @param e the {@code ActionEvent} triggered by a button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String username = usernameField.getText().trim();
            char[] passwordArray = passwordField.getPassword();
            String password = new String(passwordArray);

            if ((username.equals("admin1") && password.equals("password1")) || (username.equals("admin2") && password.equals("password2"))) {
                new AdminFrame(username);
                this.setVisible(false);
            } else if (database.getAllCustomers().containsKey(username) && database.getAllCustomers().get(username).getPassword().equals(password)) {
                new CustomerFrame(database.getAllCustomers().get(username).getUsername());
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }

            usernameField.setText("");
            passwordField.setText("");
        }

        if (e.getSource() == signUpButton) {
            new SignUpFrame();
            this.setVisible(false);
        }
    }
}