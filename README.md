# Java Chat Room System

## 📌 Project Overview
This project is a **Java-based chat room system** that includes **user registration and login functionalities**. The system consists of several main components, including:
- **User Registration & Login**
- **Message Transmission & Display**
- **Database Operations**
- **Multithreading for Server Handling**
- **Graphical User Interface (GUI) with Swing**

---

## 🚀 Advanced Features
✅ **Network Programming** – Uses `Socket` and `ServerSocket` for communication between client and server.  
✅ **Multithreading** – Handles multiple client connections using threads for improved responsiveness.  
✅ **GUI with Swing** – Provides a user-friendly interface for chat interactions.  
✅ **Database Operations** – Utilizes MySQL for user authentication and message storage.  
✅ **Custom Message Formatting** – Messages include **timestamp, sender name, and content**.  

---

## 🛠 File Structure & Descriptions
| **File Name**           | **Description** |
|-----------------------|-------------|
| `ChatAppLauncher.java` | Initializes and displays the main interface. |
| `ClientLogin.java`     | Manages user login, verifies identity, and connects to the server. |
| `Register.java`        | Handles user registration and saves data to MySQL. |
| `Server.java`          | Manages client connections and message broadcasting. |
| `Client.java`          | Connects to the server, sends, and receives messages. |
| `Message.java`         | Defines the structure of chat messages. |
| `DatabaseUtils.java`   | Provides database interaction functionalities. |

---

## 📥 Installation Guide
### 1️⃣ Environment Preparation
- Install **JDK (Java Development Kit)**.
- Install **MySQL database** and configure it.

### 2️⃣ Download the Project
- Clone the repository and place all Java files in the same project directory.

### 3️⃣ Configure the Database
- Create a MySQL database and import the required table structures.
- Update the **database connection settings** in `DatabaseUtils.java`.

---

## 🎯 Running Guide
### ✅ Start the System
1. **Compile and run `ChatAppLauncher.java`** to launch the main interface.

### ✅ Start the Server
1. Click **Start the Server** in `ChatAppLauncher`.
2. Click **Start Server** in the server interface to begin listening for client connections.

### ✅ User Registration
1. Click **Sign Up** in `ChatAppLauncher`.
2. Fill in your details and submit the form to register.

### ✅ User Login
1. Click **Login** in `ChatAppLauncher`.
2. Enter your credentials to access the chat room.

### ✅ Connect to the Chat Room
1. Enter your **name** in the `Client` chat room interface.
2. Click **Connect** to join the chat room and start sending messages.

---

## 🔹 Project Functionalities
- **📡 Server Startup & Management** – Displays chat messages and online users.
- **🔐 User Authentication** – Secure registration and login system.
- **💬 Message Handling** – Supports text and image messages.
- **🖥 Graphical Interface** – Built with `Swing` for an interactive user experience.

---

💡 **Explore the source code and experience real-time chat communication with Java!** 🚀

