import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Client-side implementation of the Multi-Client Chat Application.
 */
public class ChatClient {
    private static final String SERVER_HOST = "localhost"; // The server's hostname (change if necessary)
    private static final int SERVER_PORT = 12345; // The server's listening port

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)) {
            System.out.println("Connected to Chat Server: " + socket);

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Start a separate thread to handle server messages
            Thread serverThread = new Thread(new ServerHandler(socket));
            serverThread.start();

            Scanner scanner = new Scanner(System.in);
            String message;
            while (true) {
                message = scanner.nextLine();
                writer.println(message); // Send the message to the server
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This inner class handles messages received from the server
    private static class ServerHandler implements Runnable {
        private Socket socket;

        public ServerHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received message: " + message); // Print received messages
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
