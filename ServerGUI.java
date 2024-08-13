/* Author: Alexander May
   Last Edited: 8/12/2024
*/ 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class ServerGUI {
    private static final int PORT = 1080;
    private static List<Handler> clients = Collections.synchronizedList(new ArrayList<>());
    private JTextArea textArea = new JTextArea(20, 40);
    private JTextField textField = new JTextField(40); 
    private JFrame frame = new JFrame("TCP Server"); 
    private volatile boolean running = false;

    public ServerGUI() {
        textArea.setEditable(false);
        JButton button = new JButton("Start");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (button.getText().equals("Start")) {
                    button.setText("Stop");
                    new Thread(() -> startServer()).start();  
                } else {
                    button.setText("Start");
                    running = false;
                }
            }
        });

        textField.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText().trim();
                if (!message.isEmpty()) {
                    broadcast(message, null, ServerGUI.this);
                    textField.setText("");
                }
            }
        });

        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.getContentPane().add(textField, BorderLayout.NORTH); 
        frame.getContentPane().add(button, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    public void start() {
        frame.setVisible(true);
    }

    public void updateLog(String message) {
        SwingUtilities.invokeLater(() -> { 
            textArea.append(message + "\n");
        });
    }

    private void startServer() {
        running = true;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            updateLog("Server is running...");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                updateLog("New client connected");
                Handler clientHandler = new Handler(clientSocket, this);
                clients.add(clientHandler);

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException ex) {
            updateLog("Error in the server: " + ex.getMessage());
        }
    }

    public static void broadcast(String message, Handler sender, ServerGUI serverGUI) {
        synchronized (clients) {
            for (Handler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public static void removeClient(Handler client, ServerGUI serverGUI) {
        synchronized (clients) {
            clients.remove(client);
        }
    }
}