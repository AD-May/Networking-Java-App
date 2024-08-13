import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class TCPClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1080;
    private static PrintWriter out;
    private static BufferedReader in;
    private static JTextArea textArea = new JTextArea(20, 40);
    private static JTextField textField = new JTextField(40);

    public static void main(String[] args) {
        JFrame frame = new JFrame("TCP Client");
        textArea.setEditable(false);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText().trim();
                if (!message.isEmpty()) {
                    out.println(message);
                    textArea.append("Sent: " + message + "\n"); 
                    textField.setText("");
                }
            }
        });

        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            textArea.append("Connected to the server\n");

            new Thread(new ReceivedMessagesHandler(socket)).start();

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String command = in.readLine();

                if (command.equals("quit")) break;

                out.println(command);
            }

            socket.close();
            textArea.append("Connection closed\n");
        } catch (IOException e) {
            textArea.append("Unable to connect to the server\n");
            e.printStackTrace();
        }
    }

    private static class ReceivedMessagesHandler implements Runnable {
        private BufferedReader in;

        public ReceivedMessagesHandler(Socket socket) {
            try {
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                textArea.append("Error getting input stream: " + e.getMessage() + "\n");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    textArea.append("Received: " + message + "\n");
                }
            } catch (IOException e) {
                textArea.append("Error reading from server: " + e.getMessage() + "\n");
            }
        }
    }
}

