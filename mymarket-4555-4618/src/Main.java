import gui.*;

/**
 * The main class of the program that creates the login screen (LoginFrame) for the e-shop.
 * When the program is run, the {@code main} method creates the login window (LoginFrame),
 * which allows users to enter their credentials to access the e-shop.
 * After that, the program continues based on the user's choices and transitions
 * through other classes that handle the functionality of the e-shop.
 *
 * @see LoginFrame
 */
public class Main {
    public static void main(String[] args) {

        new LoginFrame();

    }
}