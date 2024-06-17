import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.nio.file.Files;

public class Client extends JFrame {
    private JTextField nameField, messageField;
    private JTextPane displayPane;
    private JButton connectButton, disconnectButton, sendButton, sendImageButton;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final String serverIP = "localhost";
    private final int serverPort = 6666;
    private StyledDocument document;

    public Client() {
        super("Chat Client");

        // Initialize the text input area
        nameField = new JTextField(20);
        messageField = new JTextField(20);
        messageField.setEditable(false);

        // display area
        displayPane = new JTextPane();
        displayPane.setEditable(false);
        document = displayPane.getStyledDocument();

        // Button initialization
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        sendButton = new JButton("Send");
        sendImageButton = new JButton("Send Image");

        disconnectButton.setEnabled(false);
        sendButton.setEnabled(false);
        sendImageButton.setEnabled(false);

        // Setting the Layout
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Name:"));
        topPanel.add(nameField);
        topPanel.add(connectButton);
        topPanel.add(disconnectButton);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Message:"));
        bottomPanel.add(messageField);
        bottomPanel.add(sendButton);
        bottomPanel.add(sendImageButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(displayPane), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        setupListeners();
    }

    private void setupListeners() {
        connectButton.addActionListener(e -> connectToServer());
        disconnectButton.addActionListener(e -> disconnectFromServer());
        sendButton.addActionListener(e -> sendMessage());
        sendImageButton.addActionListener(e -> sendImage());
    }

    private void connectToServer() {
        try {
            socket = new Socket(serverIP, serverPort);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeObject(nameField.getText()); // Send the client's name upon connection
            output.flush();

            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
            sendButton.setEnabled(true);
            messageField.setEditable(true);
            sendImageButton.setEnabled(true);

            listenForMessages();
        } catch (IOException ex) {
            displayMessage("Error connecting to server: " + ex.getMessage());
        }
    }

    private void disconnectFromServer() {
        try {
            output.close();
            input.close();
            socket.close();
            connectButton.setEnabled(true);
            disconnectButton.setEnabled(false);
            sendButton.setEnabled(false);
            messageField.setEditable(false);
            sendImageButton.setEnabled(false);
            displayMessage("Disconnected from server.");
        } catch (IOException ex) {
            displayMessage("Error disconnecting: " + ex.getMessage());
        }
    }

    private void sendMessage() {
        try {
            if (messageField.getText().trim().length() > 0) {
                output.writeObject(new Message(messageField.getText(), nameField.getText()));
                output.flush();
                messageField.setText("");
            }
        } catch (IOException ex) {
            displayMessage("Error sending message: " + ex.getMessage());
        }
    }

    private void sendImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose an image to send");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                byte[] imageBytes = Files.readAllBytes(file.toPath());
                output.writeObject(new Message(imageBytes, nameField.getText()));
                output.flush();
            } catch (IOException ex) {
                displayMessage("Error sending image: " + ex.getMessage());
            }
        }
    }

    private void listenForMessages() {
        new Thread(() -> {
            try {
                Object messageObj;
                while ((messageObj = input.readObject()) != null) {
                    if (messageObj instanceof Message) {
                        Message message = (Message) messageObj;
                        if (message.isText()) {
                            displayMessage(message.getTimestamp(), message.getSenderName(), message.getText());
                        } else if (message.isImage()) {
                            displayMessage(message.getTimestamp(), message.getSenderName(), " sent an image.");
                            displayImage(message.getImageBytes());
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                displayMessage("Connection lost.");
            }
        }).start();
    }

    private void displayMessage(final String timestamp, final String senderName, final String message) {
        SwingUtilities.invokeLater(() -> {
            try {
                StyleContext sc = StyleContext.getDefaultStyleContext();
                AttributeSet green = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.GREEN);
                AttributeSet blue = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLUE);

                document.insertString(document.getLength(), "[" + timestamp + "] ", green);
                document.insertString(document.getLength(), senderName + ": ", blue);
                document.insertString(document.getLength(), message + "\n", null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    private void displayMessage(final String message) {
        SwingUtilities.invokeLater(() -> {
            try {
                document.insertString(document.getLength(), message + "\n", null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    private void displayImage(byte[] imageBytes) {
        SwingUtilities.invokeLater(() -> {
            try {
                ImageIcon icon = new ImageIcon(imageBytes);
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImg);
                JLabel label = new JLabel(scaledIcon);
                displayPane.insertComponent(label);
                document.insertString(document.getLength(), "\n", null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        new Client();
    }
}
