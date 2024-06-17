import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientLogin extends JFrame {
    private JTextField userText;
    private JPasswordField pwdText;
    private JButton loginButton;
    public ClientLogin() {
        setTitle("Client login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new GridLayout(3, 2));

        add(new JLabel("ID:"));
        userText = new JTextField();
        add(userText);

        add(new JLabel("Password:"));
        pwdText = new JPasswordField();
        add(pwdText);

        loginButton = new JButton("Log in");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText().trim();
                String password = new String(pwdText.getPassword()).trim();
                if (DatabaseUtils.checkUser(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose();  // Close Login Window
                    runClient();  // Launching the client interface
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect ID or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(loginButton);
    }

    private void runClient() {
        // Assuming Client.java has a static main method to call
        Client.main(new String[0]);  // Call the Client's main method to start the client interface.
    }

    public static void main(String[] args) {
        new ClientLogin();
    }
}
