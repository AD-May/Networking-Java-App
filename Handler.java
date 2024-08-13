import java.io.*;
import java.net.*;

public class Handler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private ServerGUI serverGUI;  

    public Handler(Socket clientSocket, ServerGUI serverGUI) { 
        this.clientSocket = clientSocket;
        this.serverGUI = serverGUI;  
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error in Handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String receivedMessage;
            while ((receivedMessage = in.readLine()) != null) {
                System.out.println("Received: " + receivedMessage);
                serverGUI.updateLog("Received: " + receivedMessage);
                ServerGUI.broadcast(receivedMessage, this, serverGUI); 
            }
        } catch (IOException e) {
            System.out.println("Error in Handler: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void closeConnection() {
        ServerGUI.removeClient(this, serverGUI); 
        if (out != null) {
            out.close();
        }
        try {
            if (in != null) {
                in.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}