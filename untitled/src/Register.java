import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel messageLabel;

    public Register() {
        super("Sign up");
        initializeUI();
        setUpListeners();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // Center the window
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("ID:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        registerButton = new JButton("Register");
        add(registerButton);

        messageLabel = new JLabel();
        add(messageLabel);

        setVisible(true);
    }

    private void setUpListeners() {
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "ID and password cannot be empty!", "error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // user registration
                boolean success = DatabaseUtils.registerUser(username, password);
                if (success) {
                    messageLabel.setText("Sign-up successful!");
                } else {
                    messageLabel.setText("Sign-up failed, please try again!");
                }
            }
        });
    }

    public static void main(String[] args) {
        new Register(); // Create and display the registration screen
    }
}
