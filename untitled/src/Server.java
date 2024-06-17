import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Server extends JFrame {
    private JButton startButton, stopButton;
    private JTextArea displayArea;
    private JLabel onlineUsersLabel;
    private JTextArea onlineUsersList;
    private ServerSocket serverSocket;
    private final int DEFAULT_PORT = 6666;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private Thread serverThread;

    public Server() {
        super("Chat Server");
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        onlineUsersLabel = new JLabel("Online Users: 0");
        onlineUsersList = new JTextArea(5, 20);
        onlineUsersList.setEditable(false);

        startButton.addActionListener(e -> startServer(DEFAULT_PORT));
        stopButton.addActionListener(e -> stopServer());

        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(startButton);
        topPanel.add(stopButton);
        topPanel.add(onlineUsersLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JLabel("Online Users:"), BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(onlineUsersList), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            displayMessage("Server started on port " + port);
            serverThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Socket socket = serverSocket.accept();
                        ClientHandler client = new ClientHandler(socket);
                        clients.add(client);
                        new Thread(client).start();
                        updateOnlineUsers();
                    } catch (IOException ex) {
                        displayMessage("Error accepting connection: " + ex.getMessage());
                        break;
                    }
                }
            });
            serverThread.start();
        } catch (IOException ex) {
            displayMessage("Error starting server: " + ex.getMessage());
        }
    }

    private void stopServer() {
        try {
            if (serverThread != null && !serverThread.isInterrupted()) {
                serverThread.interrupt();
            }
            for (ClientHandler client : clients) {
                client.closeConnection();
            }
            serverSocket.close();
            clients.clear();
            updateOnlineUsers();
            displayMessage("Server stopped.");
        } catch (IOException ex) {
            displayMessage("Error stopping server: " + ex.getMessage());
        }
    }

    private void displayMessage(final String message) {
        SwingUtilities.invokeLater(() -> displayArea.append(message + "\n"));
    }

    private void updateOnlineUsers() {
        SwingUtilities.invokeLater(() -> {
            onlineUsersLabel.setText("Online Users: " + clients.size());
            onlineUsersList.setText("");
            for (ClientHandler client : clients) {
                onlineUsersList.append(client.getName() + "\n");
            }
        });
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                this.name = (String) input.readObject();
                displayMessage(name + " connected.");
                updateOnlineUsers(); // Update users list on new connection
            } catch (IOException | ClassNotFoundException ex) {
                displayMessage("Error setting up streams: " + ex.getMessage());
            }
        }

        public void run() {
            try {
                Message message;
                while ((message = (Message) input.readObject()) != null) {
                    if (message.isText()) {
                        broadcastMessage(new Message(message.getText(), name));
                        displayMessage("[" + message.getTimestamp() + "] " + name + ": " + message.getText());
                    } else if (message.isImage()) {
                        broadcastMessage(new Message(message.getImageBytes(), name));
                        displayMessage("[" + message.getTimestamp() + "] " + name + " sent an image.");
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                displayMessage(name + " has disconnected.");
            } finally {
                closeConnection();
            }
        }

        private void broadcastMessage(Message message) {
            for (ClientHandler client : clients) {
                try {
                    client.output.writeObject(message);
                    client.output.flush();
                } catch (IOException ex) {
                    displayMessage("Error broadcasting message: " + ex.getMessage());
                }
            }
        }

        private void closeConnection() {
            try {
                input.close();
                output.close();
                socket.close();
                clients.remove(this);
                updateOnlineUsers();
                displayMessage(name + " disconnected.");
            } catch (IOException ex) {
                displayMessage("Error closing connection: " + ex.getMessage());
            }
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
