import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatAppLauncher extends JFrame {

    public ChatAppLauncher() {
        setTitle("Startup");
        setSize(400, 300);  // Setting the window size
        setLocationRelativeTo(null);  // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Setting the default shutdown operation

        // Create a tab to display a welcome message
        JLabel welcomeLabel = new JLabel("Welcome to the chat room.", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 18));

        // Create three buttons
        JButton startServerButton = new JButton("Start the server");
        JButton registerButton = new JButton("Sign up");
        JButton loginButton = new JButton("Log in");

        // Button Listener
        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the server
                new Thread(() -> {
                    Server.main(new String[0]);  // Call the Server's main method to start the server
                }).start();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the registration screen
                Register.main(new String[0]);  // Call the main method of Register
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the login screen
                ClientLogin.main(new String[0]);  // Call the main method of ClientLogin.
            }
        });

        // Adding components to the panel
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.add(welcomeLabel);
        panel.add(startServerButton);
        panel.add(registerButton);
        panel.add(loginButton);
        add(panel);

        setVisible(true);  // Settings window visible
    }

    public static void main(String[] args) {
        new ChatAppLauncher();  // Creating and displaying the application interface
    }
}
